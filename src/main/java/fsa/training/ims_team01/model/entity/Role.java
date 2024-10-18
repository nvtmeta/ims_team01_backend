package fsa.training.ims_team01.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Column(name = "name")
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleUser> roleUsers;

}
