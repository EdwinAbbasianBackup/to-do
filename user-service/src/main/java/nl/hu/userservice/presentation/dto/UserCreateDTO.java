package nl.hu.userservice.presentation.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;

    public UserCreateDTO() { }

}
