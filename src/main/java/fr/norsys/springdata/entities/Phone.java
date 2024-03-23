package fr.norsys.springdata.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "Phone")
public class Phone {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number", unique = true)
    @NaturalId
    private String number;

    @ManyToOne

    private Person person;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id")
    private PhoneDetails phoneDetails;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        return Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
