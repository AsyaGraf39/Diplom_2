package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client{
    private static final String PATH_REGISTER = "api/auth/register";
    private static final String PATH_LOGIN = "api/auth/login ";
    private static final String PATH_CHANGE_DATA = "api/auth/user";
    private static final String PATH_DELETE = "api/auth/user";

    @Step("Регистрация пользователя")
    public ValidatableResponse register(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(PATH_REGISTER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserCredentials credentials){
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeData(User user, String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .body(user)
                .when()
                .patch(PATH_CHANGE_DATA)
                .then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse changeDataWithoutAuth(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(PATH_CHANGE_DATA)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .when()
                .delete(PATH_DELETE)
                .then();
    }
}
