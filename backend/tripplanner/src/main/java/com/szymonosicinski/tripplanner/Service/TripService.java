package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.TripDTO.TripCreateDTO;
import com.szymonosicinski.tripplanner.Entity.Budget;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.TripRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TripRepository tripRepository;

    public Trip create(TripCreateDTO tripCreateDTO, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip =modelMapper.map(tripCreateDTO,Trip.class);
        trip.setOrganizer(currentUser.getId());
        internalEntities(trip);
        tripRepository.save(trip);
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

    public List<Trip> getByParticipant(UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        return tripRepository.findAllByParticipant(currentUser.getId());
    }

    public void addParticipant(UUID tripId, UUID participantId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        tripRepository.saveParticipant(tripId,participantId);
        return;
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
        Budget budget=new Budget();
        budget.setTrip(trip);
        trip.setBudget(budget);
    }
}
