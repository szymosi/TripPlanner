package com.szymonosicinski.tripplanner.DTO.BlogDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szymonosicinski.tripplanner.Entity.Blog;
import com.szymonosicinski.tripplanner.Entity.BlogEntry;
import com.szymonosicinski.tripplanner.Entity.Trip;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class BlogDTO {
    public static enum Visibility{guests, users, participants};

    @NotNull
    @Length(max=50)
    private String name;

    @NotNull
    @Length(max=5000)
    private String description;

    @NotNull
    private Visibility visibility;
}
