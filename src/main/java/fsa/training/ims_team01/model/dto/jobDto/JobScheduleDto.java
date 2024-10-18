package fsa.training.ims_team01.model.dto.jobDto;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobScheduleDto {
    private Long id;
    private JobStatusEnum status;

    public JobScheduleDto(Long id, JobStatusEnum status) {
        this.id = id;
        this.status = status;
    }
}
