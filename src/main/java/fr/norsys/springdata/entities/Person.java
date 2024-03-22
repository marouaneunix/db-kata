package fr.norsys.springdata.entities;


import jakarta.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
