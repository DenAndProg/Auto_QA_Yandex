package Specs;

import MyData.EndPointsApi;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static MyData.Auth.token;
import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;

public class Specs {

    public static RequestSpecification requestSpecification(){
        RequestSpecification requestSpec = given()
                .baseUri(EndPointsApi.baseUri)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when();
        return requestSpec;
    }

    /*public static ResponseSpecification responseSpecification(){
        return expect().;
    }*/

}
