package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name="blogentry",schema = "public")
public class BlogEntry {

    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Length(max=50)
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="text")
    private String text;

    @NotNull
    @Column(name="date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_blog", referencedColumnName = "id")
    private Blog blog;
}
