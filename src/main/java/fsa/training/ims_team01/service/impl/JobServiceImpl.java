package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.jobDto.JobDetailDto;
import fsa.training.ims_team01.model.dto.jobDto.JobFormDto;
import fsa.training.ims_team01.model.dto.jobDto.JobListDto;
import fsa.training.ims_team01.model.dto.jobDto.JobScheduleDto;
import fsa.training.ims_team01.model.entity.Benefit;
import fsa.training.ims_team01.model.entity.Job;
import fsa.training.ims_team01.model.entity.Level;
import fsa.training.ims_team01.model.entity.Skill;
import fsa.training.ims_team01.repository.BenefitRepository;
import fsa.training.ims_team01.repository.JobRepository;
import fsa.training.ims_team01.repository.LevelRepository;
import fsa.training.ims_team01.repository.SkillRepository;
import fsa.training.ims_team01.service.JobService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final BenefitRepository benefitRepository;
    private final LevelRepository levelRepository;


    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        cb = entityManager.getCriteriaBuilder();
    }

    public JobServiceImpl(JobRepository jobRepository, SkillRepository skillRepository, BenefitRepository benefitRepository, LevelRepository levelRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.benefitRepository = benefitRepository;
        this.levelRepository = levelRepository;
    }

    @Override
    public Page<JobListDto> getJobList(Pageable pageable, String q, JobStatusEnum statusEnum) {
        return jobRepository.findAllByDeletedFalse(pageable, q, statusEnum);
    }

    @Override
    public Page<DropdownDto> getJobListByStatus(Pageable pageable, JobStatusEnum status, String q) {
        return jobRepository.getJobListByStatus(pageable, status, q);
    }

    @Override
    public Optional<JobDetailDto> getJobDetailById(Long id) {
        Optional<JobDetailDto> jobDetailDto = jobRepository.findByIdAndDeletedFalse(id);
        if (jobDetailDto.isPresent()) {
            List<DropdownDto> skills = getSkillsByJobId(id);
            jobDetailDto.get().setSkills(skills);

            List<DropdownDto> benefits = getBenefitsByJobId(id);
            jobDetailDto.get().setBenefits(benefits);

            List<DropdownDto> levels = getLevelsByJobId(id);
            jobDetailDto.get().setLevels(levels);

            return jobDetailDto;
        }
        throw new NotFoundException("JobDetailDto not found");
    }

    public List<DropdownDto> getSkillsByJobId(Long id) {
        return getJobDetailsByJobId(id, "skills");
    }

    public List<DropdownDto> getBenefitsByJobId(Long id) {
        return getJobDetailsByJobId(id, "benefits");
    }

    public List<DropdownDto> getLevelsByJobId(Long id) {
        return getJobDetailsByJobId(id, "levels");
    }

    private List<DropdownDto> getJobDetailsByJobId(Long id, String joinProperty) {
        CriteriaQuery<DropdownDto> query = cb.createQuery(DropdownDto.class);

        Root<Job> job = query.from(Job.class);
        Join<Job, ?> jobs = job.join(joinProperty);

        query.where(
                cb.equal(job.get("id"), id),
                cb.isFalse(job.get("deleted"))
        );

        query.select(cb.construct(DropdownDto.class,
                jobs.get("id"),
                jobs.get("name")
        ));
//        query.distinct(true); // Add this line to retrieve distinct results

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Job> getJobById(Long jobId) {
        return jobRepository.findJobByIdAndDeletedFalse(jobId);
    }

    //todo: BRL-12-03,  BRL-12-04

    @Override
    public Job create(JobFormDto jobFormDto) {
        Job job = new Job();
        jobProperties(jobFormDto, job);
//        job.setDeleted(false);
        return jobRepository.save(job);
    }

    private Job createWithoutProperties(JobFormDto jobFormDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobFormDto, job);
        job.setStatus(JobStatusEnum.OPEN);
        saveJob(job);
        return job;
    }

    private void saveJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public String create(List<JobFormDto> jobFormDto) {
        List<Job> jobs = jobFormDto
                .stream()
                .map(this::createWithoutProperties)
                .toList();
        return "success created job";
    }

    //todo: fix update offer

    @Override
    public Job update(Long id, JobFormDto jobFormDto) {
        Optional<Job> jobOptional = getJobById(id);
        if (jobOptional.isEmpty()) {
            throw new RuntimeException("Job not found");
        }

        jobProperties(jobFormDto, jobOptional.get());


        return jobRepository.save(jobOptional.get());
//        return "success updated job";
    }

    @Override
    public void deleteJobById(Long jobId) {
        Job job = getJobById(jobId).orElseThrow();
        job.setDeleted(true);
        jobRepository.save(job);
    }

    @Override
    public List<JobScheduleDto> findByStartDateLessThanEqual(LocalDate date) {
        return jobRepository.findByStartDateLessThanEqual(date);
    }

    @Override
    public List<JobScheduleDto> findByEndDateLessThanEqual(LocalDate date) {
         return jobRepository.findByEndDateLessThanEqual(date);
    }

    @Override
    public void updateJobStatus(Long id, JobStatusEnum status) {
        Job job = getJobById(id).orElseThrow();
        job.setStatus(status);
        jobRepository.save(job);
    }

    private void jobProperties(JobFormDto jobFormDto, Job job) {
        BeanUtils.copyProperties(jobFormDto, job);
        List<Skill> skills = jobFormDto.getSkills().stream()
                .map(skill -> skillRepository.findById(skill.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<Benefit> benefits = jobFormDto.getBenefits().stream()
                .map(skill -> benefitRepository.findById(skill.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<Level> levels = jobFormDto.getLevels().stream()
                .map(item -> levelRepository.findById(item.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        job.setSkills(skills);
        job.setBenefits(benefits);
        job.setLevels(levels);

    }

}
