import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserCredentials;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.ConstantsErrorMessage.*;
import static org.junit.Assert.*;

@DisplayName("Изменение данных пользователя")
public class ChangeUserDataTest {
    private User user;
    private UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        ValidatableResponse response = userClient.register(user);
        String getAccessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
    }

    @DisplayName("Проверка успешного изменения данных авторизованного пользователя")
    @Test
    public void userDataCanBeChanged(){
        String newEmail = UserGenerator.getRandomEmail();
        user.setEmail(newEmail);
        user.setPassword("111!!!");
        user.setName("КОТЭ");
        ValidatableResponse response = userClient.changeData(user,bearerToken);
        int statusCode = response.extract().statusCode();
        Boolean success = response.extract().path("success");
        String email = response.extract().path("user.email");
        String name = response.extract().path("user.name");

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int loginWithNewPwd = loginResponse.extract().statusCode();

        assertEquals(SC_OK, statusCode);
        assertTrue(success);
        assertEquals(newEmail.toLowerCase(), email);
        assertEquals(user.getName(), name);
        assertEquals(SC_OK, loginWithNewPwd);
    }

    @DisplayName("Проверка изменения данных пользователя без авторизации")
    @Test
    public void userChangedWithoutLogin(){
        user.setEmail("Sobanya@inbox.ru");
        user.setPassword("111!!!111");
        user.setName("Трололо");
        ValidatableResponse response = userClient.changeDataWithoutAuth(user);
        int statusCode = response.extract().statusCode();
        Boolean success = response.extract().path("success");
        String message = response.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(success);
        assertEquals(REQUEST_WITHOUT_AUTH, message);
    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }
}
