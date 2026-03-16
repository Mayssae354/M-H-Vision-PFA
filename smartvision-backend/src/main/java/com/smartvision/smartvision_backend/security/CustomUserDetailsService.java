package com.smartvision.smartvision_backend.security;

import com.smartvision.smartvision_backend.entity.global.User;
import com.smartvision.smartvision_backend.entity.global.UserOrganization;
import com.smartvision.smartvision_backend.repository.global.UserOrganizationRepository;
import com.smartvision.smartvision_backend.repository.global.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserOrganizationRepository userOrganizationRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String cin)
            throws UsernameNotFoundException {

        // 1. Chercher l'utilisateur par CIN
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> {
                    log.warn("Utilisateur non trouvé : {}", cin);
                    return new UsernameNotFoundException(
                            "Utilisateur non trouvé : " + cin);
                });

        // 2. Vérifier que le compte est actif
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException(
                    "Compte non activé : " + cin);
        }

        // 3. Récupérer tous les rôles de l'utilisateur
        List<SimpleGrantedAuthority> authorities =
                userOrganizationRepository
                        .findActiveOrgsByUserCin(cin)
                        .stream()
                        .map(uo -> new SimpleGrantedAuthority(
                                "ROLE_" + uo.getRole().name()))
                        .toList();

        // 4. Retourner UserDetails Spring Security
        return org.springframework.security.core.userdetails
                .User.builder()
                .username(user.getCin())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive())
                .build();
    }

    // ── Charger avec orgId spécifique ──────────────────
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsernameAndOrg(
            String cin, Long orgId)
            throws UsernameNotFoundException {

        User user = userRepository.findByCin(cin)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Utilisateur non trouvé : " + cin));

        // Vérifier que l'user appartient à cette org
        UserOrganization userOrg = userOrganizationRepository
                .findByUserCinAndOrganizationId(cin, orgId)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Accès refusé à cette organisation"));

        // Vérifier statut dans cette org
        if (userOrg.getStatus() !=
                UserOrganization.Status.ACTIVE) {
            throw new UsernameNotFoundException(
                    "Compte suspendu dans cette organisation");
        }

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + userOrg.getRole().name());

        return org.springframework.security.core.userdetails
                .User.builder()
                .username(user.getCin())
                .password(user.getPasswordHash())
                .authorities(authority)
                .disabled(!user.getIsActive())
                .build();
    }
}