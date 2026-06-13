package com.retail.pricing.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class PriceConfigurationSteps {

    private final ProductSteps productSteps;
    private Response response;

    public PriceConfigurationSteps(ProductSteps productSteps) {
        this.productSteps = productSteps;
    }

    @Given("I add a price configuration with price {double} currency {string} from {string} to {string}")
    @When("I add a price configuration with price {double} currency {string} from {string} to {string}")
    public void iAddAPriceConfiguration(double price, String currency, String startDate, String endDate) {
        String body = String.format(
                "{\"productId\":%d,\"price\":%.2f,\"currency\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
                productSteps.getProductId(), price, currency, startDate, endDate);
        response = given().contentType(ContentType.JSON).body(body)
                .put("/price-configurations");
    }

    @Then("the config price is {double}")
    public void theConfigPriceIs(double price) {
        assertThat(response.jsonPath().getDouble("price")).isEqualTo(price, offset(0.01));
    }

    @Then("the config currency is {string}")
    public void theConfigCurrencyIs(String currency) {
        assertThat(response.jsonPath().getString("currency")).isEqualTo(currency);
    }

    @Then("the config start date is {string}")
    public void theConfigStartDateIs(String startDate) {
        assertThat(response.jsonPath().getString("startDate")).isEqualTo(startDate);
    }

    @Then("the config end date is {string}")
    public void theConfigEndDateIs(String endDate) {
        assertThat(response.jsonPath().getString("endDate")).isEqualTo(endDate);
    }
}
