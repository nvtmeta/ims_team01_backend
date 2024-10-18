package fsa.training.ims_team01.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public RoleUser(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

}
