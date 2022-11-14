# Recipes API

Welcome to Recipes API!

## Objective

This is a standalone SpringBoot application which allows users to manage their favourite recipes.

It allows adding, updating, removing and fetching recipes.

Additionally users are able to filter
available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.


## Getting Started
### Prerequisites
* Java SDK 19
* Docker
### Installing
From the project root folder command line, run the following command to initiate a local PostgreSql database in a Docker container:
```
(cd dev;docker-compose up -d)
```

Then run the following command to build the application and execute unit tests
```
./mvnw clean install
```

Run the application with profile 'local'. This will execute the application and database migration connecting to the local database:
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### API Documentation
Open API documentation can be found in the following URLs when the application is running:

* Documentation UI: [http://localhost:8080/](http://localhost:8080/)
* Open API specification: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

From there we can check the Open API specification and send request to the local environment

### Usage

Create a new recipe:
```
curl --location --request POST 'http://localhost:8080/recipe' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Pad Thai",
    "numberOfServings": 3,
    "category": "breakfast",
    "ingredients": [
        "palm sugar",
        "new ingredient 2",
        "fish"
    ],
    "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n3.Add fish sauce and tamarind and bring to a simmer.\\n4.Put it in the oven"
}'
```

Get recipe by id:
```
curl --location --request GET 'http://localhost:8080/recipe/1' \
--header 'Accept: application/json'
```

Get all recipes with filters:
```
curl --location --request GET 'http://localhost:8080/recipe?instructionContains=oven&numberOfServings=3&category=breakfast&includeIngredients=fish&excludeIngredients=chicken'
```

Update recipe:
```
curl --location --request PUT 'http://localhost:8080/recipe' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": 1,
"name": "Pad Thai",
"numberOfServings": 3,
"category": "vegetarian",
"ingredients": [
"palm sugar",
"water",
"fish sauce",
"tamarindo",
"new ingredient"
],
"instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n3.Add fish sauce and tamarind and bring to a simmer."
}'
```

Delete recipe:
```
curl --location --request DELETE 'http://localhost:8080/recipe/1'
```

## Assumptions/Decisions
* A Contract first approach was used to implement this API, where I defined the OpenAPI specification first and then implemented the service
* OpenAPI Generator plugin was used to generate the Controller interface and model classes. These classes are generated at build time.
* For efficiency purposes a Postgres jsonb column was used to store the recipe ingredients.
* I'm segregating environment configuration using Spring profiles. 'application-local.yml' contains db credentials, but for the rest of environments these credentials are expected to be defined as environment variables.
* A 3 layer architecture was chosen following the separation of concerns pattern:
    * Consumer layer: This layer receives API calls and was implemented using Spring Boot Rest Controller
    * Service layer: This layer has the business logic and was implemented using Spring Boot Service components.
    * Persistence layer: This layer handles the persistence with DB and was implemented with Spring Boot Repository components.

## Running the tests

From the project root folder, run the command ```./mvnw clean install -Pintegration-tests``` to execute:
* Unit tests
* Integration tests

## Built With

* [Java 19](https://www.oracle.com/news/announcement/oracle-releases-java-19-2022-09-20/) - Language
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework
* [PostgreSql](https://www.postgresql.org/) - Database
* [OpenAPI Generator Maven plugin](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin) - Used to generate Controller interface and model classes
* [Springdoc OpenAPI](https://springdoc.org/) - REST API documentation [Documentation](http://localhost:8080/)
* [Lombok](https://projectlombok.org/) - Java library to write less code
* [JUnit](https://junit.org) - Testing
* [Cucumber](https://cucumber.io/) - Integration tests 
* [Testcontainers](https://www.testcontainers.org/) - Provides lightweight, throwaway instance of PostgreSql for Integration tests
* [Flyway](https://flywaydb.org/) - Database migration/version control tool

## Authors

* **[Diego Junco](https://www.linkedin.com/in/diego-junco/)**