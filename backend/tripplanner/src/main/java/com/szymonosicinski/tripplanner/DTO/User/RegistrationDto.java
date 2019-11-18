package com.szymonosicinski.tripplanner.DTO.User;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class RegistrationDto {

    @NotBlank
    @Length(max=50)
    private String username;

    @Setter
    @NotBlank
    @Length(min=8,max=60)
    private String password;

    @NotBlank
    @Length(min=8,max=50)
    private String passwordRepeat;


    @Length(max=50)
    private String name;

    @Length(max=50)
    private String surname;
}
