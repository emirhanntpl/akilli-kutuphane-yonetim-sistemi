package com.library.library.service;

import com.library.library.model.Loan;
import com.library.library.repository.LoansRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final LoansRepository loansRepository;
    private final EmailService emailService;


    @Scheduled(cron = "0 0 9 * * *") 
    public void sendDueDateReminders() {
        System.out.println("Hatırlatma mailleri gönderiliyor...");
        

        LocalDate reminderDate = LocalDate.now().plusDays(3);
        List<Loan> loansToRemind = loansRepository.findByDueDateAndReturnDateIsNull(reminderDate);

        for (Loan loan : loansToRemind) {
            String subject = "Kitap Teslim Tarihi Hatırlatması";
            String text = "Merhaba " + loan.getUser().getName() + ",\n\n"
                        + "Ödünç aldığınız '" + loan.getBook().getTitle() + "' adlı kitabın teslim tarihine 3 gün kaldı.\n"
                        + "Son teslim tarihi: " + loan.getDueDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        + "\n\nLütfen kitabınızı zamanında iade etmeyi unutmayın.";
            
            emailService.sendEmail(loan.getUser().getEmail(), subject, text);
            System.out.println("Hatırlatma maili gönderildi: " + loan.getUser().getEmail());
        }
        
        System.out.println("Hatırlatma maili gönderme işlemi tamamlandı.");
    }
}
