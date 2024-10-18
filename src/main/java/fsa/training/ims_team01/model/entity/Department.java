package fsa.training.ims_team01.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "department")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Department(String name) {
        this.name = name;
    }

    public Department(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(Long id) {
        this.id = id;
    }

    //Relationship Entity
    @OneToMany(mappedBy = "department")
    private List<User> users;

    @OneToMany(mappedBy = "department")
    private List<Offer> offers;
}
