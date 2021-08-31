# spring-boot-graphQL
GraphQL is a relatively new concept from Facebook that is billed as an alternative to REST for Web APIs


#Query to insert new person
mutation {
createPerson(name: "tina Moni", mobile: "34534", email: "moni@sdf.asd")
    {
        id,
        name,
        email
    }
}

# Query to getAllPerson
query {
    getAllPerson
        {
            id,
            name,
            email
        }
}