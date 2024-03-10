package ru.sber.codetasks.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterRequest {
    @Size(min = 3, max = 40, message = "Username must be from 3 to 40 characters long")
    @Pattern(regexp = "^\\w*$", message = "Username should contain characters, digits and underscore")
    private String username;

    @NotBlank
    @Size(min = 4, max = 40, message = "Password must be from 4 to 40 characters long")
    private String password;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
