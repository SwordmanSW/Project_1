import api.ApiClient;
import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.*;
import org.junit.After;
import org.junit.Test;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderApiTests {
    private ApiClient apiClient;
    private CreateUserRequest user;
    private String token;


    @Test
    @DisplayName("Создание заказа")
    public void testSuccessfulOrderCreation() {
        apiClient = new ApiClient();
        user = UserGenerator.randomUser();

        Response createResponse = apiClient.users().create(user);
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        token = createUserResponse.getAccessToken();

        Response response = apiClient.orders().create();
        CreateOrderResponse orderResponse = response.as(CreateOrderResponse.class);

        Order order = orderResponse.getOrder();
        assertThat("Заказ не должен быть null", order, notNullValue());
        assertThat("Ингредиенты не должны быть null", order.getIngredients(), notNullValue());
        assertThat("ID заказа не должен быть null", order.getId(), notNullValue());
        assertThat("Номер заказа должен быть положительным", order.getNumber(), greaterThan(0));

        // Проверяем ингредиенты как объекты
        List<Ingredient> ingredients = order.getIngredients();
        for (Ingredient ingredient : ingredients) {
            assertThat("Ингредиент должен иметь ID", ingredient.getId(), notNullValue());
            assertThat("Ингредиент должен иметь тип", ingredient.getType(), notNullValue());
            assertThat("Ингредиент должен иметь имя", ingredient.getName(), notNullValue());
        }
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    public void testGetUserOrders() {
        apiClient = new ApiClient();
        user = UserGenerator.randomUser();

        Response createResponse = apiClient.users().create(user);
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        token = createUserResponse.getAccessToken();

        Response orderResponse = apiClient.orders().create();
        CreateOrderResponse createOrderResponse = orderResponse.as(CreateOrderResponse.class);

        assertThat("Заказ создан", orderResponse.statusCode(), equalTo(200));
        assertThat("success true", createOrderResponse.isSuccess(), equalTo(true));

        int createdOrderNumber = createOrderResponse.getOrder().getNumber();

        // Получаем заказы пользователя
        Response ordersResponse = apiClient.orders().getUserOrders();
        GetUserOrdersResponse userOrders = ordersResponse.as(GetUserOrdersResponse.class);

        assertThat("Статус 200", ordersResponse.statusCode(), equalTo(200));
        assertThat("success true", userOrders.isSuccess(), equalTo(true));
        assertThat("Список заказов не null", userOrders.getOrders(), notNullValue());

        UserOrder foundOrder = findOrderByNumber(userOrders.getOrders(), createdOrderNumber);
        assertThat("Созданный заказ есть в списке", foundOrder, notNullValue());
    }

    // Метод для поиска по номеру — теперь принимает List<UserOrder>
    private UserOrder findOrderByNumber(List<UserOrder> orders, int number) {
        for (UserOrder order : orders) {
            if (order.getNumber() == number) {
                return order;
            }
        }
        return null;
    }

    @After
    public void tearDown() {
            apiClient.users().delete(token);
    }
}