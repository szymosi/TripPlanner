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
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Trip trip=tripService.getById(tripId, currentUser);
        return trip.getBudget();
    }

    public Budget update(float founds, UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Budget budget=tripService.getByIdAsOrganizer(tripId,currentUser).getBudget();
        budget.setFounds(founds);
        budgetRepository.save(budget);
        return budget;
    }

    public Expense addExpense(ExpenseDTO expenseDTO, UUID tripId, UserPrincipal currentUser) {
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Budget budget = tripService.getByIdAsOrganizer(tripId, currentUser).getBudget();
        Expense expense=modelMapper.map(expenseDTO, Expense.class);
        expense.setBudget(budget);
        expenseRepository.save(expense);
        return expense;
    }

    public Expense getExpenseAsOrganizer(UUID expenseId, UUID tripId, UserPrincipal currentUser){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Budget budget = tripService.getByIdAsOrganizer(tripId, currentUser).getBudget();
        Expense expense=expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!budget.getExpenses().contains(expense))
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        return expense;
    }

    public Expense removeExpense(UUID expenseId, UUID tripId, UserPrincipal currentUser){
        Expense expense=getExpenseAsOrganizer(expenseId, tripId, currentUser);
        expenseRepository.delete(expense);
        return  expense;
    }

    public Expense updateExpense(UUID expenseId, ExpenseDTO expenseDTO, UUID tripId, UserPrincipal currentUser){
        Expense expense=getExpenseAsOrganizer(expenseId, tripId, currentUser);
        modelMapper.map(expenseDTO, expense);
        expenseRepository.save(expense);
        return  expense;
    }
}
