package userData;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import login.UserLogin;
import login.UserLoginSteps;

import static io.restassured.RestAssured.given;

public class UserDataChangeSteps {

    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String USER_CREATE_URI = BASE_URI + "/api/auth/register";
    protected final String USER_LOGIN_URI = BASE_URI + "/api/auth/login";
    protected final String PATCH_CHANGE_USER_DATA = BASE_URI + "/api/auth/user";
    protected final String DELETE_USER = BASE_URI + "/api/auth/user";

    UserLoginSteps userLoginSteps = new UserLoginSteps();

    @Description("Создание спецификации, общее для всех @steps.")
    private RequestSpecification getSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }

    @Step("Создание нового пользователя.")
    public ValidatableResponse create(UserDataChange userDataChange) {
        return getSpec()
                .body(userDataChange)
                .when()
                .post(USER_CREATE_URI)
                .then().log().all();
    }

    @Step("Вход созданного пользователя в систему.")
    public ValidatableResponse logging() {
        UserDataChange userDataChange = UserDataChangeGenerator.passingGeneratorData();
        ValidatableResponse responseCreate = create(userDataChange);
        String accessToken = responseCreate.extract().path("accessToken");
        return getSpec()
                .auth().oauth2(accessToken)
                .and()
                .body(userDataChange)
                .when()
                .post(USER_LOGIN_URI)
                .then()
                .log()
                .all();
    }

    @Step("Изменение email пользователя.")
    public ValidatableResponse changingUserEmail() {
        ValidatableResponse responseLogging = logging();
        String accessToken = responseLogging.extract().path("accessToken");

        StringBuilder stringBuilder = new StringBuilder(accessToken);
        stringBuilder.replace(0, 7, "");
        String modifiedAccessToken = stringBuilder.toString();

        return getSpec()
                .auth().oauth2(modifiedAccessToken)
                .and()
                .body(UserDataChangeGenerator.passingGeneratorNewEmail())
                .when()
                .patch(PATCH_CHANGE_USER_DATA)
                .then()
                .log()
                .all();
    }

    @Step("Изменение password пользователя.")
    public ValidatableResponse changingUserPassword() {
        ValidatableResponse responseLogging = logging();
        String accessToken = responseLogging.extract().path("accessToken");

        StringBuilder stringBuilder = new StringBuilder(accessToken);
        stringBuilder.replace(0, 7, "");
        String modifiedAccessToken = stringBuilder.toString();

        return getSpec()
                .auth().oauth2(modifiedAccessToken)
                .and()
                .body(UserDataChangeGenerator.passingGeneratorNewPassword())
                .when()
                .patch(PATCH_CHANGE_USER_DATA)
                .then()
                .log()
                .all();
    }

    @Step("Изменение name пользователя.")
    public ValidatableResponse changingUserName() {
        ValidatableResponse responseLogging = logging();
        String accessToken = responseLogging.extract().path("accessToken");

        StringBuilder stringBuilder = new StringBuilder(accessToken);
        stringBuilder.replace(0, 7, "");
        String modifiedAccessToken = stringBuilder.toString();

        return getSpec()
                .auth().oauth2(modifiedAccessToken)
                .and()
                .body(UserDataChangeGenerator.passingGeneratorNewName())
                .when()
                .patch(PATCH_CHANGE_USER_DATA)
                .then()
                .log()
                .all();
    }

    // Шаги для изменения данных НЕавторизованного пользователя. То есть, пользователь существует, но не авторизован в системе.
    @Step("Изменение email существующего пользователя без входа в систему.")
    public ValidatableResponse changingNotAuthorizedUserEmail() {
        UserDataChange userDataChange = UserDataChangeGenerator.passingGeneratorData();
        ValidatableResponse responseCreate = create(userDataChange);


        return getSpec()
                .body(UserDataChangeGenerator.passingGeneratorNewEmail())
                .when()
                .patch(PATCH_CHANGE_USER_DATA)
                .then()
                .log()
                .all();
    }

    @Step("Изменение password существующего пользователя без входа в систему.")
    public ValidatableResponse changingNotAuthorizedUserPassword() {
        UserDataChange userDataChange = UserDataChangeGenerator.passingGeneratorData();
        create(userDataChange);

        return getSpec()
                .body(UserDataChangeGenerator.passingGeneratorNewPassword())
                .when()
                .patch(PATCH_CHANGE_USER_DATA)
                .then()
                .log()
                .all();
    }

    @Step("Изменение name существующего пользователя без входа в систему.")
    public ValidatableResponse changingNotAuthorizedUserName() {
        UserDataChange userDataChange = UserDataChangeGenerator.passingGeneratorData();
        create(userDataChange);

        return getSpec()
                .body(UserDataChangeGenerator.passingGeneratorNewName())
                .when()
                .patch(PATCH_CHANGE_USER_DATA)
                .then()
                .log()
                .all();
    }

    @Step("Удаление пользователя.")
    public ValidatableResponse deleteUser() {
        ValidatableResponse responseCreate = userLoginSteps.logging(new UserLogin());

        StringBuilder stringBuilder = new StringBuilder(responseCreate.extract().path("accessToken"));
        stringBuilder.replace(0, 7, "");
        String modifiedAccessToken = stringBuilder.toString();

        return given().log().all()
                .spec(getSpec())
                .auth().oauth2(modifiedAccessToken)
                .when()
                .delete(DELETE_USER)
                .then();
    }
}