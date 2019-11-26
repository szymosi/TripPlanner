package com.szymonosicinski.tripplanner.DTO.UserDTO;

import lombok.Getter;

import javax.validation.constraints.NotNull;


@Getter
public class LoginDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
