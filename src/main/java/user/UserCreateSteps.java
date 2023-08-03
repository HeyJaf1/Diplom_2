//package user;
//
//import io.qameta.allure.Step;
//import io.restassured.http.ContentType;
//import io.restassured.response.ValidatableResponse;
//import io.restassured.specification.RequestSpecification;
//import jdk.jfr.Description;
//import login.UserLogin;
//import login.UserLoginSteps;
//
//
//import static io.restassured.RestAssured.given;
//
//public class UserCreateSteps {
//    UserLoginSteps userLoginSteps = new UserLoginSteps();
//    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
//    protected final String USER_CREATE_URI = BASE_URI + "/api/auth/register";
//    protected final String DELETE_USER = BASE_URI + "/api/auth/user";
//
//    @Description("Создание спецификации, общее для всех @steps.")
//    private RequestSpecification getSpec() {
//        return given().log().all()
//                .contentType(ContentType.JSON)
//                .baseUri(BASE_URI);
//    }
//}