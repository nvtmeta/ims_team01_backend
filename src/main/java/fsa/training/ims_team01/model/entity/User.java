package fsa.training.ims_team01.model.entity;

import fsa.training.ims_team01.enums.userEnum.GenderUserEnum;
import fsa.training.ims_team01.enums.userEnum.UserStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    public User(Long id) {
        this.id = id;
    }

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "email",  nullable = false)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "note", columnDefinition = "VARCHAR(500)")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderUserEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusEnum status = UserStatusEnum.ACTIVE;

    //Relationship Entity
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    //    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<RoleUser> roleUsers;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "recruiter")
    private List<Candidate> candidates;

    @OneToMany(mappedBy = "user")
    private List<ScheduleEmp> scheduleEmps;

    @OneToMany(mappedBy = "approver")
    private List<Offer> offers;

    @OneToMany(mappedBy = "recruiterOwner")
    private List<Offer> offer;

    @Column(name = "is_deleted", columnDefinition = "bit default 0")
    private Boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

}
