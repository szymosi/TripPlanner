package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@Table(name="trip",schema="public")
public class Trip {
    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Length(max=100)
    @Column(name="name")
    private String name;

    @Length(max=5000)
    @Column(name="descryption")
    private String descryption;

    @NotNull
    @Column(name="startdate")
    private Date startDate;

    @NotNull
    @Column(name="enddate")
    private Date endDate;

    @NotNull
    @Column(name="organizer")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID organizer;

    @OneToOne(mappedBy = "trip",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Budget budget;

 /*   @OneToOne(mappedBy = "trip", fetch = FetchType.LAZY)
    @JsonIgnore
    private Blog blog;*/

   /* @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, targetEntity = ControlPoint.class)
    @JsonIgnore
    private Set<ControlPoint> controlPoints=new HashSet<>();*/

   /* @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, targetEntity = Task.class)
    @JsonIgnore
    private Set<Task> tasks=new HashSet<>();*/
}
