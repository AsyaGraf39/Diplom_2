import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.ConstantsErrorMessage.*;
import static org.junit.Assert.*;

@DisplayName("Получение заказов пользователя")
public class GetUserOrdersTest {
    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @DisplayName("Проверка успешного получения списка заказов пользователя")
    @Test
    public void getOrdersWithAuth(){
        ValidatableResponse registerResponse = userClient.register(user);
        String getAccessToken = registerResponse.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
        orderClient.createOrder(IngredientGenerator.getDefault());
        ValidatableResponse response = orderClient.getOrders(bearerToken);

        int statusCode = response.extract().statusCode();
        Boolean isSuccess = response.extract().path("success");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertNotNull(response.extract().path("orders"));

    }

    @DisplayName("Проверка получения списка заказов неавторизованным пользователем")
    @Test
    public void getOrdersWithoutAuth(){
        bearerToken = "";
        ValidatableResponse response = orderClient.getOrdersWithoutAuth();

        int statusCode = response.extract().statusCode();
        Boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isSuccess);
        assertEquals(REQUEST_WITHOUT_AUTH, message);
    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }
}
