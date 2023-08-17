package userDataChangeTests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userData.User;
import userData.UserRandomData;
import userData.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static userData.UserRandomData.faker;

public class UserDataChangeTest {

    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @Before
    @Step("Создание объектов для проведения тестов.")
    public void setUp() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Тест на возможность изменения password пользователя.")
    @Description("Успешное изменение в системе password пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldChangePassword() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @io.qameta.allure.Description("Создание пользователя, изменение логина и почты, проверка кода ответа 200")
    public void userDataChangingTestWithAuthorizationTest() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест на невозможность изменения password у существующего, но НЕ авторизованного пользователя.")
    @Description("Нельзя изменить в системе password у существующего, но НЕ авторизованного пользователя с проверкой возвращаемого статус-кода 401 Unauthorized в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    @Issue("Bug report link")
    public void wouldChangePasswordNonAuthorizedUser() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Тест на невозможность изменения name у существующего, но НЕ авторизованного пользователя.")
    @Description("Нельзя изменить в системе password у существующего, но НЕ авторизованного пользователя с проверкой возвращаемого статус-кода 401 Unauthorized в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    @Issue("Bug report link")
    public void wouldChangeNameNonAuthorizedUser() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}