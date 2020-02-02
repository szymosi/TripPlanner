package com.szymonosicinski.tripplanner.DTO.TaskDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import net.bytebuddy.implementation.bind.annotation.Default;
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

    @JsonFormat(pattern="yyyy-MM-dd")
    Date deadline;

    String user;
}
