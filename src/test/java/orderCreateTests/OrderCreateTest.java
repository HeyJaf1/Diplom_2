package orderCreateTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import login.UserLogin;
import login.UserLoginFieldsGenerator;
import login.UserLoginSteps;
import order.Order;
import order.OrderCreateSteps;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OrderCreateTest {

    private OrderCreateSteps step;

    @Before
    @Step("Создание объектов для проведения тестов.")
    public void setUp() {
        step = new OrderCreateSteps();
    }

    @Test
    @DisplayName("Тест на получение ингредиентов.")
    @Description("Успешное получение списка ингредиентов с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void getIngredientsList() {
        ValidatableResponse responseCreate = step.getIngredients();
        responseCreate.assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест на создание заказа c ингредиентами без авторизации.")
    @Description("Успешное получение списка ингредиентов с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    public void postOrderWithoutAuthorization() {
        Order order = new Order(Arrays.asList(step.getListOfObj()));
        ValidatableResponse responseCreate = step.orderCreate(order);
        responseCreate.assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест на создание заказа с ингредиентами с авторизацией.")
    @Description("Успешное получение списка ингредиентов с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    public void postOrderWithAuthorization() {
        UserLoginSteps userLoginSteps = new UserLoginSteps();
        userLoginSteps.logging(UserLoginFieldsGenerator.passingGenerator());
        Order order = new Order(Arrays.asList(step.getListOfObj()));
        ValidatableResponse response = step.orderCreate(order);
        response.assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест на создание заказа без игредиентов.")
    @Description("Успешное получение возвращаемого статус-кода 400 Bad Request в соответствии с документацией.")
    @Severity(SeverityLevel.TRIVIAL)
    public void postOrderWithoutIngredients() {
        Order order = new Order();
        ValidatableResponse responseCreate = step.orderCreateWithoutIngredients(order);
        responseCreate.assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Тест на получение заказа конкретного пользователя.")
    @Description("Успешное получение заказа с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    public void getUserOrder() {
        new UserLoginSteps().logging(new UserLogin());
        Order order = new Order(List.of(step.getListOfObj()));
        step.orderCreate(order);
        ValidatableResponse responseGetOrder = step.getTheOrder(order);
        responseGetOrder.assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест на получение заказа неавторизованного пользователя.")
    @Description("Успешное получение возвращаемого статус-кода 401 Unauthorized в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    public void getNotAuthUserOrder() {
        ValidatableResponse responseCreate = step.getTheOrderWithoutAuth();
        responseCreate.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}