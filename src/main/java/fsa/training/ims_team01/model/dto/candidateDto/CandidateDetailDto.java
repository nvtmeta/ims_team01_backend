package fsa.training.ims_team01.model.dto.candidateDto;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.enums.commonEnum.GenderEnum;
import fsa.training.ims_team01.enums.commonEnum.HighestLevelEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CandidateDetailDto {
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private GenderEnum gender;

    private String address;

    private LocalDate dob;

    private String note;

    private CandidateStatusEnum status;

    private HighestLevelEnum highestLevel;

    private Integer yoe;

    private String cvAttachment;

    private DropdownDto position;

    private List<DropdownDto> skills;

    private DropdownDto recruiter;
    private DropdownDto author;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public CandidateDetailDto(Long id, String fullName, String email, String phone, GenderEnum gender, String address, LocalDate dob,
                              String note, CandidateStatusEnum status, HighestLevelEnum highestLevel,
                              Integer yoe, String cvAttachment, DropdownDto position, DropdownDto recruiter, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.note = note;
        this.status = status;
        this.highestLevel = highestLevel;
        this.yoe = yoe;
        this.cvAttachment = cvAttachment;
        this.position = position;
        this.recruiter = recruiter;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }


}
