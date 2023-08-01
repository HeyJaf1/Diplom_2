package userData;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import user.Constant;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(Constant.BASE_URL);
    }

    @Step("Регистрация нового пользователя")
    public Response userCreate(User user) {
        return requestSpec()
                .body(user)
                .post(Constant.CREATE_USER);
    }

    @Step("Изменение профиля пользователя")
    public Response userProfileChanging(User newUser, String accessToken) {
        return requestSpec()
                .header("Authorization", accessToken)
                .body(newUser)
                .patch(Constant.USER);
    }

    @Step("Авторизация пользователя по логину")
    public Response userLogin(User user) {
        return requestSpec()
                .body(user)
                .post(Constant.LOGIN_USER);
    }

    @Step("Авторизация пользователя по логину и токену")
    public Response userLoginToken(User user, String accessToken) {
        return requestSpec()
                .header("Authorization", accessToken)
                .body(user)
                .post(Constant.LOGIN_USER);
    }

    @Step("Удаление профиля пользователя")
    public void userDelete(String accessToken) {
        requestSpec()
                .header("Authorization", accessToken)
                .delete(Constant.USER);
    }
}