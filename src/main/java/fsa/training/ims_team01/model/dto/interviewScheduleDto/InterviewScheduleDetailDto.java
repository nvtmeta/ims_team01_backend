package fsa.training.ims_team01.model.dto.interviewScheduleDto;

import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.enums.interviewEnum.ResultInterviewEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InterviewScheduleDetailDto {
    private String title;
    private DropdownDto job;
    private DropdownDto candidate;
    private List<DropdownDto> interviewers;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private DropdownDto recruiter;
    private DropdownDto author;
    private String meetingId;
    private String location;
    private ResultInterviewEnum result;
    private InterviewScheduleEnum status;
    private String note;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    public InterviewScheduleDetailDto(LocalDate date, String title, LocalTime startTime,
                                      LocalTime endTime, String meetingId, String location,
                                      ResultInterviewEnum result, String note, InterviewScheduleEnum status,
                                      LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.date = date;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingId = meetingId;
        this.location = location;
        this.result = result;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
