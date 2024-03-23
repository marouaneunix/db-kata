package fr.norsys.springdata.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // FtechType.EAGER is used to load the phones when the person is loaded
    // orphanRemoval = true is used to delete the phones when the person is deleted
    // If you want to lazy load the phones, you can use FetchType.LAZY + @Transactional in the main class
        // it resolves B by calling a.getB().getName() while you are still in the @Transaction.
        // Hibernate can now make a second request to the database to fetch B,
        // and now a.getB() is really of type B and stays that way, so you can use it outside the persistence context.
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "person"
    )
    private List<Phone> phones = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public Person setPhones(List<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setPerson(this);
    }

    public void removePhone(Phone phone) {
        phones.remove(phone);
        phone.setPerson(null);
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", phones=" + phones +
            '}';
    }
}
