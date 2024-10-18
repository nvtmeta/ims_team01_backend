package fsa.training.ims_team01.service;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.jobDto.JobDetailDto;
import fsa.training.ims_team01.model.dto.jobDto.JobFormDto;
import fsa.training.ims_team01.model.dto.jobDto.JobListDto;
import fsa.training.ims_team01.model.dto.jobDto.JobScheduleDto;
import fsa.training.ims_team01.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobService {
    Page<JobListDto> getJobList(Pageable pageable, String q, JobStatusEnum statusEnum);

    Page<DropdownDto> getJobListByStatus(Pageable pageable, JobStatusEnum status,String q);

    Optional<JobDetailDto> getJobDetailById(Long jobId);
    Optional<Job> getJobById(Long jobId);

    Job create(JobFormDto jobFormDto);
    String create(List<JobFormDto> jobFormDto);

    Job update(Long id, JobFormDto jobFormDto);

    void deleteJobById(Long jobId);

    List<JobScheduleDto> findByStartDateLessThanEqual(LocalDate date);

    List<JobScheduleDto> findByEndDateLessThanEqual(LocalDate date);

    void updateJobStatus(Long id, JobStatusEnum status);

}
