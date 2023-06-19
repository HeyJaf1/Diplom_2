package user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import login.UserLogin;
import login.UserLoginSteps;


import static io.restassured.RestAssured.given;

public class UserCreateSteps {
    UserLoginSteps userLoginSteps = new UserLoginSteps();
    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String USER_CREATE_URI = BASE_URI + "/api/auth/register";
    protected final String DELETE_USER = BASE_URI + "/api/auth/user";

    @Description("Создание спецификации, общее для всех @steps.")
    private RequestSpecification getSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }

    @Step("Создание нового пользователя.")
    public ValidatableResponse create(UserCreate userCreate) {
        return getSpec()
                .body(userCreate)
                .when()
                .post(USER_CREATE_URI)
                .then().log().all();
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