package fsa.training.ims_team01.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schedule_emp")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEmp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relationship Entity

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "interviewSchedule_id")
    private InterviewSchedule interviewSchedule;
}
