package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="expense",schema="public")
public class Expense {
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
    @Column(name="cost")
    private float cost;

    @Column(name="actualcost")
    private float actualCost;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_budget", referencedColumnName = "id")
    @JsonIgnore
    private Budget budget;
}
