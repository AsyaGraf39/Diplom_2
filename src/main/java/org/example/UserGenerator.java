package org.example;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    @Step("Рандомное заполнение полей email, пароля и имени пользователя")
    public static User getRandomUser(){
        String email = RandomStringUtils.randomAlphanumeric(6) + "@mail.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphabetic(8);

        return new User(email, password, name);
    }

    public static String getRandomEmail(){
        String email = RandomStringUtils.randomAlphanumeric(6) + "@mail.ru";
        return email;
    }

}
