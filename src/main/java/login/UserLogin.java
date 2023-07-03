package login;

import lombok.Data;

@Data
public class UserLogin {

    private String email;
    private String password;
    private String name;

    public UserLogin() {}

    public UserLogin(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserLogin setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserLogin setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserLogin setName(String name) {
        this.name = name;
        return this;
    }
}