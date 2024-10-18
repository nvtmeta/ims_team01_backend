package fsa.training.ims_team01.model.dto.email;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class EmailSendOffer {
    private Long id;
    private String candidateName;
    private String candidatePosition;
    private LocalDate offerDueDate;
    private String recruiterOwner;
    private String recruiterEmail;
    private String managerEmail;

    public EmailSendOffer(Long id, String candidateName, String candidatePosition, LocalDate offerDueDate, String recruiterOwner, String managerEmail, String recruiterEmail) {
        this.id = id;
        this.candidateName = candidateName;
        this.candidatePosition = candidatePosition;
        this.offerDueDate = offerDueDate;
        this.recruiterOwner = recruiterOwner;
        this.managerEmail = managerEmail;
        this.recruiterEmail = recruiterEmail;
    }
}
