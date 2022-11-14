package com.abnamro.recipes.integration_tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "classpath:features",
        glue = "com.abnamro.recipes.integration_tests.steps"
)
public class IntegrationTests {
}
