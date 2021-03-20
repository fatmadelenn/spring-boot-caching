package com.fatmadelenn.caching.service;

import com.fatmadelenn.caching.modal.Person;
import com.fatmadelenn.caching.repository.PersonRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    @Cacheable("persons")
    public List<Person> getAllPersons() {
        try {
            logger.info("Going to sleep for 2 Secs.. to simulate backend call.");
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return personRepository.findAll();
    }

    @CachePut(value = "persons", key = "#person.name")
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Cacheable(value = "persons", key = "#name", condition = "#name.length() > 4", unless = "#result.age < 24")
    public Person getPersonByName(String name) throws NotFoundException {
        try {
            logger.info("Going to sleep for 2 Secs.. to simulate backend call.");
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return personRepository.findByName(name).orElseThrow(() -> new NotFoundException("Person Not Found"));
    }

    @CachePut(value = "persons", key = "#person.name")
    public Person updatePersonById(Long id, Person person) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        optionalPerson.ifPresent(value -> person.setId(id));
        return personRepository.save(person);
    }

    @CacheEvict(value = "persons", allEntries = true)
    public String deleteAllPersons() {
        personRepository.deleteAll();
        return "Deleted all persons....";
    }

    @CacheEvict(value = "persons", key = "#name")
    public void deletePerson(String name) {
        Optional<Person> optionalPerson = personRepository.findByName(name);
        optionalPerson.ifPresent(value -> personRepository.deleteById(value.getId()));
    }

    @CacheEvict(value = "persons", allEntries = true)
    public String clearCache() {
        return "Cleared Cache.";
    }
}
