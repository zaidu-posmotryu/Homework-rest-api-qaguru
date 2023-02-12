import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ReqresFirstApiTests {
    Faker faker = new Faker();

    @Test
    void loginSuccessfulTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void loginUnsuccessfulTest() {
        String data = "{\"email\": \"peter@klaven\"}";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .assertThat().body(containsString("error"));
    }

    @Test
    void registerSuccessfulTest() {
        given()
                .log().uri()
                .contentType(JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    @Test
    void getUserTest() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat().body(containsString("email"))
                .assertThat().body(containsString("first_name"))
                .assertThat().body(containsString("last_name"))
                .assertThat().body(containsString("avatar"));
    }

    @Test
    void createUserTest() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .assertThat().body(containsString("id"))
                .assertThat().body(containsString("createdAt"));
    }

    @Test
    void updateUserTest() {
        String name = faker.name().name();
        String job = faker.job().position();
        given()
                .log().uri()
                .body("{\"name\": \"" + name + "\",\"job\": \"" + job + "\"}")
                .contentType(JSON)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is(name))
                .body("job", is(job))
                .assertThat().body(containsString("updatedAt"));
    }

    @Test
    void deleteUserTest() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
