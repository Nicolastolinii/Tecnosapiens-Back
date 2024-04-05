package com.dblog.dblog.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private  JavaMailSender javaMailSender;
    public void sendOtpEmail(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verificar OTP");
        message.setText("Hola, tu OTP es el siguiente: " + otp);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Su código de verificación OTP");
            String text = """
                    <div style="background-color: #2F2E2E; padding: 30px; width: auto;  border-radius: 10px; ">
                            <div style="display: flex;">
                                <h1 style="color: white; font-size: 22px; margin: 0; padding-left: 4px; padding-top: 10px;">TECNOSAPIENS</h1>
                            </div>
                            <div style=" text-align: center; padding-bottom: 5.1rem;">
                                        <p style="font-size: 24px; padding-bottom: 2rem; font-weight: 700; color: white; margin-bottom: 20px;">¡Gracias por registrarse! Confirma tu dirección de correo electronico:</p>
                                        
                                            <a href="http://localhost:8080/validate/verify-account?email=%s&otp=%s" target="_blank" style="text-decoration: none;  color: white; background-color: #F8A22D;  color: white; border: none; cursor: pointer; text-decoration: none; font-size: 18px; padding: 12px 20px; border-radius: 5px;">Click Aqui</a>
                                        
                                    </div>
                            <span style="color: rgb(226, 226, 226);">*Luego de verificar el correo, tu cuenta tiene que ser habilitada por un administrador.</span>
                        </div>
                            """;
            mimeMessageHelper.setText(text.formatted(email,otp),true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
