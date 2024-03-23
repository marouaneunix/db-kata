package fr.norsys.springdata.entities;


import jakarta.persistence.*;

@Entity(name = "Phone")
public class Phone {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number")
    private String number;


    public Long getId() {
        return id;
    }

    public Phone setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Phone setNumber(String number) {
        this.number = number;
        return this;
    }


    @Override
    public String toString() {
        return "Phone{" +
            "id=" + id +
            ", number='" + number +
            '}';
    }
}
