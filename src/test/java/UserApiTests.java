import api.ApiClient;
import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.CreateUserResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserApiTests {
        private ApiClient apiClient;
        private CreateUserRequest user;
        private String token;

        @Test
        @DisplayName("Успешное создание пользователя")
        public void testCreateUserSuccess() {
            apiClient = new ApiClient();
            user = UserGenerator.randomUser();

            Response createResponse = apiClient.users().create(user);
            CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
            token= createUserResponse.getAccessToken();

            assertThat("Статус код должен быть 200", createResponse.statusCode(), equalTo(200));
        }

        @Test
        @DisplayName("Изменение имени пользователя на случайное")
        public void testUpdateUserName() {
            apiClient = new ApiClient();
            user = UserGenerator.randomUser();
            String email = user.getEmail();
            String password = user.getPassword();

            Response response = apiClient.users().create(user);
            CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
            token= createUserResponse.getAccessToken();

            String newName = UserGenerator.randomUser().getName();

            Response updateResponse = apiClient.users().updateUser(newName, null, token);

            // Проверки
            assertThat("Статус код должен быть 200", updateResponse.statusCode(), equalTo(200));
            assertThat("Имя должно обновиться", updateResponse.path("user.name"), equalTo(newName));
        }

    @After
    public void tearDown() {
        apiClient.users().delete(token);
    }

}
