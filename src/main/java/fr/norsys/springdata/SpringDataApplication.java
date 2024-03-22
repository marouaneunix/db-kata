package fr.norsys.springdata;

import fr.norsys.springdata.entities.Person;
import fr.norsys.springdata.entities.Phone;
import fr.norsys.springdata.repository.PersonRepository;
import fr.norsys.springdata.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
        person.setName("John Doe");
        this.personRepository.save(person);

        Phone phone = new Phone();
        phone.setNumber("1234567890");
        phone.setPerson(person);
        this.phoneRepository.save(phone);


        phoneRepository.findAll().forEach(System.out::println);
    }
}
