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

    @OneToOne(
        mappedBy = "phone",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
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

    public void addDetails(PhoneDetails phoneDetails) {
        this.phoneDetails = phoneDetails;
        this.phoneDetails.setPhone(this);
    }

    public void removeDetails() {
        if(this.phoneDetails == null) {
            return;
        }
        this.phoneDetails.setPhone(null);
        this.phoneDetails = null;

    }
}
