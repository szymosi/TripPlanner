package com.szymonosicinski.tripplanner.DTO.TaskDTO;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
public class CreateTaskDTO {

    @NotNull
    @Length(max=200)
    String name;

    boolean finish;

    Date deadline;

    UUID user;
}
