package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@Entity()
@Table(name="userr",schema="public")
public class User implements Serializable {
    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column(name="username",unique = true)
    private String username;

    @NotNull
    @Length(max=60)
    @Column(name="passwordhash")
    @JsonIgnore
    private String password;

    @Length(max=50)
    @Column(name="name")
    private String name;

    @Length(max=50)
    @Column(name="surname")
    private String surname;

    public User(){
        username="";
        password="";
        name="";
        surname="";
    }

    @Override
    public String toString(){
        return username;
    }
}
