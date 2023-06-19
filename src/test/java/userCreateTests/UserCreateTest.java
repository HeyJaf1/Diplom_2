package userCreateTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserCreate;
import user.UserCreateFieldsGenerator;
import user.UserCreateSteps;

public class UserCreateTest {

    private UserCreateSteps step;
    UserCreateFieldsGenerator userCreateFieldsGenerator;

    @Before
    @Step("Создание объектов для проведения тестов.")
    public void setUp() {
        userCreateFieldsGenerator = new UserCreateFieldsGenerator();
        step = new UserCreateSteps();
    }

    @Test
    @DisplayName("Тест на создание уникального пользователя.")
    @Description("Успешное создание уникального пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldCreateAUser() {
        UserCreate userCreate = UserCreateFieldsGenerator.passingGeneratorData();
        ValidatableResponse responseCreate = step.create(userCreate);
        responseCreate.assertThat().statusCode(HttpStatus.SC_OK).extract().path("ok");
    }

    @Test
    @DisplayName("Тест на создание пользователя, который уже создан.")
    @Description("Успешный возврат статус-кода 403 Forbidden в соответствии с документацией при создании пользователя, который уже был создан ранее.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldNotCreateAUserThatHasBeenAlreadyCreatedEarlier() throws InterruptedException {
        UserCreate userCreate = UserCreateFieldsGenerator.userDefault();
        ValidatableResponse responseCreate = step.create(userCreate);
        responseCreate.assertThat().statusCode(HttpStatus.SC_FORBIDDEN).extract().path("Forbidden");
    }

    @Test
    @DisplayName("Тест на создание пользователя, если нет одного из полей.")
    @Description("Успешный возврат статус-кода 403 Forbidden в соответствии с документацией при создании пользователя, если нет одного из полей.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldNotCreateAUserWithoutOneRequiredField() {
        UserCreate userCreate = UserCreateFieldsGenerator.passingGeneratorDataWithoutOneRequiredField();
        ValidatableResponse responseCreate = step.create(userCreate);
        responseCreate.assertThat().statusCode(HttpStatus.SC_FORBIDDEN).extract().path("Forbidden");
    }

    @After
    @DisplayName("Удаление пользователя.")
    public void tearDown() {
            step.deleteUser();
    }
}