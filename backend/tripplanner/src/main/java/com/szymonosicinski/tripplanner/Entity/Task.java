package com.szymonosicinski.tripplanner.Entity;

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
@Table(name="tasks",schema="public")
public class Task {
    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Length(max=200)
    private String name;

    @NotNull
    @Column(name="finish")
    private boolean finish;

    @Column(name="deadline")
    private Date deadline;

    @Column(name="finishdate")
    private Date finishDate;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_trip", referencedColumnName = "id")
    @JsonIgnore
    private Trip trip;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User.class)
    @JoinColumn(name="id_user", referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
