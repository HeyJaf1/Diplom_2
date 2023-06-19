package userData;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Random;

public class UserDataChangeGenerator {

    static final String[] emails = {
            "@yandex.ru",
            "@gmail.com",
            "@yahoo.com",
            "@mail.ru",
            "@postbox.com"
    };

    public static String email() {
        return RandomStringUtils.random(new Random().nextInt(10) + 1, true, false).toLowerCase() + emails[new Random().nextInt(emails.length)];
    }

    public static String password() {
        int max = 12;
        int min = 6;
        return RandomStringUtils.random(new SecureRandom().nextInt(max - min) + min, false, true);
    }

    public static String name() {
        return RandomStringUtils.random(new Random().nextInt(10) + 1, true, false);
    }

    public static UserDataChange passingGeneratorData() {
        return new UserDataChange().setEmail(email()).setPassword(password()).setName(name());
    }

    public static UserDataChange passingGeneratorNewEmail() {
        return new UserDataChange().setEmail(email());
    }

    public static UserDataChange passingGeneratorNewPassword() {
        return new UserDataChange().setPassword(password());
    }

    public static UserDataChange passingGeneratorNewName() {
        return new UserDataChange().setName(name());
    }
}