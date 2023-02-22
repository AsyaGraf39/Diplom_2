package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client{
    private static final String PATH_CREATE = "api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Ingredients ingredients, String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .body(ingredients)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse createOrder(Ingredients ingredients){
        return given()
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getOrders(String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .when()
                .get(PATH_CREATE)
                .then();
    }

    @Step("Получение заказов неавторизованным пользователя")
    public ValidatableResponse getOrdersWithoutAuth(){
        return given()
                .spec(getSpec())
                .when()
                .get(PATH_CREATE)
                .then();
    }
}
