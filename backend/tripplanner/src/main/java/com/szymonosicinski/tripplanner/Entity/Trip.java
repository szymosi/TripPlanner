package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

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
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @NotNull
    @Column(name="enddate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    @NotNull
    @Column(name="organizer")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID organizer;

    @NotNull
    @OneToOne(mappedBy = "trip",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = Budget.class)
    @JsonIgnore
    private Budget budget;

    @OneToOne(mappedBy = "trip",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private Blog blog;

    @OneToMany(mappedBy = "trip",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = ControlPoint.class)
    @JsonIgnore
    private List<ControlPoint> controlPoints=new ArrayList<>();

    @OneToMany(mappedBy = "trip",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = Task.class)
    @JsonIgnore
    private Set<Task> tasks=new HashSet<>();
}
