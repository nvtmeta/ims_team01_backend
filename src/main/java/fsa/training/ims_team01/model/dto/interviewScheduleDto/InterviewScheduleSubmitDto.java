package fsa.training.ims_team01.model.dto.interviewScheduleDto;

import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.enums.interviewEnum.ResultInterviewEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class InterviewScheduleSubmitDto {
    private String note;

    private ResultInterviewEnum result;
}
