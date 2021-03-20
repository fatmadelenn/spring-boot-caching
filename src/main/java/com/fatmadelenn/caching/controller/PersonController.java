package com.fatmadelenn.caching.controller;

import com.fatmadelenn.caching.modal.Person;
import com.fatmadelenn.caching.service.PersonService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonService personService;

    @GetMapping("/all")
    public List<Person> getAllPersons() {
        logger.info("Searching all persons....");
        return personService.getAllPersons();
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        logger.info("Creating person: {}", person);
        return personService.createPerson(person);
    }


    @GetMapping("/{name}")
    public Person findPersonById(@PathVariable String name) throws NotFoundException {
        logger.info("Searching by name  :  {}" ,name);
        return personService.getPersonByName(name);
    }

    @PutMapping("/update/{id}")
    public Person updatePersonById(@PathVariable Long id, @RequestBody Person person) {
        logger.info("Updating by ID  : {}", id);
        return personService.updatePersonById(id, person);
    }

    @DeleteMapping("/delete/all")
    public String deleteAllPersons() {
        return personService.deleteAllPersons();
    }

    @DeleteMapping("/delete/{name}")
    public void deletePerson(@PathVariable String name) {
        logger.info("Deleting by name  : {}" + name);
        personService.deletePerson(name);
    }

    @DeleteMapping("/cacheClear")
    public String clearCache() {
        return personService.clearCache();
    }
}
