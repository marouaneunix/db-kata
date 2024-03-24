package fr.norsys.springdata;

import fr.norsys.springdata.entities.Address;
import fr.norsys.springdata.entities.Person;
import fr.norsys.springdata.entities.Phone;
import fr.norsys.springdata.entities.PhoneDetails;
import fr.norsys.springdata.repository.AddressRepository;
import fr.norsys.springdata.repository.PersonRepository;
import fr.norsys.springdata.repository.PhoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.objenesis.SpringObjenesis;

import java.util.List;

@SpringBootApplication
@Transactional
public class SpringDataApplication implements CommandLineRunner {


    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Person person = new Person();
        person.setName("p1");

        Person person1 = new Person();
        person.setName("p2");

        Address address = new Address();
        address.setStreet("street1");
        address.setNumber("1");

        Address address1 = new Address();
        address1.setStreet("street2");
        address1.setNumber("2");

        // TODO: save the address
        addressRepository.saveAll(List.of(address, address1));
        // TODO: save the persons
        personRepository.saveAll(List.of(person, person1));
        // TODO: add the address to the persons and see the magic
        person.addAddress(address);
        person.addAddress(address1);
        person1.addAddress(address1);
        /**
         * Both the Person and the Address have a mappedBy @OneToMany side,
         * while the PersonAddress owns the person and the address @ManyToOne associations.
         * Because this mapping is formed out of two bidirectional associations,
         * the helper methods are even more relevant.
         */
    }
}

