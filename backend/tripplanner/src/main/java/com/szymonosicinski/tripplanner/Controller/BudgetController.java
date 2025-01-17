package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.BudgetDTO.ExpenseDTO;
import com.szymonosicinski.tripplanner.Entity.Expense;
import com.szymonosicinski.tripplanner.Service.BudgetService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/{tripId}/Budget")
public class BudgetController {
    @Autowired
    BudgetService budgetService;

    @GetMapping()
    public ResponseEntity getBudget(@PathVariable("tripId") final UUID tripId,
                                    @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(budgetService.getByTripId(tripId,currentUser), HttpStatus.OK);
    }

    @PostMapping("/BudgetUpdate")
    public ResponseEntity update(@RequestParam(value = "founds", defaultValue = "0") final int founds,
                                 @PathVariable("tripId") final UUID tripId,
                                 @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(budgetService.update(founds,tripId,currentUser),HttpStatus.OK);
    }

    @PutMapping("/ExpenseAdd")
    public ResponseEntity addExpense(@RequestBody @Valid final ExpenseDTO expenseDTO,
                                     @RequestParam(value = "parentExpense") final UUID parentExpenseId,
                                     @PathVariable("tripId") final UUID tripId,
                                     @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(budgetService.addExpense(expenseDTO, parentExpenseId, tripId, currentUser),HttpStatus.CREATED);
    }

    @DeleteMapping("/ExpenseDelete")
    public ResponseEntity removeExpense(@RequestParam(value = "expenseId") final UUID expenseId,
                                 @PathVariable("tripId") final UUID tripId,
                                 @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(budgetService.removeExpense(expenseId,tripId,currentUser),HttpStatus.OK);
    }

    @PostMapping("/ExpenseUpdate")
    public ResponseEntity updateExpense(@RequestParam(value = "expenseId") final UUID expenseId,
                                        @RequestBody @Valid final ExpenseDTO expenseDTO,
                                        @PathVariable("tripId") final UUID tripId,
                                        @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(budgetService.updateExpense(expenseId,expenseDTO,tripId,currentUser),HttpStatus.OK);
    }
}
