package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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

    @OneToOne(fetch=FetchType.EAGER, mappedBy = "expense", targetEntity = Budget.class)
    @JsonIgnore
    private Budget budget;

    @ManyToOne
    @JoinColumn(name="expense_id")
    @JsonIgnore
    private Expense parentExpense;

    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER, targetEntity = Expense.class)
    private List<Expense> childExpenses=new ArrayList<>();
}
