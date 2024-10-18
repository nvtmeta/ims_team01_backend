package fsa.training.ims_team01.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "level")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Level(Long id) {
        this.id = id;
    }

    public Level(String name) {
        this.name = name;
    }
}
