package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.BudgetDTO.ExpenseDTO;
import com.szymonosicinski.tripplanner.Entity.Budget;
import com.szymonosicinski.tripplanner.Entity.Expense;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.BudgetRepository;
import com.szymonosicinski.tripplanner.Repository.ExpenseRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TripService tripService;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    public Budget getByTripId(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getById(tripId, currentUser);
        return trip.getBudget();
    }

    public Budget update(float founds, UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Budget budget=tripService.getByIdAsOrganizer(tripId,currentUser).getBudget();
        budget.setFounds(founds);
        budgetRepository.save(budget);
        return budget;
    }

    public Expense addExpense(ExpenseDTO expenseDTO, UUID parentExpenseId, UUID tripId, UserPrincipal currentUser) {
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Budget budget = tripService.getByIdAsOrganizer(tripId, currentUser).getBudget();
        Expense parent=getExpenseAsOrganizer(parentExpenseId, tripId, currentUser);
        Expense expense=modelMapper.map(expenseDTO, Expense.class);
        expense.setBudget(budget);
        expense.setParentExpense(parent);
        parent.getChildren().add(expense);
        expenseRepository.save(expense);
        expense.getParentExpense().updateCost(expenseRepository);
        return expense;
    }

    public Expense getExpenseAsOrganizer(UUID expenseId, UUID tripId, UserPrincipal currentUser){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Budget budget = tripService.getByIdAsOrganizer(tripId, currentUser).getBudget();
        Expense expense=expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));

        Expense parentexpense=expense;
        while(parentexpense.getParentExpense()!=null)
            parentexpense=parentexpense.getParentExpense();

        if(!parentexpense.getBudget().getId().equals(budget.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());

        return expense;
    }

    public Expense removeExpense(UUID expenseId, UUID tripId, UserPrincipal currentUser){
        Expense expense=getExpenseAsOrganizer(expenseId, tripId, currentUser);
        if(expense.getParentExpense()==null)
            throw new RuntimeException(ExceptionMessage.COST_ROOT.toString());
        expenseRepository.delete(expense);
        expense.getParentExpense().getChildren().remove(expense);
        expense.getParentExpense().updateCost(expenseRepository);
        return  expense;
    }

    public Expense updateExpense(UUID expenseId, ExpenseDTO expenseDTO, UUID tripId, UserPrincipal currentUser){
        Expense expense=getExpenseAsOrganizer(expenseId, tripId, currentUser);
        modelMapper.map(expenseDTO, expense);
        expenseRepository.save(expense);

        expense.getParentExpense().updateCost(expenseRepository);
        return  expense;
    }
}
