package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CreateOrderRequest;
import model.Ingredient;
import model.IngredientsResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrdersApiClient {

    private final ApiClient apiClient;

    public OrdersApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Step("Создание заказа")
    public Response create() {
        // 1. Получаем все ингредиенты
        List<Ingredient> allIngredients = getAllIngredients();

        // 2. Создаем запрос со случайными ингредиентами
        CreateOrderRequest request = CreateOrderRequest.withRandomIngredients(allIngredients);

        // 3. Отправляем запрос
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", apiClient.getToken())
                .body(request)
                .post("/api/orders");
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders() {
        return given().log().all()
                .header("Authorization", apiClient.getToken())
                .get("/api/orders");
    }

    private List<Ingredient> getAllIngredients() {
        IngredientsResponse response = given()
                .get("/api/ingredients")
                .as(IngredientsResponse.class);
        return response.getData();
    }

}