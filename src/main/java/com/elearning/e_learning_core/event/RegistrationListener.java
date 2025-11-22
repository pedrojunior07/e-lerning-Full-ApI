package com.elearning.e_learning_core.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.elearning.e_learning_core.exceptions.EmailSendingException;
import com.elearning.e_learning_core.model.OnRegistrationCompleteEvent;
import com.elearning.e_learning_core.model.Usr;
import com.elearning.e_learning_core.service.UsrService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UsrService service;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    @Value("${APP_BASE_URL}")
    private String appBaseUrl;

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        Usr user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "🎓 Confirme seu cadastro - E-Learning";
        String confirmationUrl = appBaseUrl + "/e-learning/api/auth/registrationConfirm?token=" + token;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("ressureicaomacuacua27@gmail.com");
            helper.setTo(recipientAddress);
            helper.setSubject(subject);

            String userName = user.getPerson().getFirstName() != null
                ? user.getPerson().getFirstName() + " " + (user.getPerson().getLastName() != null ? user.getPerson().getLastName() : "")
                : user.getEmail();

            String htmlContent = """
                    <html>
                    <head>
                        <meta charset="UTF-8">
                    </head>
                    <body style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f8f9fa; padding: 0; margin: 0;">
                        <div style="max-width: 600px; margin: 40px auto; background-color: white; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); overflow: hidden;">

                            <!-- Header -->
                            <div style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 40px 30px; text-align: center;">
                                <h1 style="color: white; margin: 0; font-size: 28px;">🎓 E-Learning</h1>
                                <p style="color: rgba(255,255,255,0.9); margin: 10px 0 0 0; font-size: 14px;">Plataforma de Ensino Online</p>
                            </div>

                            <!-- Content -->
                            <div style="padding: 40px 30px;">
                                <h2 style="color: #2c3e50; margin: 0 0 20px 0; font-size: 24px;">Bem-vindo(a)!</h2>

                                <p style="font-size: 16px; color: #555; line-height: 1.6; margin: 0 0 15px 0;">
                                    Olá <strong style="color: #2c3e50;">%s</strong>,
                                </p>

                                <p style="font-size: 16px; color: #555; line-height: 1.6; margin: 0 0 25px 0;">
                                    Obrigado por se registrar em nossa plataforma! Estamos muito felizes em tê-lo(a) conosco.
                                    Para começar sua jornada de aprendizado, confirme seu e-mail clicando no botão abaixo:
                                </p>

                                <div style="text-align: center; margin: 35px 0;">
                                    <a href="%s" style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 16px 40px; border-radius: 8px; text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);">
                                        ✓ Confirmar Meu E-mail
                                    </a>
                                </div>

                                <p style="font-size: 14px; color: #888; line-height: 1.6; margin: 25px 0 0 0;">
                                    <strong>Não consegue clicar no botão?</strong><br>
                                    Copie e cole este link no seu navegador:<br>
                                    <a href="%s" style="color: #667eea; word-break: break-all;">%s</a>
                                </p>

                                <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">

                                <p style="font-size: 13px; color: #999; line-height: 1.5; margin: 0;">
                                    Este link expira em 24 horas. Se você não criou uma conta em nossa plataforma,
                                    por favor ignore este e-mail.
                                </p>
                            </div>

                            <!-- Footer -->
                            <div style="background-color: #f8f9fa; padding: 25px 30px; text-align: center; border-top: 1px solid #eee;">
                                <p style="font-size: 14px; color: #666; margin: 0 0 10px 0;">
                                    <strong>Equipe E-Learning</strong>
                                </p>
                                <p style="font-size: 12px; color: #999; margin: 0;">
                                    © 2024 E-Learning Platform. Todos os direitos reservados.
                                </p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(userName, confirmationUrl, confirmationUrl, confirmationUrl);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new EmailSendingException("Falha ao enviar email de confirmação para " + recipientAddress, e);
        }
    }
}
