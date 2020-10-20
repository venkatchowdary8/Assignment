package org.example;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

public class CapitalCityServiceTests
{
    @BeforeClass
    public void testSetup(){
        if(RestAssured.baseURI == null){
            RestAssured.baseURI = "https://restcountries.eu";
        }

        if(RestAssured.basePath == null){
            RestAssured.basePath = "rest/v2/capital";
        }
    }

    @Test
    public void shouldReturnSuccessStatusCodeForValidCapitalValue()
    {
        given()
            .pathParam("capital", "delhi")
        .when()
            .get("{capital}")
        .then().statusCode(200);
    }

    @Test(dataProvider = "validcapitalentries")
    public void shouldReturnCorrectResponseObjectForValidCapitalValue(String capital)
    {
        given()
            .pathParam("capital", capital)
        .when()
            .get("{capital}")
        .then().assertThat().statusCode(200)
            .body("size()", is(1))
            .body("[0].capital", equalTo(capital));
    }

    @Test(dataProvider = "invalidcapitalentries")
    public void shouldReturnNotFoundStatusCodeForWrongCapitalValue(String capital)
    {
        given()
                .pathParam("capital", capital)
                .when()
                .get("{capital}")
                .then().statusCode(404);
    }

    @DataProvider(name = "validcapitalentries")
    public Object[][] validDataInput(){
        return new Object[][] {
                {"New Delhi"},
                {"Washington, D.C."},
                {"Ottawa"}
        };
    }

    @DataProvider(name = "invalidcapitalentries")
    public Object[][] inValidDataInput(){
        return new Object[][] {
                {""},
                {"jcbldkbiuh"},
                {"7547647654"}
        };
    }
}
