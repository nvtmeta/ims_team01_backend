package fsa.training.ims_team01.service;

import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule;
import fsa.training.ims_team01.model.dto.email.EmailSendOffer;
import fsa.training.ims_team01.model.entity.User;

import java.util.List;

public interface EmailService {

    void sendEmailCreateUser(User user, String generatePassword);

    void sendEmailInterviewSchedule(EmailSendDto candidateEmail, List<EmailSendDto> interviewerEmails);

    void sendReminderScheduleEmail(EmailSendInterviewSchedule interviewSchedule);
    void sendReminderOfferEmail(EmailSendOffer interviewSchedule);

    void sendResetPasswordEmail(String email, String token);
}
