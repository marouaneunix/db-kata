package fr.norsys.springdata.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "persons")
@EqualsAndHashCode
@Entity(name = "Address")
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String street;

    @Column(name = "number")
    private String number;

    @ManyToMany(mappedBy = "addresses")
    private List<Person> persons = new ArrayList<>();


}
