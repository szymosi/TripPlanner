package com.szymonosicinski.tripplanner.Entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="username")
    private String username;

    @Column(name="passwordhash")
    private String password;

    @Column(name="name")
    private String name;

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
