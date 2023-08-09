package userData;

import io.qameta.allure.Step;
import net.datafaker.Faker;

public class UserRandomData {

    public static Faker faker = new Faker();

    @Step("Создание нового пользователя с рандомными данными")
    public static User createNewRandomUser() {
        return new User(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password());
    }
    @Step("Создание пользователя с рандомными данными без логина")
    public static User createRandomNoNameUser() {
        return new User(
                "",
                faker.internet().emailAddress(),
                faker.internet().password());
    }

}