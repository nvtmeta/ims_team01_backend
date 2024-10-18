package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.model.dto.email.EmailDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule;
import fsa.training.ims_team01.model.dto.email.EmailSendOffer;
import fsa.training.ims_team01.model.entity.User;
import fsa.training.ims_team01.service.EmailService;
import fsa.training.ims_team01.service.SendGridService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final SendGridService sendGridService;

    @Override
    public void sendEmailCreateUser(User user, String generatePassword) {
        //send two diff password and account for 2 email send
        String content = "<html><body>"
                + "<h2>Hello " + user.getUsername() + "!</h2><br>"
                + "<p>You have successfully registered a new account.</p><br>"
                + "<p>We have sent you your username and password below:</p><br>"
                + "<table border='0' cellpadding='5'>"
                + "<tr><td><b>Username:</b></td><td>" + user.getUsername() + "</td></tr>"
                + "<tr><td><b>Password:</b></td><td>" + generatePassword + "</td></tr>"
                + "</table><br>"
                + "<p>Please do not share this email with anyone.</p><br>"
                + "</body></html>";


        EmailDto emailDto = EmailDto.builder()
                .subject("no-reply-email-IMS-system <Account created> - " + user.getUsername())
                .emailToList(List.of(user.getEmail()))
                .body(content)
                .build();

        System.out.println("emailDto: " + emailDto);
        sendGridService.sendEmail(emailDto);
    }

    @Override
    public void sendEmailInterviewSchedule(EmailSendDto candidateEmail, List<EmailSendDto> interviewerEmails) {
        // Send email to candidate
        String candidateContent = "<html><body>"
                + "<h2>Hello " + candidateEmail.getFullName() + "!</h2><br>"
                + "<p>Your interview schedule has been confirmed.</p><br>"
                + "</body></html>";

        EmailDto candidateEmailDto = EmailDto.builder()
                .subject("Interview Schedule Confirmation")
                .emailToList(List.of(candidateEmail.getEmail()))
                .body(candidateContent)
                .build();

        System.out.println("candidateEmailDto " + candidateEmailDto.getEmailToList());
        sendGridService.sendEmail(candidateEmailDto);

        // Send email to interviewers

        String interviewerContent = "<html><body>"
                + "<h2>Hello Interviewers!</h2><br>"
                + "<p>The interview schedule has been confirmed.</p><br>"
                + "</body></html>";
        EmailDto interviewerEmailDto = EmailDto.builder()
                .subject("Interview Schedule Confirmation")
                .emailToList(interviewerEmails.stream().map(EmailSendDto::getEmail).toList())
                .body(interviewerContent)
                .build();

        System.out.println("interviewerEmailDto: " + interviewerEmailDto.getEmailToList());
        sendGridService.sendEmail(interviewerEmailDto);
    }

    @Override
    public void sendReminderScheduleEmail(EmailSendInterviewSchedule emailSendInterviewSchedule) {
        System.out.println("sendReminderEmail: " + emailSendInterviewSchedule);
        String candidateName = emailSendInterviewSchedule.getCandidateName();
        Long id = emailSendInterviewSchedule.getId();
        String candidatePosition = emailSendInterviewSchedule.getCandidatePosition();
        String interviewTime = emailSendInterviewSchedule.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String meetingId = emailSendInterviewSchedule.getMeetingId();
        String date = emailSendInterviewSchedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String endTime = emailSendInterviewSchedule.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String interviewerContent = "<html><body>"
                + "This email is from IMS system,<br>"
                + "You have an interview schedule on " + date + " from " + interviewTime + " to " + endTime + " with Candidate " + candidateName + " for position " + candidatePosition + ".<br>"
                + "The CV is attached with this no reply email "   + ".<br>"
                + "If anything wrong, please refer recruiter " + emailSendInterviewSchedule.getRecruiterEmail() + " or visit our website "
                + "http://localhost:3000/interview/" + id + ".<br>"
                + "Please join interview room ID: " + meetingId + ".<br>"
                + "</body></html>";
        EmailDto interviewerEmailDto = EmailDto.builder()
                .subject("Reminder interview schedule " + emailSendInterviewSchedule.getTitle())
                .emailToList(emailSendInterviewSchedule.getInterviewerEmails())
                .body(interviewerContent)
                .build();

        System.out.println("interviewerEmailDto: " + interviewerEmailDto.getEmailToList());
        sendGridService.sendEmail(interviewerEmailDto);
    }

    @Override
    public void sendReminderOfferEmail(EmailSendOffer emailSendOffer) {
        String candidateName = emailSendOffer.getCandidateName();
        String candidatePosition = emailSendOffer.getCandidatePosition();
        String offerDueDate = emailSendOffer.getOfferDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String managerEmail = emailSendOffer.getManagerEmail();
        String recruiterEmail = emailSendOffer.getRecruiterEmail();

        System.out.println("recruiterEmail: " + recruiterEmail);
        String emailBody = "<html><body>"
                + "<h4>This email is from IMS system.</h4>"
                + "<p>You have an offer to take action For Candidate " + candidateName + ".</p>"
                + "<p>Position " + candidatePosition + " before " + offerDueDate + ", the contract is attached with this no-reply-email.</p>"
                + "<p>Please refer this link to take action: <a href='http://localhost:3000/offer/" + emailSendOffer.getId() + "'>Take Action</a></p>"
                + "<p>If anything wrong, please reach-out recruiter " + recruiterEmail + ".</p>"
                + "<p>We are so sorry for this inconvenience.</p>"
                + "<p>Thanks & Regards!</p>"
                + "<p>IMS Team.</p>"
                + "</body></html>";

        EmailDto emailDto = EmailDto.builder()
                .subject("Reminder: Take Action on Your Offer !")
                .emailToList(Collections.singletonList(managerEmail)) // Assuming the recruiter email is the one receiving this email
                .body(emailBody)
                .build();

        sendGridService.sendEmail(emailDto);
    }

    @Override
    public void sendResetPasswordEmail(String email, String token) {
        String subject = "Password Reset";
        String body = "<html><body>"
                + "<h4>We have just received a password reset request for " + email + ".</h4>"
                + "<p>Please click here to reset your password: <a href='http://localhost:3000/reset-password?token=" + token + "'>Reset Password</a></p>"
                + "<p_For your security, the link will expire in 24 hours or immediately after you reset your password.</p>"
                + "<p>Thanks & Regards!</p>"
                + "<p>IMS Team.</p>"
                + "</body></html>";

        EmailDto emailDto = EmailDto.builder()
                .subject(subject)
                .emailToList(Collections.singletonList(email))
                .body(body)
                .build();
        sendGridService.sendEmail(emailDto);

    }
}
