package fsa.training.ims_team01.service;

import fsa.training.ims_team01.model.dto.email.EmailDto;

public interface SendGridService {

    void sendEmail(EmailDto emailDto);

}
