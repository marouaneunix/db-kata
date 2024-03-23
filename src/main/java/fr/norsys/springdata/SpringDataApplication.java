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

        person.getAddresses().add(address);

        person1.getAddresses().add(address);
        person1.getAddresses().add(address1);

        personRepository.save(person);
        personRepository.save(person1);

        personRepository.findAll().forEach(System.out::println);


        person.getAddresses().remove(address);

        personRepository.delete(person1);
    }
}

