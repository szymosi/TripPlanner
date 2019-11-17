package com.szymonosicinski.tripplanner.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {
    private UUID id;

    private String username;

    private String name;

    private String surname;
}