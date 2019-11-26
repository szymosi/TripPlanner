package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="controlpoint",schema="public")
public class ControlPoint {

    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Length(max=50)
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="latitude")
    private double latitude;

    @NotNull
    @Column(name="northing")
    private boolean northing;

    @NotNull
    @Column(name="longitude")
    private double longitude;

    @NotNull
    @Column(name="easting")
    private boolean easting;

    @NotNull
    @Column(name="visitorder")
    private int order;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_trip", referencedColumnName = "id")
    @JsonIgnore
    private Trip trip;
}
