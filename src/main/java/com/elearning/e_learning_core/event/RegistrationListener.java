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
        String subject = "Confirmação de Registro - E-Learning";
        String confirmationUrl = appBaseUrl + "/registrationConfirm?token=" + token;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("ressureicaomacuacua27@gmail.com");
            helper.setTo(recipientAddress);
            helper.setSubject(subject);

            String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;">
                        <div style="max-width: 600px; margin: auto; background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                            <h2 style="color: #2c3e50;">Bem-vindo à Plataforma E-Learning!</h2>
                            <p style="font-size: 16px; color: #333;">
                                Olá <strong>%s</strong>,
                            </p>
                            <p style="font-size: 16px; color: #333;">
                                Obrigado por se registrar. Para concluir seu cadastro, clique no botão abaixo para confirmar seu e-mail:
                            </p>
                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s" style="background-color: #3498db; color: white; padding: 12px 20px; border-radius: 5px; text-decoration: none; font-size: 16px;">
                                    Confirmar Cadastro
                                </a>
                            </div>
                            <p style="font-size: 14px; color: #888;">
                                Se você não realizou esse registro, ignore este e-mail.
                            </p>
                            <p style="font-size: 14px; color: #888;">
                                Atenciosamente,<br>
                                Equipe E-Learning
                            </p>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(user.getPerson().getFirstName() + " " + user.getPerson().getLastName(), confirmationUrl);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new EmailSendingException("Falha ao enviar email de confirmação para " + recipientAddress, e);
        }
    }
}
