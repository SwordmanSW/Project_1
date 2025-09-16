package model;

public class CreateOrderResponse {
    private boolean success;
    private Order order;
    private String name;

    // Конструкторы, геттеры и сеттеры
    public CreateOrderResponse() {}

    public CreateOrderResponse(boolean success, Order order, String name) {
        this.success = success;
        this.order = order;
        this.name = name;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}