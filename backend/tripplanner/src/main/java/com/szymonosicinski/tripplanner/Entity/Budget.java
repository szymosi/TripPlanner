package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name="budget",schema="public")
public class Budget {
    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column(name="founds")
    private float founds;

    @NotNull
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_trip", referencedColumnName = "id")
    @JsonIgnore
    private Trip trip;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JoinColumn(name="id_expense", referencedColumnName = "id")
    private Expense expense;
}
