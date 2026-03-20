package com.smartvision.smartvision_backend.service.auth;

import com.smartvision.smartvision_backend.entity.global.OtpCode;
import com.smartvision.smartvision_backend.repository.global.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final JavaMailSender    mailSender;

    // ── Générer et envoyer OTP ─────────────────────────
    @Transactional
    public void generateAndSendOtp(
            String email,
            OtpCode.OtpType type) {

        // 1. Supprimer les anciens OTPs du même type
        otpCodeRepository.deleteAllByEmailAndType(
                email, type);

        // 2. Générer code 6 chiffres
        String code = generateCode();

        // 3. Sauvegarder en BDD
        OtpCode otp = OtpCode.builder()
                .email(email)
                .code(code)
                .type(type)
                .isUsed(false)
                .expiresAt(LocalDateTime.now()
                        .plusMinutes(10))
                .build();
        otpCodeRepository.save(otp);

        // 4. Envoyer par email (async)
        sendOtpEmail(email, code, type);
    }

    // ── Valider OTP ────────────────────────────────────
    @Transactional
    public boolean validateOtp(
            String email,
            String code,
            OtpCode.OtpType type) {

        Optional<OtpCode> otpOpt =
                otpCodeRepository.findLatestValidOtp(
                        email, type);

        if (otpOpt.isEmpty()) {
            log.warn("OTP non trouvé pour : {}", email);
            return false;
        }

        OtpCode otp = otpOpt.get();

        // Vérifier le code et l'expiration
        if (!otp.isValid()) {
            log.warn("OTP expiré pour : {}", email);
            return false;
        }

        if (!otp.getCode().equals(code)) {
            log.warn("OTP incorrect pour : {}", email);
            return false;
        }

        // Marquer comme utilisé
        otp.markAsUsed();
        otpCodeRepository.save(otp);
        return true;
    }

    // ── Envoyer email OTP ──────────────────────────────
    @Async
    public void sendOtpEmail(
            String email,
            String code,
            OtpCode.OtpType type) {
        try {
            MimeMessage message =
                    mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(getSubject(type));
            helper.setText(
                    buildEmailHtml(code, type), true);

            mailSender.send(message);
            log.info("OTP envoyé à : {}", email);

        } catch (MessagingException e) {
            log.error("Erreur envoi OTP : {}",
                    e.getMessage());
        }
    }

    // ── Générer code 6 chiffres ────────────────────────
    private String generateCode() {
        return String.format("%06d",
                new Random().nextInt(999999));
    }

    // ── Sujet email selon type ─────────────────────────
    private String getSubject(OtpCode.OtpType type) {
        return switch (type) {
            case REGISTRATION    ->
                    "SmartVision — Code de verification inscription";
            case LOGIN           ->
                    "SmartVision — Code de connexion";
            case RESET_PASSWORD  ->
                    "SmartVision — Code reinitialisation mot de passe";
        };
    }

    // ── Template HTML email ────────────────────────────
    private String buildEmailHtml(
            String code,
            OtpCode.OtpType type) {
        String action = switch (type) {
            case REGISTRATION   -> "votre inscription";
            case LOGIN          -> "votre connexion";
            case RESET_PASSWORD -> "la reinitialisation de votre mot de passe";
        };

        return """
            <!DOCTYPE html>
            <html>
            <body style="font-family:Arial,sans-serif;
                         background:#F0F4F8;
                         padding:20px;">
              <div style="max-width:500px;
                          margin:auto;
                          background:white;
                          border-radius:12px;
                          overflow:hidden;
                          box-shadow:0 4px 12px rgba(0,0,0,0.1);">
                <div style="background:#1A3C5E;
                            padding:24px;
                            text-align:center;">
                  <h1 style="color:white;
                             margin:0;
                             font-size:24px;">
                    SmartVision
                  </h1>
                </div>
                <div style="padding:32px;
                            text-align:center;">
                  <p style="color:#5D6D7E;font-size:16px;">
                    Votre code pour %s
                  </p>
                  <div style="background:#EBF5FB;
                              border:2px solid #2E86C1;
                              border-radius:12px;
                              padding:24px;
                              margin:24px 0;">
                    <span style="font-size:42px;
                                 font-weight:bold;
                                 color:#1A3C5E;
                                 letter-spacing:12px;">
                      %s
                    </span>
                  </div>
                  <p style="color:#E74C3C;
                            font-size:14px;">
                    Ce code expire dans <strong>10 minutes</strong>
                  </p>
                  <p style="color:#5D6D7E;
                            font-size:12px;">
                    Si vous n'etes pas a l'origine de cette
                    demande, ignorez cet email.
                  </p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(action, code);
    }
}