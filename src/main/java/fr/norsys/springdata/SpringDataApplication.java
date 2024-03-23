package fr.norsys.springdata;

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

        PhoneDetails phoneDetails = new PhoneDetails();
        phoneDetails.setProvider("provider");
        phoneDetails.setTechnology("technology");
        phoneDetails.setFrequency("frequency");

        Phone phone = new Phone();
        phone.setNumber("1234567890");
        phone.addDetails(phoneDetails);


        phoneRepository.save(phone);

        System.out.println("***** Phone After ADD ******");

        phoneRepository.findAll().forEach(System.out::println);

        phone.removeDetails();

        System.out.println("***** Phone After Delete *****");
        phoneRepository.findAll().forEach(System.out::println);

    }
}

