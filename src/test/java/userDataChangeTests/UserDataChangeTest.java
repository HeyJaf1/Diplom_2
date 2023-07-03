package userDataChangeTests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import login.Generator;
import login.UserCredentials;
import login.UserLogin;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userData.UserDataChangeSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDataChangeTest {

    private UserDataChangeSteps step;
    private UserLogin user;
    private String accessToken;

    @Before
    @Step("Создание объектов для проведения тестов.")
    public void setUp() {
       // step = new UserDataChangeSteps();

        user = Generator.getRandomUser();
        step = new UserDataChangeSteps();
        step.createUser(user);
        Response loginResponse = step.login(UserCredentials.from(user));
        accessToken = loginResponse.body().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Тест на возможность изменения email пользователя.")
    @Description("Успешное изменение в системе email пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldChangeEmail() {
        UserLogin updatEmailUser = new UserLogin(Generator.getRandomUser().getEmail(), user.getPassword(), user.getName());
        Response UpdateUserResponse = step.updateUser(updatEmailUser, accessToken);
        int statusCode = UpdateUserResponse.getStatusCode();
        assertEquals(SC_OK, statusCode);
        boolean isUpdateUserResponseSuccess = UpdateUserResponse.jsonPath().getBoolean("success");
        assertTrue(isUpdateUserResponseSuccess);
        String email = UpdateUserResponse.jsonPath().getString("user.email");
        assertEquals(updatEmailUser.getEmail().toLowerCase(), email);

    }

    @Test
    @DisplayName("Тест на возможность изменения password пользователя.")
    @Description("Успешное изменение в системе password пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldChangePassword() {
        ValidatableResponse responsePasswordChanged = step.changingUserPassword();
        responsePasswordChanged.assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест на возможность изменения name пользователя.")
    @Description("Успешное изменение в системе name пользователя с проверкой возвращаемого статус-кода 200 ОК в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldChangeName() {
        ValidatableResponse responseNameChanged = step.changingUserName();
        responseNameChanged.assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест на невозможность изменения email у существующего, но НЕ авторизованного пользователя.")
    @Description("Нельзя изменить в системе email у существующего, но НЕ авторизованного пользователя с проверкой возвращаемого статус-кода 401 Unauthorized в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    @Issue("Bug report link")
    public void wouldChangeEmailNonAuthorizedUser() {
        ValidatableResponse responseEmailChanged = step.changingNotAuthorizedUserEmail();
        responseEmailChanged.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Тест на невозможность изменения password у существующего, но НЕ авторизованного пользователя.")
    @Description("Нельзя изменить в системе password у существующего, но НЕ авторизованного пользователя с проверкой возвращаемого статус-кода 401 Unauthorized в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    @Issue("Bug report link")
    public void wouldChangePasswordNonAuthorizedUser() {
        ValidatableResponse responseEmailChanged = step.changingNotAuthorizedUserPassword();
        responseEmailChanged.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Тест на невозможность изменения name у существующего, но НЕ авторизованного пользователя.")
    @Description("Нельзя изменить в системе password у существующего, но НЕ авторизованного пользователя с проверкой возвращаемого статус-кода 401 Unauthorized в соответствии с документацией.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    @Issue("Bug report link")
    public void wouldChangeNameNonAuthorizedUser() {
        ValidatableResponse responseEmailChanged = step.changingNotAuthorizedUserName();
        responseEmailChanged.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @After
    @DisplayName("Удаление пользователя.")
    public void tearDown() {
        step.deleteUser(accessToken);
    }
}