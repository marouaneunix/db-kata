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
        /**
         * Whenever a bidirectional association is formed,
         * the application developer must make sure both sides are in-sync at all times.
         */
        Person person = new Person();
        person.setName("John Doe");

        Phone phone = new Phone();
        phone.setNumber("1234567890");
        phone.setPerson(person);

        Phone phone2 = new Phone();
        phone2.setNumber("0987654321");
        phone2.setPerson(person);


        person.getPhones().add(phone);
        person.getPhones().add(phone2);

        // or you can use the addPhone method in the Person class
        // person.addPhone(phone);
        // person.addPhone(phone2);

        personRepository.save(person);

        personRepository.findAll().forEach(p -> {
            System.out.println(p.getName());

        });
//        phoneRepository.findAll().forEach(System.out::println);



    }
}

