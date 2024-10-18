package fsa.training.ims_team01.model.entity;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Job(Long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "salary_from")
    private BigDecimal salaryFrom;

    @Column(name = "salary_to")
    private BigDecimal salaryTo;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "working_address")
    private String workingAddress;

    //todo:maxlenght 500
    @Column(name = "description", columnDefinition = "VARCHAR(500)")
    private String description;

    @Enumerated(EnumType.STRING)
    private JobStatusEnum status;

    @Column(name = "is_deleted", nullable = false , columnDefinition = "bit default 0")
    private Boolean deleted = false;

    //Relationship Entity
    @OneToMany(mappedBy = "job")
    private List<InterviewSchedule> schedules;

    @OneToMany(mappedBy = "job")
    private List<Offer> offers;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "skill_job",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;


    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "level_job",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "level_id"))
    private List<Level> levels;

    @OneToMany(mappedBy = "job")
    private List<JobApplication> jobApplications;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "benefit_job",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "benefit_id"))
    private List<Benefit> benefits;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;


//    public void setLevels(List<Level> levels) {
//        this.levels = levels;
//    }
//
//    public void setSkills(List<Skill> skills) {
//        if (this.skills == null) {
//            this.skills = new ArrayList<>();
//        } else {
//            this.skills.clear();
//        }
//        this.skills.addAll(skills);
//    }
//
//    public void setBenefits(List<Benefit> benefits) {
//        this.benefits = benefits;
//    }
}
