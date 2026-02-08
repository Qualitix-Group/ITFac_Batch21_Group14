package qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import qa.utils.TestData;

import static io.restassured.RestAssured.given;

public class AuthClient {

    public static String getToken(String usernameKey, String passwordKey) {

        String username = TestData.get(usernameKey);
        String password = TestData.get(passwordKey);

        String base = TestData.get("base.url");
        String[] paths = {"/api/auth/login", "/api/authenticate", "/authenticate", "/login"};

        for (String path : paths) {
            // try JSON body
            Response resp = given()
                    .baseUri(base)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body("""
                          {
                            "username": "%s",
                            "password": "%s"
                          }
                          """.formatted(username, password))
            .when()
                    .post(path)
            .then()
                    .extract()
                    .response();

            String token = extractTokenIfOk(resp);
            if (token != null) {
                return token;
            }

            // try form-urlencoded fallback
            resp = given()
                    .baseUri(base)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.URLENC)
                    .formParam("username", username)
                    .formParam("password", password)
            .when()
                    .post(path)
            .then()
                    .extract()
                    .response();

            token = extractTokenIfOk(resp);
            if (token != null) {
                return token;
            }
        }

        throw new IllegalStateException("Could not obtain token: login endpoints returned non-200 or empty token");
    }

    private static String extractTokenIfOk(Response resp) {
        int status = resp.getStatusCode();
        if (status == 200 || status == 201) {
            String token = resp.path("token");
            if (token == null) token = resp.path("accessToken");
            if (token == null) token = resp.path("access_token");
            if (token == null) token = resp.asString().replace("\"", "").trim();
            if (token != null && !token.isBlank()) {
                return token;
            }
        }
        return null;
    }
}
