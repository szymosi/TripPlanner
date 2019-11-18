package com.szymonosicinski.tripplanner.Entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="trip",schema="public")
public class Trip {
    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="descryption")
    private String descryption;

    @Column(name="startdate")
    private Date startDate;

    @Column(name="enddate")
    private Date endDate;

    @Column(name="organizer")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID organizer;
}
