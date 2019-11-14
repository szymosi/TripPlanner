package com.szymonosicinski.tripplanner.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@Entity()
@Table(name="user",schema="public")
public class User implements Serializable {
    @Id
    @Column(name="id")
    UUID id;

    @Column(name="username")

    String username;

    @Column(name="passwordhash")
    String password;

    @Column(name="name")
    String name;

    @Column(name="surname")
    String surname;

    @Override
    public String toString(){
        return username;
    }
}
