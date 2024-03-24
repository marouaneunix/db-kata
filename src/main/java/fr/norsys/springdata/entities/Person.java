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
@ToString
@EqualsAndHashCode(of = "name")
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "person"
    )
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        mappedBy = "person"
    )
    private List<PersonAddress> personAddresses = new ArrayList<>();

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setPerson(this);
    }

    public void removePhone(Phone phone) {
        phones.remove(phone);
        phone.setPerson(null);
    }

    public void addAddress(Address address) {
        PersonAddress personAddress = new PersonAddress(null, this, address);
        this.personAddresses.add(personAddress);
        address.getPersonAddresses().add(personAddress);
    }

    public void deleteAddress(Address address) {
        PersonAddress personAddress = new PersonAddress(null, this, address);
        address.getPersonAddresses().remove(personAddress);
        this.personAddresses.remove(personAddress);
        personAddress.setAddress(null);
        personAddress.setPerson(null);

    }


}
