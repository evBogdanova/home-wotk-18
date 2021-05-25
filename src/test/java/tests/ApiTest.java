package tests;

import lombok.UserData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @Test
    void successfulSingleUserTest() {
                UserData data = Spec.request
                .when()
                .get("/users/10")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(UserData.class);
                assertEquals(10, data.getUser().getId());
                assertEquals("byron.fields@reqres.in", data.getUser().getEmail());
                assertEquals("Byron", data.getUser().getFirstname());
                assertEquals("https://reqres.in/img/faces/10-image.jpg", data.getUser().getAvatar());
   }

    @Test
    void unsuccessfulSingleUserTest() {
                Spec.request
                .when()
                .get("/users/50")
                .then()
                .log().body()
                .statusCode(404);
    }

    @Test
    void successfulListUserTest() {
                UserData data = Spec.request
                .when()
                .get("/users?page=2")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(UserData.class);
                assertEquals(7, data.getUser().getId());
                assertEquals("michael.lawson@reqres.in", data.getUser().getEmail());
                assertEquals("Michael", data.getUser().getFirstname());
                assertEquals("Lawson", data.getUser().getLastname());
                assertEquals("https://reqres.in/img/faces/10-image.jpg", data.getUser().getAvatar());
    }

    @Test
    void successfulListResourcesTest() {
                Spec.request
                .when()
                .get("/unknown?page=1")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.findAll{it.id = 2}.name.flatten()",
                        hasItem("fuchsia rose"))
                .body("data.findAll{it.id = 4}.year.flatten()",
                        hasItem(2003))
                .body("data.findAll{it.year > 2004}.color.flatten()",
                        hasItem("#53B0AE"))
                .body("data.findAll{it.color = '#BF1932'}.pantone_value.flatten()",
                        hasItem("19-1664"));
    }

    @Test
    void unsuccessfulRegisterTest() {
                Spec.request
                .body("{ \"email\": \"sydney@fife\" }")
                .when()
                .post("/register")
                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}