package nl.hu.userservice.presentation.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;

    public UserLoginDTO() { }

    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
