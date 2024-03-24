package fr.norsys.springdata.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
public class PersonAddress {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Person person;
    @ManyToOne
    private Address address;
}
