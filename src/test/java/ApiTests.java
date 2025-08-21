import api.ApiClient;
import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CreateUserRequest;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {
    private ApiClient apiClient;
    private CreateUserRequest user;
    private String token;


    @Test
    @DisplayName("Изменение имени пользователя на случайное")
    public void testUpdateUserName() {
        // Инициализация
        apiClient = new ApiClient();
        user = UserGenerator.randomUser();

        // Авторизация
        Response loginResponse = apiClient.users().login(user);
        token = loginResponse.path("accessToken");
        apiClient.setToken(token);


        // Генерация нового имени
        String newName = UserGenerator.randomUser().getName();

        // Изменение имени
        Response updateResponse = apiClient.users().updateUser(newName, null);

        // Проверки
        assertThat("Статус код должен быть 200", updateResponse.statusCode(), equalTo(200));
        assertThat("Имя должно обновиться", updateResponse.path("user.name"), equalTo(newName));
    }

    @Test
    @DisplayName("Создание заказа")
    public void testSuccessfulOrderCreation() {
        // Инициализация
        apiClient = new ApiClient();
        user = UserGenerator.randomUser();

        // Авторизация
        Response loginResponse = apiClient.users().login(user);
        token = loginResponse.path("accessToken");
        apiClient.setToken(token);

        // Создание заказа
        Response response = apiClient.orders().create();

        // Проверки
        assertThat("Статус код должен быть 200", response.statusCode(), equalTo(200));
        assertThat("Поле success должно быть true", response.path("success"), equalTo(true));

        Map<String, Object> order = response.path("order");
        assertThat("Заказ должен содержать ingredients", order.get("ingredients"), notNullValue());
        assertThat("Заказ должен содержать _id", order.get("_id"), notNullValue());
        assertThat("Заказ должен содержать status", order.get("status"), notNullValue());
        assertThat("Заказ должен содержать number", order.get("number"), notNullValue());
        assertThat("Заказ должен содержать createdAt", order.get("createdAt"), notNullValue());
        assertThat("Заказ должен содержать updatedAt", order.get("updatedAt"), notNullValue());
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    public void testGetUserOrders() {
        // Инициализация
        apiClient = new ApiClient();
        user = UserGenerator.randomUser();

        // Авторизация (с автоматической регистрацией)
        Response loginResponse = apiClient.users().login(user);
        token = loginResponse.path("accessToken");
        apiClient.setToken(token);

        // Создаем тестовый заказ
        Response createOrderResponse = apiClient.orders().create();
        assertThat("Заказ должен быть создан", createOrderResponse.statusCode(), equalTo(200));
        int orderNumber = createOrderResponse.path("order.number");

        // Получаем заказы пользователя
        Response ordersResponse = apiClient.orders().getUserOrders();

        // Проверки ответа
        assertThat("Статус код должен быть 200", ordersResponse.statusCode(), equalTo(200));
        assertThat("Поле success должно быть true", ordersResponse.path("success"), equalTo(true));

        // Проверка структуры ответа
        List<Map<String, Object>> orders = ordersResponse.path("orders");
        assertThat("Список заказов не должен быть пустым", orders, not(empty()));

        // Проверка полей в заказе
        Map<String, Object> firstOrder = orders.get(0);
        assertThat("Заказ должен содержать ingredients", firstOrder.get("ingredients"), notNullValue());
        assertThat("Заказ должен содержать _id", firstOrder.get("_id"), notNullValue());
        assertThat("Заказ должен содержать status", firstOrder.get("status"), notNullValue());
        assertThat("Заказ должен содержать number", firstOrder.get("number"), notNullValue());
        assertThat("Заказ должен содержать createdAt", firstOrder.get("createdAt"), notNullValue());
        assertThat("Заказ должен содержать updatedAt", firstOrder.get("updatedAt"), notNullValue());

        // Проверка totals
        assertThat("Должно быть поле total", ordersResponse.path("total"), notNullValue());
        assertThat("Должно быть поле totalToday", ordersResponse.path("totalToday"), notNullValue());

        // Проверка, что созданный заказ есть в списке
        boolean orderFound = orders.stream()
                .anyMatch(o -> o.get("number").equals(orderNumber));
        assertThat("Созданный заказ должен быть в списке", orderFound, equalTo(true));
    }

    @After
    public void tearDown() {
        if (apiClient != null && apiClient.getToken() != null) {
            apiClient.users().delete();
        }
    }
}