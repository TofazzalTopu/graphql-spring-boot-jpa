package com.tofazzal.graphql.controller;

import com.tofazzal.graphql.service.PersonService;
import com.tofazzal.graphql.entity.Person;
import com.tofazzal.graphql.mutation.SchemaLoadingService;
import com.tofazzal.graphql.repository.PersonRepository;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/persons")
public class PersonController {

    @Autowired
    private PersonRepository repository;
    private final PersonService personService;

    @Autowired
    private SchemaLoadingService schemaLoadingService;

    @Value("classpath:person.graphqls")
    private Resource schemaResource;

    private GraphQL graphQL;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostConstruct
    public void loadSchema() throws IOException {
        graphQL = schemaLoadingService.loadSchema();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPerson(@RequestBody List<Person> persons) {
        personService.save(persons);
        return ResponseEntity.ok().body("Person created successfully.");
    }

    @GetMapping("/all")
    public List<Person> getPersons() {
        return repository.findAll();
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestBody String query) {
        log.info("Fetching list of persons using graphql.");
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<Object> findByEmail(@RequestBody String query) {
        log.info("Fetching a person by email using graphql.");
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createPerson(@RequestBody String mutation) {
        log.info("Create a person using graphql.");
        ExecutionResult result = graphQL.execute(mutation);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
