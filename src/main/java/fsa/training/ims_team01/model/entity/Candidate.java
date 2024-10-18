package fsa.training.ims_team01.model.entity;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.enums.commonEnum.GenderEnum;
import fsa.training.ims_team01.enums.commonEnum.HighestLevelEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    public Candidate(Long id) {
        this.id = id;
    }

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "address")
    private String address;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "note", columnDefinition = "VARCHAR(500)")
    private String note;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CandidateStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "highest_level")
    private HighestLevelEnum highestLevel;

    @Column(name = "yoe")
    private Integer yoe;

    @Column(name = "cv_attachment")
    private String cvAttachment;

    @Column(name = "is_deleted", nullable = false , columnDefinition = "bit default 0")
    private Boolean deleted;

    //Relationship Entity
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "candidate")
    private List<InterviewSchedule> interviewSchedule;

    @OneToMany(mappedBy = "candidate")
    private List<Offer> offers;

//    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
//    private List<SkillCandidate> skillCandidates;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "skill_candidate",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @OneToMany(mappedBy = "candidate")
    private List<JobApplication> jobApplications;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
