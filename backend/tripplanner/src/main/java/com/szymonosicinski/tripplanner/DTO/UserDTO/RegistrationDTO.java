package com.szymonosicinski.tripplanner.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class RegistrationDTO {

    @NotBlank
    @Length(min=4, max=50)
    private String username;

    @Setter
    @NotBlank
    @Length(min=8,max=50)
    private String password;

    @NotBlank
    @Length(min=8,max=50)
    private String passwordRepeat;


    @Length(max=50)
    private String name;

    @Length(max=50)
    private String surname;
}
