package fsa.training.ims_team01.resource;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.jobDto.JobDetailDto;
import fsa.training.ims_team01.model.dto.jobDto.JobFormDto;
import fsa.training.ims_team01.model.dto.jobDto.JobListDto;
import fsa.training.ims_team01.model.entity.Job;
import fsa.training.ims_team01.service.JobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/api/job")
public class JobResource {
    private final JobService jobService;

    public JobResource(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public Page<JobListDto> getJobList(@PageableDefault Pageable pageable,
                                       @RequestParam(name = "status", required = false) JobStatusEnum status,
                                       @RequestParam(name = "q", required = false) String q) {
        return jobService.getJobList(pageable, q, status);
    }

    @GetMapping("/status/{status}")
    public Page<DropdownDto> getJobListByStatus(@PageableDefault Pageable pageable,
                                                @PathVariable JobStatusEnum status,
                                                @RequestParam(name = "q", required = false) String q) {
        return jobService.getJobListByStatus(pageable, status,q);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Optional<JobDetailDto> jobDetailOptional = jobService.getJobDetailById(id);
        if (jobDetailOptional.isEmpty()) {
            throw new NotFoundException("Cannot find this job");
        }
        JobDetailDto jobDetailDto = jobDetailOptional.get();
        return new ResponseEntity<>(jobDetailDto, HttpStatus.OK);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<?> createJob(@RequestBody @Valid JobFormDto jobFormDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                System.out.println("Binding result has errors: " + bindingResult.getAllErrors());
                throw new NotFoundException(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            checkForm(jobFormDto);
            Job savedJob = jobService.create(jobFormDto);
            return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<?> createJobExcel(@RequestBody @Valid List<JobFormDto> jobFormDtos, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                System.out.println("Binding result has errors: " + bindingResult.getAllErrors());
                throw new NotFoundException(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            checkForm(jobFormDtos);
            String savedJob = jobService.create(jobFormDtos);
            return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update function
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<?> updateJob(@PathVariable("id") Long id, @RequestBody @Valid JobFormDto jobFormDto, BindingResult bindingResult) throws Exception {
        try {
            if (bindingResult.hasErrors()) {
                System.out.println("Binding result has errors: " + bindingResult.getAllErrors());
                throw new NotFoundException(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            checkForm(jobFormDto);
            Job updatedJob = jobService.update(id, jobFormDto);
            return new ResponseEntity<>(updatedJob, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update job: " + e.getMessage());
            return new ResponseEntity<>("Failed to update job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        checkForm(jobFormDto);
//        Job updatedJob = jobService.update(id, jobFormDto);
//        return new ResponseEntity<>(updatedJob, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id) {
        jobService.deleteJobById(id);
        return ResponseEntity.noContent().build();
    }

    private void checkForm(JobFormDto jobFormDto) throws Exception {
        if (jobFormDto.getStartDate().isAfter(jobFormDto.getEndDate())) {
            throw new Exception("Start date must be before end date");
        }
        if (jobFormDto.getSalaryFrom() != null
                && jobFormDto.getSalaryTo() != null
                && jobFormDto.getSalaryFrom().compareTo(jobFormDto.getSalaryTo()) > 0
        ) {
            throw new Exception("Salary from must be smaller than salary to");
        }
    }


    private void checkForm(List<JobFormDto> jobFormDtos) throws Exception {
        jobFormDtos.stream()
                .filter(jobFormDto -> jobFormDto.getStartDate().isAfter(jobFormDto.getEndDate()))
                .findAny()
                .ifPresent(jobFormDto -> {
                    try {
                        throw new Exception("Start date must be before end date");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        jobFormDtos.stream()
                .filter(jobFormDto -> jobFormDto.getSalaryFrom() != null
                        && jobFormDto.getSalaryTo() != null
                        && jobFormDto.getSalaryFrom().compareTo(jobFormDto.getSalaryTo()) > 0)
                .findAny()
                .ifPresent(jobFormDto -> {
                    try {
                        throw new Exception("Salary from must be smaller than salary to");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
