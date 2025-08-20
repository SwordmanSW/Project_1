package test;

import api.ApiClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CreateUserRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static generators.UserGenerator.randomUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class ApiParamTests {

    private final ApiClient apiClient = new ApiClient();
    private final CreateUserRequest user;
    private final int expectedStatusCode;
    private final String testCaseName;

    public ApiParamTests(String testCaseName, CreateUserRequest user, int expectedStatusCode) {
        this.testCaseName = testCaseName;
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Успешное создание пользователя", randomUser(), 200},
                {"Ошибка: без email", randomUser().withEmail(null), 403},
                {"Ошибка: без пароля", randomUser().withPassword(null), 403},
                {"Ошибка: без имени", randomUser().withName(null), 403}
        });
    }

    @Test
    @DisplayName("Параметризованный тест: {0}")
    public void testCreateUser() {
        Response response = apiClient.users().create(user);
        assertThat(testCaseName + ": неверный статус-код",
                response.statusCode(),
                equalTo(expectedStatusCode));

        if (expectedStatusCode == 200) {
            assertThat("Токен не сохранён", apiClient.getToken(), notNullValue());
        }
    }

    @After
    public void tearDown() {
        if (apiClient.getToken() != null) {
            apiClient.users().delete();
        }
    }
}