package userData;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import login.UserCredentials;
import login.UserLogin;
import login.UserLoginSteps;

import static io.restassured.RestAssured.given;
import static login.RestClient.getBaseSpecSettings;

public class UserDataChangeSteps {

    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String USER_CREATE_URI = BASE_URI + "/api/auth/register";
    protected final String USER_LOGIN_URI = BASE_URI + "/api/auth/login";
    protected final String PATCH_CHANGE_USER_DATA = BASE_URI + "/api/auth/user";
    protected final String DELETE_USER = BASE_URI + "/api/auth/user";
    private static final String UPDATE_OR_DELETE = "auth/user/";
    private static final String REGISTER = "auth/register/";
    private static final String LOGIN = "auth/login/";

    @Description("Создание спецификации, общее для всех @steps.")
    private RequestSpecification getSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }

    @Step("Создание пользователя")
    public Response createUser(UserLogin user) {
        return (Response) given()
                .spec(getBaseSpecSettings())
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .extract();
    }

    @Step("Логин пользователя")
    public static Response login(UserCredentials creds) {
        return (Response) given()
                .spec(getBaseSpecSettings())
                .body(creds)
                .when()
                .post(LOGIN)
                .then()
                .extract();
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(UserLogin user, String accessToken) {
        return (Response) given()
                .spec(getBaseSpecSettings())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(UPDATE_OR_DELETE)
                .then()
                .extract();
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
    public void deleteUser(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .spec(getBaseSpecSettings())
                .header("authorization", accessToken)
                .when()
                .delete(UPDATE_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(202)
                .extract()
                .path("ok");
    }
}