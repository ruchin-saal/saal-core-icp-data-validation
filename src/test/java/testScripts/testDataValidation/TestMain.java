package testScripts.testDataValidation;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;


import static io.restassured.RestAssured.given;

public class TestMain {

    @Test
    public void test() {
        TestPojo obj=new TestPojo();
        obj.setId(8);
        obj.setEmail("abc@abc.com");
        obj.setFirst_name("abc");
        obj.setLast_name("xyz");
        obj.setAvatar("123.jpg");
        Response response=
                given().
                        contentType(ContentType.JSON).body(obj).
                        when().post("https://reqres.in/api/users/");
        response.then().statusCode(201);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody().asPrettyString());
    }
}
