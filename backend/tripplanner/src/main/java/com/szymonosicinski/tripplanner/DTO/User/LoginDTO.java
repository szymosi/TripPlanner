package com.szymonosicinski.tripplanner.DTO.User;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
public class LoginDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
