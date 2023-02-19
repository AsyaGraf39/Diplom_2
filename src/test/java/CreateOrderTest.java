import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.ConstantsErrorMessage.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Создание заказа")
public class CreateOrderTest {
    private User user;
    private Ingredients ingredients;
    private UserClient userClient;
    private OrderClient orderClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        ValidatableResponse response = userClient.register(user);
        String getAccessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
    }

    @DisplayName("Проверка успешного создания заказа")
    @Test
    public void successOrderCreation(){
        ingredients = IngredientGenerator.getDefault();
        ValidatableResponse response = orderClient.createOrder(ingredients, bearerToken);
        String expectedName = "Space био-марсианский флюоресцентный бургер";
        boolean isCreated = true;

        int statusCode = response.extract().statusCode();
        String actualName = response.extract().path("name");
        Boolean isSuccess = response.extract().path("success");
        int numberOrder = response.extract().path("order.number");

        assertEquals(SC_OK, statusCode);
        assertEquals(expectedName, actualName);
        assertEquals(isCreated, isSuccess);
        assertNotNull(numberOrder);
    }

    @DisplayName("Проверка создания заказа без авторизации")
    @Test
    public void createOrderWithoutAuth(){
        ingredients = IngredientGenerator.getDefault();
        ValidatableResponse response = orderClient.createOrder(ingredients);
        boolean isCreated = false;

        int statusCode = response.extract().statusCode();
        Boolean isSuccess = response.extract().path("success");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals(isCreated, isSuccess);
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    public void createOrderWithoutIngredients(){
        ingredients = IngredientGenerator.getNull();
        ValidatableResponse response = orderClient.createOrder(ingredients, bearerToken);
        boolean isCreated = false;

        int statusCode = response.extract().statusCode();
        Boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(isCreated, isSuccess);
        assertEquals(ORDER_WITHOUT_INGREDIENTS, message);
    }

    @Test
    @DisplayName("Проверка создания заказа с невалидным хешем ингредиентов")
    public void createOrderInvalidIngredients(){
        ingredients = IngredientGenerator.getInvalidIngredient();
        ValidatableResponse response = orderClient.createOrder(ingredients, bearerToken);

        int statusCode = response.extract().statusCode();

        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }
}
