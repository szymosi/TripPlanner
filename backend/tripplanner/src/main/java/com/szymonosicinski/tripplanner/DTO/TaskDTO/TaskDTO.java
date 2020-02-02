package com.szymonosicinski.tripplanner.DTO.TaskDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class TaskDTO {
    private UUID id;

    private String name;

    private boolean finish;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date deadline;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date finishDate;

    private User user;
}
