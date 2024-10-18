package fsa.training.ims_team01.model.dto.interviewScheduleDto;

import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// todo: check interviewer only submit result

@Getter
@Setter
@NoArgsConstructor
public class InterviewScheduleFormDto {
    @NotNull(message = "{ME002.message}")
    private String title;

    @NotNull(message = "{ME002.message}")
    private LocalDate date;

    @NotNull(message = "{ME002.message}")
    private LocalTime startTime;

    @NotNull(message = "{ME002.message}")
    private LocalTime endTime;

    private String location;

    private String meetingId;

    private String note;

    private DropdownDto author;

    private String result;

    private InterviewScheduleEnum status = InterviewScheduleEnum.NEW;

    @NotNull(message = "{ME002.message}")
    private List<DropdownDto> interviewers;

    @NotNull(message = "{ME002.message}")
    private DropdownDto candidate;

    @NotNull(message = "{ME002.message}")
    private DropdownDto recruiter;

    private DropdownDto job;
}
