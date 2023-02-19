package org.example;
import java.util.List;

public class IngredientGenerator {
    public static Ingredients getDefault(){
        return new Ingredients(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa71"));
    }
    public static Ingredients getNull(){
        return new Ingredients(List.of());
    }
    public static Ingredients getInvalidIngredient(){
        return new Ingredients(List.of("60d3b41abdacab0026a733c6111", "609646e4dc916e00276b2870111"));
    }
}
