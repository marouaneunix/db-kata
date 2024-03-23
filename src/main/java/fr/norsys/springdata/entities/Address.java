package fr.norsys.springdata.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "Address")
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String street;

    @Column(name = "number")
    private String number;


}
