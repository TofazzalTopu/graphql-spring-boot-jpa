package com.tofazzal.graphql.mutation;

import com.tofazzal.graphql.entity.Person;
import com.tofazzal.graphql.service.PersonService;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SchemaLoadingService {

    private final PersonService personService;

    public SchemaLoadingService(PersonService personService) {
        this.personService = personService;
    }

    @Value("classpath:person.graphqls")
    private Resource schemaResource;

    private GraphQL graphQL;

    public GraphQL loadSchema() throws IOException {
        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
        return graphQL;
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<Person>> fetcher1 = data -> {
            return personService.findAll();
        };

        DataFetcher<Person> fetcher2 = data -> {
            return personService.findByEmail(data.getArgument("email"));
        };

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWriting -> typeWriting.dataFetcher("getAllPerson", fetcher1)
                        .dataFetcher("findPerson", fetcher2))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("createPerson", personService.createPerson()))
                .build();

        return runtimeWiring;
    }
}
