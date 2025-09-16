package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.LoginUserRequest;

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

        String token = response.path("accessToken");
        apiClient.setToken(token);
        return response;
    }

    @Step("Авторизация пользователя")
    public Response login(String email, String password) {
        LoginUserRequest request = new LoginUserRequest(email, password);
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/auth/login");
        ;

        return response;
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(String newName, String newEmail, String token) {
        Map<String, String> updateData = new HashMap<>();
        if (newName != null) updateData.put("name", newName);
        if (newEmail != null) updateData.put("email", newEmail);

        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(updateData)
                .patch("/api/auth/user");
    }


    @Step("Удаление пользователя")
    public Response delete(String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .delete("/api/auth/user");
    }
}
