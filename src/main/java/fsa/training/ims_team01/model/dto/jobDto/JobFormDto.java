package fsa.training.ims_team01.model.dto.jobDto;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobFormDto {

    @NotNull(message = "{ME002.message}")
    private String title;

    private BigDecimal salaryFrom;

    @NotNull(message = "{ME002.message}")
    private BigDecimal salaryTo;

    @NotNull(message = "{ME002.message}")
    private LocalDate startDate;

    @NotNull(message = "{ME002.message}")
    private LocalDate endDate;

    private String workingAddress;

    private List<DropdownDto> levels;

    private List<DropdownDto> skills;

    private List<DropdownDto> benefits;

    private String description;

    private JobStatusEnum status = JobStatusEnum.DRAFT;
}
