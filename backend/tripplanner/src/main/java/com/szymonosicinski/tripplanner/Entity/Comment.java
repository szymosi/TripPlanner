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
@Table(name="comment",schema="public")
public class Comment {

    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column(name="date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    @NotNull
    @Length(max=500)
    @Column(name="text")
    private String text;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_user", referencedColumnName = "id")
    private User user;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_blogentry", referencedColumnName = "id")
    @JsonIgnore
    private BlogEntry blogEntry;

}
