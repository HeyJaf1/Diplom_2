package userLoginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import login.UserLogin;
import login.UserLoginSteps;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {

    private UserLoginSteps step;
    UserLogin userLogin;

    @Before
    @Step("Создание объектов для проведения тестов.")
    public void setUp() {
        step = new UserLoginSteps();
    }

    @Test
    @DisplayName("Тест на вход в систему уникального пользователя.")
    @Description("Успешный вход в систему уникального пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldCreateAndLogInAUser() {
        ValidatableResponse responseCreate = step.logging(userLogin);
        responseCreate.assertThat().statusCode(HttpStatus.SC_OK).extract().path("success");
    }

    @Test
    @DisplayName("Тест на вход в систему с неверным логином и паролем.")
    @Description("Успешный возврат статус-кода 401 Unauthorized в соответствии с документацией при входе с неверным логином и паролем.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldNotLogAUser() {
        ValidatableResponse responseCreate = step.loggingWithInvalidData(userLogin);
        responseCreate.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
    @After
    @DisplayName("Удаление пользователя.")
    public void tearDown() {
        step.deleteUser();
    }
}