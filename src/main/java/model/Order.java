package model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Order {
    private List<Ingredient> ingredients;
    @SerializedName("_id")
    private String id;
    private String status;
    private int number;
    private Date createdAt;
    private Date updatedAt;

    // Геттеры
    public List<Ingredient> getIngredients() { return ingredients; }
    public String getId() { return id; }
    public String getStatus() { return status; }
    public int getNumber() { return number; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    // Сеттеры
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public void setId(String id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setNumber(int number) { this.number = number; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}