package com.smartvision.smartvision_backend.security;

import com.smartvision.smartvision_backend.entity.global.SecurityLog;
import com.smartvision.smartvision_backend.repository.global.SecurityLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider     jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final SecurityLogRepository securityLogRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest  request,
            HttpServletResponse response,
            FilterChain         filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            // 1. Extraire le token
            String token = extractToken(request);

            // 2. Valider et authentifier
            if (token != null
                    && jwtTokenProvider.validateToken(token)) {

                String cin   = jwtTokenProvider
                        .getCinFromToken(token);
                Long   orgId = jwtTokenProvider
                        .getOrgIdFromToken(token);

                // 3. Charger l'utilisateur
                UserDetails userDetails = userDetailsService
                        .loadUserByUsernameAndOrg(cin, orgId);

                // 4. Créer l'authentification Spring Security
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // 5. Mettre dans le contexte Spring Security
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }

        } catch (Exception e) {
            log.error("Erreur authentification : {}",
                    e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // 6. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);

        // 7. Logger la requête après réponse
        logRequest(request, response, startTime);
    }

    // ── Extraire token du header ───────────────────────
    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null
                && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    // ── Logger chaque requête ──────────────────────────
    private void logRequest(HttpServletRequest  request,
                            HttpServletResponse response,
                            long                startTime) {
        try {
            int responseMs = (int)(System.currentTimeMillis()
                    - startTime);

            SecurityLog log = SecurityLog.createLog(
                    getClientIp(request),
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus(),
                    responseMs,
                    request.getHeader("User-Agent"));

            // Détecter requêtes suspectes
            if (response.getStatus() == 401
                    || response.getStatus() == 403) {
                log.setSuspicious(false);
            }

            securityLogRepository.save(log);

        } catch (Exception e) {
            log.error("Erreur logging : {}", e.getMessage());
        }
    }

    // ── Extraire IP réelle (proxy/load balancer) ───────
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        // Si multiple IPs (proxy chain) → prendre la première
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    // ── Ne pas filtrer les routes publiques ────────────
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/")
                || path.startsWith("/actuator/");
    }
}
