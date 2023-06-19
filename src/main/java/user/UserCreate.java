package user;

public class UserCreate {

    private String email;
    private String password;
    private String name;

    public UserCreate() {}

    public UserCreate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserCreate setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCreate setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserCreate setName(String name) {
        this.name = name;
        return this;
    }
}