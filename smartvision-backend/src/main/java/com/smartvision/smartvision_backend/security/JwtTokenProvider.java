package com.smartvision.smartvision_backend.security;

import com.smartvision.smartvision_backend.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    // ── Générer Access Token ───────────────────────────
    public String generateAccessToken(String cin,
                                      Long orgId,
                                      String role,
                                      String schema) {
        return Jwts.builder()
                .subject(cin)
                .claim("org_id", orgId)
                .claim("role",   role)
                .claim("schema", schema)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis()
                                + jwtConfig.getExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    // ── Générer Refresh Token ──────────────────────────
    public String generateRefreshToken(String cin,
                                       Long orgId) {
        return Jwts.builder()
                .subject(cin)
                .claim("org_id", orgId)
                .claim("type",   "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis()
                                + jwtConfig.getRefreshExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    // ── Valider Token ──────────────────────────────────
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token expiré : {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("Token invalide : {}", e.getMessage());
        }
        return false;
    }

    // ── Extraire CIN ───────────────────────────────────
    public String getCinFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // ── Extraire OrgId ─────────────────────────────────
    public Long getOrgIdFromToken(String token) {
        return getClaims(token)
                .get("org_id", Long.class);
    }

    // ── Extraire Role ──────────────────────────────────
    public String getRoleFromToken(String token) {
        return getClaims(token)
                .get("role", String.class);
    }

    // ── Extraire Schema ────────────────────────────────
    public String getSchemaFromToken(String token) {
        return getClaims(token)
                .get("schema", String.class);
    }

    // ── Vérifier expiration ────────────────────────────
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // ── Privé : extraire Claims ────────────────────────
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ── Privé : clé de signature ───────────────────────
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtConfig.getSecret()
                        .getBytes(StandardCharsets.UTF_8));
    }
}