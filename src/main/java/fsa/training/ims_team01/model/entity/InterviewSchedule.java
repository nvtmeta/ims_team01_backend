package fsa.training.ims_team01.model.entity;


import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.enums.interviewEnum.ResultInterviewEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "interview_schedule")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public InterviewSchedule(Long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "location")
    private String location;

    @Column(name = "meetingId")
    private String meetingId;

    @Column(name = "note", columnDefinition = "VARCHAR(500)")
    private String note;

    @Column(name = "result")
    private ResultInterviewEnum result;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InterviewScheduleEnum status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    // interviewer
    @ManyToMany
    @JoinTable(
            name = "interview_schedule_interviewer",
            joinColumns = @JoinColumn(name = "interview_schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "interviewer_id")
    )
    private List<User> interviewers;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToMany(mappedBy = "interviewSchedule")
    private List<Offer> offers;

    @Column(name = "is_deleted", columnDefinition = "bit default 0")
    private Boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
