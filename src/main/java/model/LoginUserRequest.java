package model;

public class LoginUserRequest {
    private final String email;
    private final String password;

    public LoginUserRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
