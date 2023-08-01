package userLoginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userData.User;
import userData.UserRandomData;
import userData.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void loginUserTest() {
        response = userSteps.userLoginToken(user, accessToken);
        response
                .then().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест на вход в систему с неверным логином и паролем.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/5c87a15a-37d9-4d06-8e7d-3ebe49aba2fb/sprints/72940/topics/7ec6ef07-a3d5-4923-a8ac-64313ac438e1/lessons/311b7751-0b28-438a-adb4-732ca7080912/")
    public void wouldNotLogAUser() {
        String email = user.getEmail();
        user.setEmail("wrong@email.ru");
        String password = user.getPassword();
        user.setPassword("wrongPassword");
        response = userSteps.userLoginToken(user, accessToken);
        user.setEmail(email);
        user.setPassword(password);
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