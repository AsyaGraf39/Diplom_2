import io.qameta.allure.Step;
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

@DisplayName("Регистрация пользователя")
public class RegisterUserTest {
    private User user;
    private UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @DisplayName("Проверка успешной регистрации пользователя")
    @Test
    public void userCanBeRegister(){
        ValidatableResponse response = userClient.register(user);
        int statusCode = response.extract().statusCode();

        String accessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(accessToken);

        assertEquals(SC_OK, statusCode);
        assertTrue(response.extract().path("success"));
    }

    @DisplayName("Проверка регистрации пользователя, который уже зарегистрирован")
    @Test
    public void registerUserExists() {
        ValidatableResponse SuccessResponse = userClient.register(user);
        ValidatableResponse response = userClient.register(user);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        String accessToken = SuccessResponse.extract().path("accessToken");
        bearerToken = String.valueOf(accessToken);

        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals(REGISTER_USER_EXISTS, errorMessage);
        assertFalse(response.extract().path("success"));
        }

    @DisplayName("Проверка регистрации пользователя без email")
    @Test
    public void registerUserWithoutEmail() {
        user.setEmail("");
        ValidatableResponse response = userClient.register(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        String accessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(accessToken);

        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals(REGISTER_NO_REQUIRED_FIELDS, message);
        assertFalse(response.extract().path("success"));
        }

    @DisplayName("Проверка регистрации пользователя без email")
    @Test
    public void registerUserWithoutPwd() {
        user.setPassword("");
        ValidatableResponse response = userClient.register(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        String accessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(accessToken);

        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals(REGISTER_NO_REQUIRED_FIELDS, message);
        assertFalse(response.extract().path("success"));
        }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }


}
