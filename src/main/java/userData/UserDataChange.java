package userData;

public class UserDataChange {

    private String email;
    private String password;
    private String name;

    public UserDataChange() {}

    public UserDataChange setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDataChange setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDataChange setName(String name) {
        this.name = name;
        return this;
    }
}