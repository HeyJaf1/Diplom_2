package userCreateTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import userData.User;
import userData.UserRandomData;
import userData.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;


    @Test
    @DisplayName("Тест на создание уникального пользователя.")
    @Description("Успешное создание уникального пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldCreateAUser() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест на создание пользователя, который уже создан.")
    @Description("Успешный возврат статус-кода 403 Forbidden в соответствии с документацией при создании пользователя, который уже был создан ранее.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldNotCreateAUserThatHasBeenAlreadyCreatedEarlier() throws InterruptedException {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .statusCode(200);
        response = userSteps.userCreate(user);
        response.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Тест на создание пользователя, если нет одного из полей.")
    @Description("Успешный возврат статус-кода 403 Forbidden в соответствии с документацией при создании пользователя, если нет одного из полей.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldNotCreateAUserWithoutOneRequiredField() {
        user = UserRandomData.createRandomNoNameUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}