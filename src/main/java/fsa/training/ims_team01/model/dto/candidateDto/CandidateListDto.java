package fsa.training.ims_team01.model.dto.candidateDto;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CandidateListDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private CandidateStatusEnum status;
    private String positionName;
    private String recruiterName;
    private LocalDateTime createdDate;


    public CandidateListDto(Long id, String fullName, String email, String phone, CandidateStatusEnum status,
                            String positionName, String recruiterName) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.positionName = positionName;
        this.recruiterName = recruiterName;
    }



}
