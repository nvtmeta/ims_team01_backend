package fsa.training.ims_team01.model.dto.email;


import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.enums.interviewEnum.ResultInterviewEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class EmailSendInterviewSchedule {
    private Long id;
    private String title;
    private LocalDate date;
    private String candidateName;
    private String candidatePosition;
    private String candidateEmail;
    private List<String> interviewerEmails;
    private LocalTime startTime;
    private LocalTime endTime;
    private String recruiterEmail;
    private String meetingId;

    public EmailSendInterviewSchedule(Long id, String title, LocalDate date, String candidateName, String candidatePosition,
                                      String candidateEmail, LocalTime startTime, LocalTime endTime, String recruiterEmail, String meetingId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.candidateName = candidateName;
        this.candidatePosition = candidatePosition;
        this.candidateEmail = candidateEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recruiterEmail = recruiterEmail;
        this.meetingId = meetingId;
    }


}
