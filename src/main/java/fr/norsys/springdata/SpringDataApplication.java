package fr.norsys.springdata;

import fr.norsys.springdata.entities.Address;
import fr.norsys.springdata.entities.Person;
import fr.norsys.springdata.entities.Phone;
import fr.norsys.springdata.entities.PhoneDetails;
import fr.norsys.springdata.repository.PersonRepository;
import fr.norsys.springdata.repository.PhoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.objenesis.SpringObjenesis;

@SpringBootApplication
@Transactional
public class SpringDataApplication implements CommandLineRunner {


    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PersonRepository personRepository;

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

        person.addAddress(address);

        person1.addAddress(address);
        person1.addAddress(address1);

        personRepository.save(person);
        personRepository.save(person1);

        personRepository.findAll().forEach(System.out::println);


        person.removeAddress(address);
        personRepository.delete(person1);

        /**
         * If a bidirectional @OneToMany association performs better when removing or changing the order of child elements,
         * the @ManyToMany relationship cannot benefit from such an optimization because the foreign key side is not in control. To overcome this limitation, the link table must be directly exposed and the @ManyToMany association split into two bidirectional @OneToMany relationships.
         */
    }
}

