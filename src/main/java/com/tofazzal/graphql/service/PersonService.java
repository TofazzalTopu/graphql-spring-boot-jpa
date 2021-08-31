package com.tofazzal.graphql.service;

import com.tofazzal.graphql.entity.Person;
import com.tofazzal.graphql.repository.PersonRepository;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public void save(List<Person> persons) {
        personRepository.saveAll(persons);
    }

    public DataFetcher<Person> createPerson() {
        return dataFetchingEnvironment -> {
            String name = dataFetchingEnvironment.getArgument("name");
            String mobile = dataFetchingEnvironment.getArgument("mobile");
            String email = dataFetchingEnvironment.getArgument("email");
            String address[] = dataFetchingEnvironment.getArgument("address");

            Person person = new Person();
            person.setName(name);
            person.setMobile(mobile);
            person.setEmail(email);
            person.setAddress(address);
            return personRepository.save(person);
        };
    }
}
