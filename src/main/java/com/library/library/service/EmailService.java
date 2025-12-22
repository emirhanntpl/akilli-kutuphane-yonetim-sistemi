package com.library.library.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        System.out.println("EmailService başlatıldı.");
    }

    public void sendEmail(String to, String subject, String text) {
        System.out.println("Mail gönderimi tetiklendi. Alıcı: " + to);
        System.out.println("Gönderici (from): " + fromEmail);
        
        if (fromEmail == null || fromEmail.isEmpty()) {
            System.err.println("HATA: 'spring.mail.username' application.properties dosyasından okunamadı!");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            System.out.println("Mail başarıyla gönderildi: " + to);
        } catch (Exception e) {
            System.err.println("Mail gönderilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
