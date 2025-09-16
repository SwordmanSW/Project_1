package model;

import java.util.List;

public class GetUserOrdersResponse {
    private boolean success;
    private List<UserOrder> orders;
    private int total;
    private int totalToday;

    // Геттеры
    public boolean isSuccess() { return success; }
    public List<UserOrder> getOrders() { return orders; }
    public int getTotal() { return total; }
    public int getTotalToday() { return totalToday; }
}