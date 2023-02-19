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
import static org.junit.Assert.assertEquals;

@DisplayName("Авторизация пользователя")
public class LoginUserTest {
    private User user;
    private UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @DisplayName("Проверка успешной авторизации")
    @Test
    public void userCanBeLogin(){
        userClient.register(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int statusCode = loginResponse.extract().statusCode();
        boolean isLogin = true;
        Boolean isSuccess = loginResponse.extract().path("success");

        String getAccessToken = loginResponse.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);

        assertEquals(SC_OK, statusCode);
        assertEquals(isLogin, isSuccess);
    }

    @DisplayName("Проверки авторизации с неверным паролем")
    @Test
    public void userLoginWrongPwd(){
        ValidatableResponse response = userClient.register(user);
        user.setPassword("aaa111");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        boolean isLogin = false;

        int statusCode = loginResponse.extract().statusCode();
        Boolean isSuccess = loginResponse.extract().path("success");
        String message = loginResponse.extract().path("message");

        String getAccessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals(isLogin, isSuccess);
        assertEquals(LOGIN_INCORRECT, message);
    }

    @DisplayName("Проверки авторизации с неверным email")
    @Test
    public void userLoginWrongEmail(){
        ValidatableResponse response = userClient.register(user);
        user.setEmail("aaa111!");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        boolean isLogin = false;

        int statusCode = loginResponse.extract().statusCode();
        Boolean isSuccess = loginResponse.extract().path("success");
        String message = loginResponse.extract().path("message");

        String getAccessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals(isLogin, isSuccess);
        assertEquals(LOGIN_INCORRECT, message);
    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }
}
