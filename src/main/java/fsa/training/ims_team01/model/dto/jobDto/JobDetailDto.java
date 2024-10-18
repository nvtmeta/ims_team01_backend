package fsa.training.ims_team01.model.dto.jobDto;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JobDetailDto {
    private Long id;

    private String title;

    private BigDecimal salaryFrom;

    private BigDecimal salaryTo;

    private LocalDate startDate;

    private LocalDate endDate;

    private String workingAddress;

    private List<DropdownDto> skills;

    private List<DropdownDto> benefits;

    private List<DropdownDto> levels;

    private String description;

    private JobStatusEnum status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    public JobDetailDto(Long id, String title, BigDecimal salaryFrom, BigDecimal salaryTo, LocalDate startDate, LocalDate endDate,
                        String workingAddress, String description, JobStatusEnum status , LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.title = title;
        this.salaryFrom = salaryFrom;
        this.salaryTo = salaryTo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.workingAddress = workingAddress;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
