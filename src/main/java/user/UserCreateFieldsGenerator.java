package user;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Random;

public class UserCreateFieldsGenerator {

    static final String[] emails = {
            "@yandex.ru", "@gmail.com", "@yahoo.com", "@mail.ru",
            "@ya.ru", "@hotbox.com", "@rambler.ru", "@list.ru", "@bk.ru", "@postbox.com"
    };

    public static String email() {
        return RandomStringUtils.random(new Random().nextInt(10) + 1, true, false)
                .toLowerCase() + emails[new Random().nextInt(emails.length)];
    }

    public static String password() {
        int max = 12;
        int min = 6;
        return RandomStringUtils.random(new SecureRandom().nextInt(max - min) + min, false, true);
    }

    public static String name() {
        return RandomStringUtils.random(new Random().nextInt(10) +1, true, false);
    }

    public static UserCreate userDefault() {
        return new UserCreate("NewerUser@yandex.ru", "12344321", "Newer");
    }

    public static UserCreate passingGeneratorData() {
        return new UserCreate().setEmail(email()).setPassword(password()).setName(name());
    }

    public static UserCreate passingGeneratorDataWithoutOneRequiredField() {
        return new UserCreate().setEmail(email()).setPassword(password());
    }
}