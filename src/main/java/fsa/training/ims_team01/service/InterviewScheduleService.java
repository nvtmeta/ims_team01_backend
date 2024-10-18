package fsa.training.ims_team01.service;

import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleDetailDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleFormDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleListDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleSubmitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InterviewScheduleService {
    Page<InterviewScheduleListDto> getInterviewScheduleList(Pageable pageable, String q,
                                                            InterviewScheduleEnum status, Long interviewerId);

    String createInterviewSchedule(InterviewScheduleFormDto interviewScheduleFormDto);

    String updateInterviewSchedule(Long interviewScheduleId, InterviewScheduleFormDto interviewScheduleFormDto);

    Optional<InterviewScheduleDetailDto> getInterviewScheduleById(Long id);

    String cancelInterviewSchedule(Long interviewScheduleId);

    String submitResult(Long interviewScheduleId, InterviewScheduleSubmitDto interviewScheduleSubmitDto);

    Page<DropdownDto> getAllInterviewers(Pageable pageable);

    Optional<String> getNoteById(Long id);

    boolean isAllowedToSubmitResult(Long interviewScheduleId, Long interviewerId);

    List<EmailSendInterviewSchedule> findByScheduleDate(LocalDate scheduleDate);

    void sendReminder(Long interviewScheduleId);

    void updateInterviewStatus(Long id, InterviewScheduleEnum status);

}
