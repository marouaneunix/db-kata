package fr.norsys.springdata;

import fr.norsys.springdata.entities.Person;
import fr.norsys.springdata.entities.Phone;
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


        Phone phone = new Phone();
        phone.setNumber("1234567890");

        Phone phone2 = new Phone();
        phone2.setNumber("0987654321");

        Person person = new Person();
        person.setName("John Doe");
        person.getPhones().add(phone);
        person.getPhones().add(phone2);

        this.personRepository.save(person);

        // delete phone
        person.getPhones().remove(phone);
        this.personRepository.save(person);

        personRepository.findAll().forEach( p -> {
            System.out.println(p);
        });

        // you can't delete phone directly because it's still associated with the person
        // this.phoneRepository.delete(phone2);

    }
}

