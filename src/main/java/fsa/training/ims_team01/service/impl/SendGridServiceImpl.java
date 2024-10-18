package fsa.training.ims_team01.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import fsa.training.ims_team01.model.dto.email.EmailDto;
import fsa.training.ims_team01.service.SendGridService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;


@Service
public class SendGridServiceImpl implements SendGridService {
    private final String SENDER_NAME;
    private final String SENDER_EMAIL;
    private final String API_KEY;

    public SendGridServiceImpl(Environment env) {
        SENDER_NAME = env.getProperty("sendgrid.from-name");
        SENDER_EMAIL = env.getProperty("sendgrid.from-email");
        API_KEY = env.getProperty("sendgrid.api-key");
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        Objects.requireNonNull(emailDto.getEmailToList());

        // Setting sender information
        Email sender = new Email(SENDER_EMAIL, SENDER_NAME);

        // Setting email to, email cc, email bcc
        Personalization personalization = new Personalization();
        emailDto.getEmailToList().forEach(e -> personalization.addTo(new Email(e)));

        if (Objects.nonNull(emailDto.getEmailCcList())) {
            emailDto.getEmailCcList().forEach(e -> personalization.addCc(new Email(e)));
        }

        if (Objects.nonNull(emailDto.getEmailBccList())) {
            emailDto.getEmailBccList().forEach(e -> personalization.addBcc(new Email(e)));
        }

        // Setting content with default type HTML
        Content content = new Content("text/html", emailDto.getBody());

        Mail mail = new Mail();
        mail.setFrom(sender);
        mail.setSubject(emailDto.getSubject());
        mail.addPersonalization(personalization);
        mail.addContent(content);

        try {
            SendGrid sendGrid = new SendGrid(API_KEY);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            response.getStatusCode();
        } catch (IOException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }


}
