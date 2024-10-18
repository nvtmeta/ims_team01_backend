package fsa.training.ims_team01.schedule;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.model.dto.jobDto.JobScheduleDto;
import fsa.training.ims_team01.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JobSchedule {
    private final JobService jobService;
//    @Scheduled(initialDelay = 60000, fixedDelay = Long.MAX_VALUE)

    @Scheduled(cron = "0 0 0 * * *") // runs at 10:00 AM every day
//    @Scheduled(initialDelay = 60000, fixedDelay = Long.MAX_VALUE)
    public void updateStatus() {
        // query the database for jobs with startDate today or earlier
        List<JobScheduleDto> jobScheduleDtoList = jobService.findByStartDateLessThanEqual(LocalDate.now());

        // update the status of each job to "open"
        jobScheduleDtoList.stream()
                .filter(jobScheduleDto -> Objects.equals(jobScheduleDto.getStatus(), JobStatusEnum.DRAFT))
                .forEach(jobScheduleDto -> jobService.updateJobStatus(jobScheduleDto.getId(), JobStatusEnum.OPEN));


        // query the database for jobs with endDate today or earlier
        jobScheduleDtoList = jobService.findByEndDateLessThanEqual(LocalDate.now());

        // update the status of each job to "closed"
        jobScheduleDtoList.stream()
                .filter(jobScheduleDto -> Objects.equals(jobScheduleDto.getStatus(), JobStatusEnum.OPEN))
                .forEach(jobScheduleDto -> jobService.updateJobStatus(jobScheduleDto.getId(), JobStatusEnum.CLOSE));

    }
}
