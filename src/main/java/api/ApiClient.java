package api;

import io.restassured.RestAssured;

public class ApiClient {

    UserApiTest users;
    OrderApiTest orders;

    public ApiClient(UserApiTest users, OrderApiTest orders) {
        this.users = users;
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        this.orders = orders;
    }

    public OrderApiTest getOrders() {
        return orders;
    }

    public void setOrders(OrderApiTest orders) {
        this.orders = orders;
    }

    public UserApiTest getUsers() {
        return users;
    }

    public void setUsers(UserApiTest users) {
        this.users = users;
    }

}
