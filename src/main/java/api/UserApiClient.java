package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CreateUserRequest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserApiClient {

    private final ApiClient apiClient;

    public UserApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Step("Создание пользователя")
    public Response create(CreateUserRequest user) {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/auth/register");

        // Сохраняем токен, если регистрация успешна
        if (response.statusCode() == 200) {
            String token = response.path("accessToken"); // или другой путь к токену
            apiClient.setToken(token);
        }
        return response;
    }

    @Step("Авторизация пользователя")
    public Response login(CreateUserRequest user) {
        Response createResponse = create(user);
        if (createResponse.statusCode() != 200) {
            return createResponse;
        }

        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/auth/login")
                .then()
                .extract().response();
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(String newName, String newEmail) {
        Map<String, String> updateData = new HashMap<>();
        if (newName != null) updateData.put("name", newName);
        if (newEmail != null) updateData.put("email", newEmail);

        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", apiClient.getToken())
                .body(updateData)
                .patch("/api/auth/user");
    }

     @Step("Получение информации о пользователе")
     public Response getUserInfo() {
         return given()
                 .header("Authorization", apiClient.getToken())
                 .get("/api/auth/user");
     }


    @Step("Удаление пользователя")
    public Response delete() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", apiClient.getToken())
                .delete("/api/auth/user");
    }
}
