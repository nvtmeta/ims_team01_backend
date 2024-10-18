package fsa.training.ims_team01.model.dto.jobDto;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class JobListDto {

    private Long id;

    private String title;

    private Object skills;

    private Object levels;

//    private Object benefits;

    private LocalDate startDate;

    private LocalDate endDate;

    private JobStatusEnum status;

    public JobListDto(Long id, String title, Object skills, Object levels, LocalDate startDate, LocalDate endDate, JobStatusEnum status) {
        this.id = id;
        this.title = title;
        this.skills = skills;
        this.levels = levels;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}
