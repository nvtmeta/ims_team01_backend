package fsa.training.ims_team01.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "position")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Position(Long id) {
        this.id = id;
    }

    public Position(String name) {
        this.name = name;
    }
//Relationship Entity

    @OneToMany(mappedBy = "position")
    private List<Candidate> candidates;

    @OneToMany(mappedBy = "position")
    private List<Offer> offers;
}
