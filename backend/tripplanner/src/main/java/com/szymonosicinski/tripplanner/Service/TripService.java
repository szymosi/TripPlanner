package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.TripDTO.TripCreateDTO;
import com.szymonosicinski.tripplanner.DTO.UserDTO.UserDTO;
import com.szymonosicinski.tripplanner.Entity.Budget;
import com.szymonosicinski.tripplanner.Entity.Expense;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.BudgetRepository;
import com.szymonosicinski.tripplanner.Repository.ExpenseRepository;
import com.szymonosicinski.tripplanner.Repository.TripRepository;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Service
public class TripService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TripRepository tripRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    BudgetRepository budgetRepository;

    public Trip create(TripCreateDTO tripCreateDTO, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip =modelMapper.map(tripCreateDTO,Trip.class);
        trip.setOrganizer(currentUser.getId());
        tripRepository.save(trip);
        internalEntities(trip);
        return trip;
    }

    public Trip update(UUID tripId, TripCreateDTO tripCreateDTO, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        modelMapper.map(tripCreateDTO,trip);
        tripRepository.save(trip);
        return trip;
    }

    public Trip delete(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        tripRepository.delete(trip);
        return trip;
    }

    public Trip getById(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!(trip.getOrganizer().equals(currentUser.getId())||
                isParticipant(tripId,currentUser)))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        return trip;
    }

    public Trip getByIdAsOrganizer(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        return trip;
    }

    public Page<Trip> getByOrganizer(UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        return tripRepository.findAllByOrganizer(currentUser.getId(),pageable);
    }

    public Page<Trip> getByParticipant(UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        return tripRepository.findAllByParticipant(currentUser.getId(), pageable);
    }

    public String addParticipant(UUID tripId, String participant, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        User user = userRepository.findByUsername(participant)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(isParticipant(tripId, user))
            throw new RuntimeException(ExceptionMessage.IS_PARTICIPANT.toString());
        try {
            tripRepository.saveParticipant(tripId,user.getId());
        } catch (Exception e){
            return "Participant could not be added";
        }
        return "Participant added succesfuly";
    }

    public String deleteParticipant(UUID tripId, UUID participantId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        int rowsDeleted=0;
        try {
            rowsDeleted=tripRepository.deleteParticipant(tripId, participantId);
        } catch (Exception e) {
            return "Participant could not be removed";
        }
        if(rowsDeleted==1)
            return "Participant removed succesfuly";
        else
            return "User is not participant";
    }

    public Page<UserDTO> getParticipants(UUID tripId, Pageable pageable, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=getById(tripId,currentUser);
        Page<User> users = userRepository.findAllParticipantsByTrip(trip.getId(), pageable);
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        for (User user:users) {
            userDTOs.add(modelMapper.map(user,UserDTO.class));
        }
        return new PageImpl<UserDTO>(userDTOs);
    }

    public boolean isParticipant(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        if(tripRepository.isParticipant(tripId, currentUser.getId())==0)
            return false;
        else
            return true;
    }

    public boolean isParticipant(UUID tripId, User user){
        if(user==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        if(tripRepository.isParticipant(tripId, user.getId())==0)
            return false;
        else
            return true;
    }

    private void internalEntities(Trip trip){
        Expense expense=new Expense();
        expense.setName("Sum");
        expenseRepository.save(expense);

        Budget budget=new Budget();
        budget.setTrip(trip);
        trip.setBudget(budget);
        expense.setBudget(budget);
        budget.setExpense(expense);
        budgetRepository.save(budget);
    }
}
