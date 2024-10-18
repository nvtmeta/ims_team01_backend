package fsa.training.ims_team01.model.dto.candidateDto;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.enums.commonEnum.GenderEnum;
import fsa.training.ims_team01.enums.commonEnum.HighestLevelEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateFormDto {

    @NotNull(message = "{ME002.message}")
    private String fullName;

    @Email(message = "{ME009.message}")
    @NotNull(message = "{ME002.message}")
    private String email;

    private String phone;

    private GenderEnum gender;

    private String address;

    private LocalDate dob;

    private String note;

    private CandidateStatusEnum status = CandidateStatusEnum.OPEN;

    private HighestLevelEnum highestLevel;

    private DropdownDto position;

    private List<DropdownDto> skillCandidates;

    private Integer yoe;

    //    @NotNull(message = "{ME002.message}")
    private String cvAttachment;

    private DropdownDto recruiter;

    private DropdownDto author;

    private Boolean deleted = false;
}
