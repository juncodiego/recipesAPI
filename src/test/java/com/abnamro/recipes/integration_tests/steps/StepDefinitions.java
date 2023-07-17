package com.abnamro.recipes.integration_tests.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
@Slf4j
public class StepDefinitions {

    @LocalServerPort
    private int port = 0;

    private static final String RECIPE_API_BASE_URL = "http://localhost:%d";
    private static final String RECIPE_API_URL = "/recipe";

    private ResponseEntity<String> response;

    private final Flyway flyway;

    @When("^the client call the create recipe endpoint with valid headers and body$")
    public void the_client_call_the_create_recipe_endpoint_with_valid_headers_and_body(String body){
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        var request =  new HttpEntity(body, headers);
        var testRestTemplate = new TestRestTemplate();
        response = testRestTemplate.postForEntity(getRecipeApiUrl(), request, String.class);
    }

    @When("the client call the get recipe endpoint with id {int}")
    public void the_client_call_the_get_recipe_endpoint_with_id(Integer id) {
        var testRestTemplate = new TestRestTemplate();
        response = testRestTemplate.getForEntity(getRecipeApiUrlWithPath("/"+id), String.class);
    }

    @When("^the client call the update recipe endpoint with valid headers and body$")
    public void the_client_call_the_update_recipe_endpoint_with_valid_headers_and_body(String body){
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        var request =  new HttpEntity(body, headers);
        var testRestTemplate = new TestRestTemplate();
        response = testRestTemplate.exchange(getRecipeApiUrl(), HttpMethod.PUT, request, String.class);
    }

    @When("the client call the delete recipe endpoint with id {int}")
    public void the_client_call_the_delete_recipe_endpoint_with_id(Integer id) {
        var testRestTemplate = new TestRestTemplate();
        response = testRestTemplate.exchange(getRecipeApiUrlWithPath("/"+id), HttpMethod.DELETE, null, String.class);
    }

    @When("the client call the get all recipes endpoint")
    public void the_client_call_the_get_recipe_endpoint_with_id() {
        var testRestTemplate = new TestRestTemplate();
        response = testRestTemplate.getForEntity(getRecipeApiUrl(), String.class);
    }

    @When("the client call the get all recipes endpoint with instructionContains {string} numberOfServings {int} category {string} includeIngredients {string} excludeIngredients {string}")
    public void the_client_call_the_get_recipe_endpoint_with_id(String instructionContains, Integer numberOfServings, String category, String includeIngredients, String excludeIngredients) {
        var testRestTemplate = new TestRestTemplate();
        var uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(getRecipeApiUrl());

        if (StringUtils.hasText(instructionContains)) {
            uriComponentsBuilder.queryParam("instructionContains",instructionContains);
        }
        if(Optional.ofNullable(numberOfServings).isPresent() && numberOfServings != -1){
            uriComponentsBuilder.queryParam("numberOfServings", String.valueOf(numberOfServings));
        }
        if (StringUtils.hasText(category)) {
            uriComponentsBuilder.queryParam("category", category);
        }
        if (StringUtils.hasText(includeIngredients)) {
            uriComponentsBuilder.queryParam("includeIngredients", includeIngredients);
        }
        if (StringUtils.hasText(excludeIngredients)) {
            uriComponentsBuilder.queryParam("excludeIngredients", excludeIngredients);
        }
        response = testRestTemplate.getForEntity(uriComponentsBuilder.build().toUri(), String.class);
    }

    @Then("the client receives status code {int} with body")
    public void the_client_receives_status_code_with_body(Integer expectedStatusCode, String expectedBody) throws JSONException {
        var responseStatusCode = Optional.ofNullable(response).map(ResponseEntity::getStatusCode).map(HttpStatus::value).orElse(0);
        Assertions.assertThat(responseStatusCode).isEqualTo(expectedStatusCode);

        if(expectedBody != null){
            var responseBody = Optional.ofNullable(response).map(HttpEntity::getBody).orElse("");
            JSONAssert.assertEquals(expectedBody, responseBody, true);
        }
    }

    @Then("the client receives status code {int}")
    public void the_client_receives_status_code(Integer expectedStatusCode) throws JSONException {
        var responseStatusCode = Optional.ofNullable(response).map(ResponseEntity::getStatusCode).map(HttpStatus::value).orElse(0);
        Assertions.assertThat(responseStatusCode).isEqualTo(expectedStatusCode);
    }

    @Given("The database is clean")
    public void the_database_is_clean() {
        log.info("********************Cleaning the database********************");
        flyway.clean();
        flyway.migrate();
    }

    @Given("The database contains test data")
    public void the_database_contains_test_data() {
        var record1 =
            """
            {
                "name": "Pad Thai",
                "numberOfServings": 2,
                "category": "main-course",
                "ingredients": [
                    "palm sugar",
                    "water",
                    "fish sauce",
                    "tamarind"
                ],
                "instructions": "1.To make the sauce, melt and caramelize the palm sugar.\\n2.Add water to stop the caramelization.\\n3.Add fish sauce and tamarind and bring to a simmer."
            }
            """;
        var record2 =
            """
            {
                "name": "Scrambled eggs",
                "numberOfServings": 1,
                "category": "breakfast",
                "ingredients": [
                    "2 large eggs",
                    "6 tbsp single cream milk",
                    "a knob of butter"
                ],
                "instructions": "1.Lightly whisk 2 large eggs, 6 tbsp single cream or full cream milk and a pinch of salt together until the mixture has just one consistency.\\n2.Heat a small non-stick frying pan for a minute or so, then add a knob of butter and let it melt. Don’t allow the butter to brown or it will discolour the eggs.\\n3.Pour in the egg mixture and let it sit, without stirring, for 20 seconds. Stir with a wooden spoon, lifting and folding it over from the bottom of the pan."
            }
            """;
        the_client_call_the_create_recipe_endpoint_with_valid_headers_and_body(record1);
        the_client_call_the_create_recipe_endpoint_with_valid_headers_and_body(record2);
    }

    private String getRecipeApiUrl(){
        return String.format(RECIPE_API_BASE_URL, port) + RECIPE_API_URL;
    }

    private String getRecipeApiUrlWithPath(String path){
        return getRecipeApiUrl() + path;
    }

}
