package fr.norsys.springdata.repository;

import fr.norsys.springdata.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
