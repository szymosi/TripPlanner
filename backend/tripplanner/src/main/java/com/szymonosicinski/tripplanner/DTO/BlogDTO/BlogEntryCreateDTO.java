package com.szymonosicinski.tripplanner.DTO.BlogDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szymonosicinski.tripplanner.Entity.Blog;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
public class BlogEntryCreateDTO {

    @NotNull
    @Length(max=50)
    private String name;

    @NotNull
    private String text;
}
