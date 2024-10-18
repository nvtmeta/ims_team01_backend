package fsa.training.ims_team01.model.dto.offerDto;

import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OfferListDto {

    private Long id;

    private String candidateName;

    private String email;

    private String approver;

    private String department;

    private String note;

    private OfferStatusEnum status;


    public OfferListDto(Long id, String candidateName, String email, String approver, String department, String note, OfferStatusEnum status) {
        this.id = id;
        this.candidateName = candidateName;
        this.email = email;
        this.approver = approver;
        this.department = department;
        this.note = note;
        this.status = status;
    }
}
