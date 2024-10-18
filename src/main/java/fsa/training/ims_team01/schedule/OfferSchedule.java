package fsa.training.ims_team01.schedule;

import fsa.training.ims_team01.model.dto.email.EmailSendOffer;
import fsa.training.ims_team01.service.EmailService;
import fsa.training.ims_team01.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OfferSchedule {
    private final OfferService offerService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *") // runs at 8:00 AM every day
    public void reminder() {
        List<EmailSendOffer> emailSendOffer = offerService.findByOfferDueDate(LocalDate.now());
        System.out.println("emailSendOffer: " + emailSendOffer);
        emailSendOffer
                .forEach(emailService::sendReminderOfferEmail);

    }
}
