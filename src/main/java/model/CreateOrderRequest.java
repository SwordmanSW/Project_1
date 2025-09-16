package model;

import java.util.*;
import java.util.stream.Collectors;

public class CreateOrderRequest {
    private List<String> ingredients;

    // Метод для создания запроса со случайными ингредиентами
    public static CreateOrderRequest withRandomIngredients(List<Ingredient> allIngredients) {
        CreateOrderRequest request = new CreateOrderRequest();

        // Группируем ингредиенты по типу
        Map<String, List<Ingredient>> ingredientsByType = allIngredients.stream()
                .collect(Collectors.groupingBy(Ingredient::getType));

        // Выбираем по одному случайному ингредиенту из каждого типа
        List<String> randomIngredientIds = new ArrayList<>();
        ingredientsByType.forEach((type, ingredients) -> {
            if (!ingredients.isEmpty()) {
                Ingredient randomIngredient = ingredients.get(new Random().nextInt(ingredients.size()));
                randomIngredientIds.add(randomIngredient.getId());
            }
        });

        request.ingredients = randomIngredientIds;
        return request;
    }
}