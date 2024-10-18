package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.enums.interviewEnum.ResultInterviewEnum;
import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.exception.ValidationException;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleDetailDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleFormDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleListDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleSubmitDto;
import fsa.training.ims_team01.model.entity.Candidate;
import fsa.training.ims_team01.model.entity.InterviewSchedule;
import fsa.training.ims_team01.model.entity.Job;
import fsa.training.ims_team01.model.entity.User;
import fsa.training.ims_team01.repository.InterviewScheduleRepository;
import fsa.training.ims_team01.service.CandidateService;
import fsa.training.ims_team01.service.EmailService;
import fsa.training.ims_team01.service.InterviewScheduleService;
import fsa.training.ims_team01.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    private final UserService userService;
    private final EmailService emailService;
    private final CandidateService candidateService;
    private final InterviewScheduleRepository interviewScheduleRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;


    @PostConstruct
    public void init() {
        cb = entityManager.getCriteriaBuilder();
    }
    @Override
    public Page<InterviewScheduleListDto> getInterviewScheduleList(Pageable pageable, String q,
                                                                   InterviewScheduleEnum status, Long interviewerId) {
        return interviewScheduleRepository.findAllByDeletedFalse(pageable, q, status, interviewerId);
    }
// TODO :do inviting email address
    private void mapInterviewScheduleFormDtoToEntity(InterviewScheduleFormDto dto, InterviewSchedule entity) {
        BeanUtils.copyProperties(dto, entity);

        Optional.ofNullable(dto.getCandidate())
                .map(candidateDto -> new Candidate(candidateDto.getValue()))
                .ifPresent(entity::setCandidate);

        Optional.ofNullable(dto.getRecruiter())
                .map(item -> new User(item.getValue()))
                .ifPresent(entity::setRecruiter);

        Optional.ofNullable(dto.getJob())
                .map(item -> new Job(item.getValue()))
                .ifPresent(entity::setJob);

        Optional.ofNullable(dto.getAuthor())
                .map(item -> new User(item.getValue()))
                .ifPresent(entity::setAuthor);

        List<User> interviewers = dto.getInterviewers().stream()
                .map(item -> new User(item.getValue()))
                .distinct()
                .collect(Collectors.toList());

        entity.setInterviewers(interviewers);
    }

    @Override
    public String createInterviewSchedule(InterviewScheduleFormDto interviewScheduleCreateDto) {
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        mapInterviewScheduleFormDtoToEntity(interviewScheduleCreateDto, interviewSchedule);
        candidateService.updateCandidateStatus(interviewSchedule.getCandidate().getId(), CandidateStatusEnum.WAITING_FOR_INTERVIEW);

        // Validate the interview schedule
        validateInterviewSchedule(interviewSchedule);

        return saveAndSendEmail(interviewSchedule);
    }

    private String saveAndSendEmail(InterviewSchedule interviewSchedule) {
        interviewScheduleRepository.save(interviewSchedule);
        sendEmailInterviewSchedule(interviewSchedule);
        return "Interview schedule created successfully";
    }

    private void sendEmailInterviewSchedule(InterviewSchedule interviewSchedule) {

        Optional<EmailSendDto> emailCandidate = candidateService.getEmailCandidate(interviewSchedule.getCandidate().getId());
        List<EmailSendDto> emailSendDtoInterviewerList = interviewSchedule.getInterviewers()
                .stream()
                .map(user -> userService.getEmailDtoUser(user.getId()))
                .map(optional -> optional.orElse(null)) // Use orElse on each Optional object
                .toList();

        emailCandidate.ifPresent(emailSendDto -> emailService.sendEmailInterviewSchedule(emailSendDto, emailSendDtoInterviewerList));

    }


    private void validateInterviewSchedule(InterviewSchedule interviewSchedule) {
        // Check if start time is before end time
        if (!interviewSchedule.getStartTime().isBefore(interviewSchedule.getEndTime())) {
            throw new ValidationException("Schedule From Time need to be earlier than Schedule To Time");
        }
    }

    @Override
    public String updateInterviewSchedule(Long interviewScheduleId, InterviewScheduleFormDto interviewScheduleFormDto) {
        boolean isInterviewSchedule = interviewScheduleRepository.existsByIdAndDeletedFalse(interviewScheduleId);
        System.out.println("isInterviewSchedule" + isInterviewSchedule);
        if (isInterviewSchedule) {
            InterviewSchedule updateInterviewSchedule = new InterviewSchedule(interviewScheduleId);
            mapInterviewScheduleFormDtoToEntity(interviewScheduleFormDto, updateInterviewSchedule);
            // Validate the interview schedule
            validateInterviewSchedule(updateInterviewSchedule);

            interviewScheduleRepository.save(updateInterviewSchedule);
            return "Interview schedule updated successfully";
        }

        throw new NotFoundException("interview schedule not found");
    }

    @Override
    public Optional<InterviewScheduleDetailDto> getInterviewScheduleById(Long id) {
        Optional<InterviewScheduleDetailDto> interviewSchedule = interviewScheduleRepository.getByIdAndDeletedFalse(id);
        interviewSchedule.ifPresent(userDetailDto1 -> {
            Optional<DropdownDto> recruiters = interviewScheduleRepository.getRecruiterByInterviewId(id);
            recruiters.ifPresent(userDetailDto1::setRecruiter);

            Optional<DropdownDto> jobs = interviewScheduleRepository.getJobByInterviewId(id);
            jobs.ifPresent(userDetailDto1::setJob);

            Optional<DropdownDto> candidate = interviewScheduleRepository.getCandidateByInterviewId(id);
            candidate.ifPresent(userDetailDto1::setCandidate);

            Optional<DropdownDto> author = interviewScheduleRepository.getAuthorByInterviewId(id);
            author.ifPresent(userDetailDto1::setAuthor);

            List<DropdownDto> interviewersList = getInterviewersByInterviewId(id);
            userDetailDto1.setInterviewers(interviewersList);
        });

        return interviewSchedule;
    }


    @Override
    public String cancelInterviewSchedule(Long interviewScheduleId) {
        Optional<InterviewSchedule> interview = interviewScheduleRepository.findById(interviewScheduleId);
        if (interview.isPresent()) {
            if (interview.get().getStatus() == InterviewScheduleEnum.NEW) {
                interview.get().setStatus(InterviewScheduleEnum.CANCELLED);

                // update candidate status
                candidateService.updateCandidateStatus(interview.get().getCandidate().getId(), CandidateStatusEnum.CANCELLED_INTERVIEW);
                // TODO:If the interview is cancelled, system will not sent Reminder to interviewer
                interviewScheduleRepository.save(interview.get());
                return "Interview Schedule cancelled successfully";
            }
        }
        //todo:5 Candidate’s status is updated to Cancelled Interview
        throw new NotFoundException("interview schedule not found");
    }

    @Override
    public String submitResult(Long interviewScheduleId, InterviewScheduleSubmitDto interviewScheduleSubmitDto) {
        Optional<InterviewSchedule> interview = interviewScheduleRepository.findById(interviewScheduleId);
        if (interview.isPresent()) {
            interview.get().setResult(interviewScheduleSubmitDto.getResult());
            interview.get().setNote(interviewScheduleSubmitDto.getNote());

            updateCandidateStatus(interview.get());
            // TODO: verify status
            interview.get().setStatus(InterviewScheduleEnum.INTERVIEWED);
            interviewScheduleRepository.save(interview.get());
            return "Interview result submitted successfully";
        }
        throw new NotFoundException("interview schedule not found");
    }

    private void updateCandidateStatus(InterviewSchedule interview) {
        if (interview.getResult() == ResultInterviewEnum.PASSED) {
            candidateService.updateCandidateStatus(interview.getCandidate().getId(), CandidateStatusEnum.PASSED_INTERVIEW);
        } else if (interview.getResult() == ResultInterviewEnum.FAILED) {
            candidateService.updateCandidateStatus(interview.getCandidate().getId(), CandidateStatusEnum.FAILED_INTERVIEW);
        }
    }

    @Override
    public Page<DropdownDto> getAllInterviewers(Pageable pageable) {
        CriteriaQuery<DropdownDto> query = cb.createQuery(DropdownDto.class);

        Root<InterviewSchedule> interviewSchedule = query.from(InterviewSchedule.class);
        Join<InterviewSchedule, User> interviewers = interviewSchedule.join("interviewers");

        query.where(cb.isFalse(interviewers.get("deleted"))); // add this line
        query.distinct(true); // add this line to remove duplicates

        query.select(cb.construct(DropdownDto.class,
                interviewers.get("id"),
                interviewers.get("fullName")
        ));

        TypedQuery<DropdownDto> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<DropdownDto> result = typedQuery.getResultList();

        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Optional<String> getNoteById(Long id) {
        return interviewScheduleRepository.getNoteByIdAndDeletedFalse(id);
    }

    @Override
    public boolean isAllowedToSubmitResult(Long id, Long interviewerId) {
        List<DropdownDto> result = getInterviewersByInterviewId(id);
        return result.stream().anyMatch(dto -> dto.getValue().equals(interviewerId));
    }

    @Override
    public List<EmailSendInterviewSchedule> findByScheduleDate(LocalDate scheduleDate) {
        List<EmailSendInterviewSchedule> emailSendInterviewSchedules = interviewScheduleRepository.findByScheduleDate(scheduleDate);
        System.out.println("emailSendInterviewSchedules = " + emailSendInterviewSchedules);
//        get list interviewer email
        emailSendInterviewSchedules.forEach(emailSendInterviewSchedule -> {
            List<String> interviewerEmails = getInterviewersEmailByInterviewId(emailSendInterviewSchedule.getId());
            emailSendInterviewSchedule.setInterviewerEmails(interviewerEmails);
        });
        System.out.println("emailSendInterviewSchedules = " + emailSendInterviewSchedules);

        // update interview schedule status t
        return emailSendInterviewSchedules;
    }

    @Override
    public void sendReminder(Long interviewScheduleId) {
        Optional<EmailSendInterviewSchedule> emailSendInterviewSchedule = interviewScheduleRepository.findByScheduleDateById(interviewScheduleId);
        if (emailSendInterviewSchedule.isPresent()) {
            List<String> interviewerEmails = getInterviewersEmailByInterviewId(emailSendInterviewSchedule.get().getId());
            emailSendInterviewSchedule.get().setInterviewerEmails(interviewerEmails);
            updateInterviewStatus(emailSendInterviewSchedule.get().getId(), InterviewScheduleEnum.INVITED);
            emailService.sendReminderScheduleEmail(emailSendInterviewSchedule.get());
        }
    }

    @Override
    public void updateInterviewStatus(Long id, InterviewScheduleEnum status) {
        Optional<InterviewSchedule> interview = interviewScheduleRepository.findByIdAndDeletedFalse(id);
        if (interview.isPresent()) {
            interview.get().setStatus(status);
            interviewScheduleRepository.save(interview.get());
        }
    }

    public List<DropdownDto> getInterviewersByInterviewId(Long id) {
        CriteriaQuery<DropdownDto> query = cb.createQuery(DropdownDto.class);

        Root<InterviewSchedule> interviewSchedule = query.from(InterviewSchedule.class);
        Join<InterviewSchedule, User> interviewers = interviewSchedule.join("interviewers");

        interviewers.on(cb.equal(interviewSchedule.get("id"), id));
        interviewers.on(cb.isFalse(interviewers.get("deleted"))); // add this line, user delete false

        query.select(cb.construct(DropdownDto.class,
                interviewers.get("id"),
                interviewers.get("fullName")
        ));

        return entityManager.createQuery(query).getResultList();
    }

    public List<String> getInterviewersEmailByInterviewId(Long id) {
        CriteriaQuery<String> query = cb.createQuery(String.class);

        Root<InterviewSchedule> interviewSchedule = query.from(InterviewSchedule.class);
        Join<InterviewSchedule, User> interviewers = interviewSchedule.join("interviewers");

        interviewers.on(cb.equal(interviewSchedule.get("id"), id));
        interviewers.on(cb.isFalse(interviewers.get("deleted"))); // add this line, user delete false

        query.select(cb.construct(String.class,
                interviewers.get("email")
        ));

        return entityManager.createQuery(query).getResultList();
    }


}
