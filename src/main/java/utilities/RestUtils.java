package utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RestUtils {
    protected static Response response;
    FileInputStream fileInputStream;

    public Response sendGetRequest(String url) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .header("ContentType","application/json")
                .header("Accept",ContentType.JSON)
                .when()
                .get(url);
        return response;
    }

    public Response sendGetRequestUsingQueryParam(String url, String term,String subject,String grade,String schoolId) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .header("ContentType","application/json")
                .header("Accept",ContentType.JSON)
                .queryParam("term",term)
                .queryParam("subject",subject)
                .queryParam("grade",grade)
                .queryParam("school_id",schoolId)
                .when()
                .get(url);
        return response;
    }

    public Response sendGetRequestFromQueryBook(String url) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .header("ContentType","application/json")
                .header("Accept",ContentType.JSON)
                .header("api-access-token",GlbVar.api_access_token)
                .when()
                .get(url);
        return response;
    }

    public Response sendGetRequestFromQueryBookUsingQueryParameter(String url, String param) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .header("ContentType","application/json")
                .header("Accept",ContentType.JSON)
                .header("api-access-token",GlbVar.api_access_token)
                .queryParam("params",param)
                .when()
                .get(url);
        return response;
    }

    public Response sendPostDataWithoutAuthentication(String url, String body) {
        response = SerenityRest
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url);
        return response;
    }


    public Response sendPostDataWithAuthentication(String url, String body) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url);
        return response;
    }

    public Response sendPostRequestFromQueryBook(String url, String body) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .header("ContentType","application/json")
                .header("Accept",ContentType.JSON)
                .header("api-access-token",GlbVar.api_access_token)
                .body(body)
                .when()
                .post(url);
        return response;
    }

    public Response sendDeleteRequest(String url, String body) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .delete(url);
        return response;
    }

    public Response sendDeleteRequestWithoutBody(String url) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .contentType(ContentType.JSON)
                .when()
                .delete(url);
        return response;
    }

    public Response sendPatchRequest(String url, String body) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(url);
        return response;
    }

    public Response sendPutRequest(String url, String body) {
        response = SerenityRest.given()
                .header("Authorization", "Bearer " + GlbVar.authToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url);
        return response;
    }

    public boolean isSuccess(Response response) {
        if (response.statusCode() == 200) {
            return true;
        } else if (response.statusCode() == 202) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isUnauthorized(Response response) {
        if (response.statusCode() == 401) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRequestCreated(Response response) {
        if (response.statusCode() == 201) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBadRequest(Response response) {
        if (response.statusCode() == 400) {
            return true;
        } else {
            return false;
        }
    }

    public String getJSON(String Json) {
        String jsonData = "";
        String filPath = System.getProperty("user.dir");
        try {
            fileInputStream = new FileInputStream(new File(filPath + "/src/test/resources/Json/" + Json + ""));
            jsonData = IOUtils.toString(fileInputStream, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    public Response sendPostDataWithAuthenticationForKeyClock(String url, String body) {
        response = SerenityRest
                .given()
                .header("Authorization", "Bearer " + GlbVar.authTokenKeyClock)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url);
        return response;
    }

    public void checkStatusCode(Response response){
        if(isSuccess(response)){
            Assertions.assertTrue(true);
        }
        else{
            Assertions.assertTrue(false, "Response Code came "+response.statusCode());
        }
    }

    public Response sendGetRequestWithCredentials(String baseURL, String endPoint) {
        RestAssured.baseURI = Config.api_baseUrl;
        response = given()
                .auth()
                .preemptive()
                .basic(Config.api_username, Config.api_password)
                .when()
                .get(endPoint);
        return response;
    }

    public ArrayList returnListInShortedOrder(ArrayList listValue, int listSize){
        for (int i = 0; i < listSize; i++) {
            listValue.add(listValue.get(i));
        }
        Collections.sort(listValue);
        return listValue;
    }
}

