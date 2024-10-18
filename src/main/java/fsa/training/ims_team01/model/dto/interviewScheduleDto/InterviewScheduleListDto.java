package fsa.training.ims_team01.model.dto.interviewScheduleDto;


import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.enums.interviewEnum.ResultInterviewEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewScheduleListDto {
    private Long id;

    private String title;

    private String candidateName;

    private Object interviewerName;

    private Object interviewerId;

    private String schedule;

    private ResultInterviewEnum result;

    private InterviewScheduleEnum status;

    private String jobName;

    public InterviewScheduleListDto(Long id, String title, String candidateName, Object interviewerName, Object interviewerId, String schedule,
                                    ResultInterviewEnum result, InterviewScheduleEnum status, String jobName) {
        this.id = id;
        this.title = title;
        this.candidateName = candidateName;
        this.interviewerName = interviewerName;
        this.schedule = schedule;
        this.result = result;
        this.status = status;
        this.jobName = jobName;
        this.interviewerId = interviewerId;
    }

}
