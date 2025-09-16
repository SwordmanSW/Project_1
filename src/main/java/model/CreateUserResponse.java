package model;

public class CreateUserResponse {

    private String accessToken;
    private String refreshToken;
    private boolean success;
    private User user;

    public String getAccessToken() {
        return accessToken;
    }

}
