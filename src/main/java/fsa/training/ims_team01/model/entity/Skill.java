package fsa.training.ims_team01.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skill")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //Relationship Entity


    public Skill(String name) {
        this.name = name;
    }

    public Skill(Long id) {
        this.id = id;
    }

}
