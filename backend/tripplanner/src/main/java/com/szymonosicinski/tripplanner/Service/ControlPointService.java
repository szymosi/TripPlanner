package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.ControlPointDTO.CreateControlPointDTO;
import com.szymonosicinski.tripplanner.Entity.ControlPoint;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.ControlPointRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ControlPointService {

    @Autowired
    ControlPointRepository controlPointRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TripService tripService;

    public ControlPoint addControlPoint(CreateControlPointDTO createControlPointDTO, UUID tripId, UserPrincipal currentUser){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getByIdAsOrganizer(tripId,currentUser);
        ControlPoint controlPoint=controlPointConverter(createControlPointDTO);
        controlPoint.setOrder(trip.getControlPoints().size());
        controlPoint.setTrip(trip);
        trip.getControlPoints().add(controlPoint);
        controlPointRepository.save(controlPoint);
        return controlPoint;
    }

    public Page<ControlPoint> getControlPoints(UUID tripId, UserPrincipal currentUser, Pageable pageable){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        tripService.getById(tripId, currentUser);
        return controlPointRepository.findAllByTrip_IdOrderByOrderAsc(tripId, pageable);
    }

    public ControlPoint removeControlPoint(UUID tripId, UUID controlPointId, UserPrincipal currentUser){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip = tripService.getByIdAsOrganizer(tripId,currentUser);
        ControlPoint controlPoint = controlPointRepository.getOne(controlPointId);
        trip.getControlPoints().remove(controlPoint);
        controlPointRepository.delete(controlPoint);
        return controlPoint;
    }

    public Page<ControlPoint> changeOrder(UUID tripId, UUID controlPointId, int newPosition, UserPrincipal currentUser, Pageable pageable){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip = tripService.getByIdAsOrganizer(tripId,currentUser);
        List<ControlPoint> controlPoints = trip.getControlPoints();
        if(newPosition<0 || newPosition>controlPoints.size())
            throw new RuntimeException(ExceptionMessage.INPUT_ERROR.toString());

        ControlPoint controlPoint=controlPointRepository.getOne(controlPointId);


        //change order of neighborhood points
        for (ControlPoint point: controlPoints) {
            if(controlPoint.getOrder()<newPosition)
                if(point.getOrder()>controlPoint.getOrder() && point.getOrder()<=newPosition) {
                    point.setOrder(point.getOrder()-1);
                    controlPointRepository.save(point);
                }
            if(controlPoint.getOrder()>newPosition)
                if(point.getOrder()<controlPoint.getOrder() && point.getOrder()>=newPosition) {
                    point.setOrder(point.getOrder()+1);
                    controlPointRepository.save(point);
                }
        }
        controlPoint.setOrder(newPosition);
        controlPointRepository.save(controlPoint);

        controlPoints = controlPointRepository.findAllByTrip_IdOrderByOrderAsc(tripId);
        trip.setControlPoints(controlPoints);
        return controlPointRepository.findAllByTrip_IdOrderByOrderAsc(tripId, pageable);
    }

    private ControlPoint controlPointConverter(CreateControlPointDTO createControlPointDTO){
        ControlPoint controlPoint=new ControlPoint();

        controlPoint.setName(createControlPointDTO.getName());

        Pattern latitudePattern = Pattern.compile("([0-9]{1,2}.[0-9]+)([NS])");
        Pattern longitudePattern = Pattern.compile("([0-9]{1,3}.[0-9]+)([EW])");

        Matcher latitudeMatcher = latitudePattern.matcher(createControlPointDTO.getLatitude());
        Matcher longitudeMatcher = longitudePattern.matcher(createControlPointDTO.getLongitude());

        if(latitudeMatcher.matches() && longitudeMatcher.matches()){
            controlPoint.setLatitude(Double.valueOf(latitudeMatcher.group(1)));
            controlPoint.setNorthing(latitudeMatcher.group(2).equals("N"));

            controlPoint.setLongitude(Double.valueOf(longitudeMatcher.group(1)));
            controlPoint.setEasting(longitudeMatcher.group(2).equals("E"));

        }

        return controlPoint;
    }
}
