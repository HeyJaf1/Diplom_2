package order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import login.UserLogin;
import login.UserLoginSteps;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderCreateSteps {

    ListOfIngredients listOfIngredients = new ListOfIngredients();
    UserLoginSteps step;
    UserLoginSteps userLoginSteps = new UserLoginSteps();
    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String GET_INGREDIENTS_LIST = BASE_URI + "/api/ingredients";
    protected final String POST_ORDER_CREATE = BASE_URI + "/api/orders";
    protected final String GET_ORDER = BASE_URI + "/api/orders";


    @Description("Общая спецификация.")
    private RequestSpecification getSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }

    @Step("Получение списка ингредиентов.")
    public ValidatableResponse getIngredients() {
        return getSpec()
                .when()
                .get(GET_INGREDIENTS_LIST)
                .then().log().all();
    }

    @Step("Получение объекта из ингредиентов")
    public ListOfIngredients getIngredientsObject() {
        return listOfIngredients =
                given().log().all()
                        .contentType(ContentType.JSON)
                        .get(GET_INGREDIENTS_LIST)
                        .body().as(ListOfIngredients.class);
    }

    @Step("obj to list")
    public String getListOfObj() {
        List<Data> dataList = getIngredientsObject().getData();
        int size = dataList.size();
        int index = new Random().nextInt(size);
        return dataList.get(index).getId();
    }

    @Step("Создание заказа.")
    public ValidatableResponse orderCreate(Order order) {
        return getSpec()
                .body(order)
                .when()
                .post(POST_ORDER_CREATE)
                .then().log().all();
    }

    @Step("Создание заказа без ингредиентов.")
    public ValidatableResponse orderCreateWithoutIngredients(Order order) {
        return getSpec()
                .body(order)
                .when()
                .post(POST_ORDER_CREATE)
                .then().log().all();
    }

    @Step("Получение заказа авторизованным пользователем.")
    public ValidatableResponse getTheOrder(Order order) {
        String accessToken = userLoginSteps.logging().extract().path("accessToken");
        orderCreate(order);

        return getSpec()
                .auth().oauth2(accessToken.substring(7))
                .when()
                .get(GET_ORDER)
                .then()
                .log()
                .all();
    }

    @Step("Получение заказа неавторизованным пользователем.")
    public ValidatableResponse getTheOrderWithoutAuth() {
        return getSpec()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get(GET_ORDER)
                .then()
                .log()
                .all();
    }
}