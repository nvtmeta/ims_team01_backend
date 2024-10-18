package fsa.training.ims_team01.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "benefit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Benefit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Benefit(String name) {
        this.name = name;
    }

    public Benefit(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    private String name;

}
