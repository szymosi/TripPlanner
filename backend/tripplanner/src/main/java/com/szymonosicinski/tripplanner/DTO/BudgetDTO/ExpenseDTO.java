package com.szymonosicinski.tripplanner.DTO.BudgetDTO;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
public class ExpenseDTO {

    @NotNull
    @Length(max = 50)
    private String name;

    @NotNull
    private float cost;


    private float actuualCost;
}
