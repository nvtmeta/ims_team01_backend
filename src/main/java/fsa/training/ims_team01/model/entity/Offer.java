package fsa.training.ims_team01.model.entity;

import fsa.training.ims_team01.enums.commonEnum.ContractTypeEnum;
import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_from_date", nullable = false)
    private LocalDate contractFromDate;

    @Column(name = "contract_to_date", nullable = false)
    private LocalDate contractToDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "note", columnDefinition = "VARCHAR(500)")
    private String note;

    @Column(name = "basic_salary", nullable = false)
    private BigDecimal basicSalary;

    @Enumerated(EnumType.STRING)
    private OfferStatusEnum status;

    @Enumerated(EnumType.STRING)
    private ContractTypeEnum contractType;

    //Relationship Entity
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @ManyToOne
    @JoinColumn(name = "recruiter_owner_id")
    private User recruiterOwner;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "interview_info_id")
    private InterviewSchedule interviewSchedule;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @Column(name = "is_deleted", nullable = false , columnDefinition = "bit default 0")
    private Boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
