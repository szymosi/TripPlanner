package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.TaskDTO.CreateTaskDTO;
import com.szymonosicinski.tripplanner.DTO.TaskDTO.TaskDTO;
import com.szymonosicinski.tripplanner.DTO.UserDTO.UserDTO;
import com.szymonosicinski.tripplanner.Entity.Task;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Exception.UserException;
import com.szymonosicinski.tripplanner.Repository.TaskRepository;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TripService tripService;
    @Autowired
    UserRepository userRepository;

    public Page<Task> getTasks(UUID tripId, UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getById(tripId, currentUser);
        Page<Task> tasks=taskRepository.findByTrip_IdOrderByDeadlineAsc(tripId,pageable);
        return tasks;
    }

    public Task getTaskById(UUID tripId, UUID taskId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getById(tripId, currentUser);
        return taskRepository.getOne(taskId);
    }

    public Task addTask(UUID tripId, CreateTaskDTO createTaskDTO, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        User user=userRepository.findByUsername(createTaskDTO.getUser())
                .orElseThrow(()->new UserException(ExceptionMessage.USER_NOT_EXIST.toString()));
        Trip trip=tripService.getByIdAsOrganizer(tripId, currentUser);
        if(!(tripService.isParticipant(tripId,user)||trip.getOrganizer().equals(user.getId())))
            throw new RuntimeException(ExceptionMessage.NOT_PARTICIPANT.toString());
        Task task=modelMapper.map(createTaskDTO,Task.class);
        task.setTrip(trip);
        task.setUser(user);
        taskRepository.save(task);
        return task;
    }

    public Task updateTask(UUID tripId, UUID taskID, CreateTaskDTO createTaskDTO, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getByIdAsOrganizer(tripId, currentUser);
        Task task=taskRepository.findById(taskID)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        User user=userRepository.findByUsername(createTaskDTO.getUser())
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!(tripService.isParticipant(tripId,user)||trip.getOrganizer().equals(user.getId())))
            throw new RuntimeException(ExceptionMessage.NOT_PARTICIPANT.toString());
        modelMapper.map(createTaskDTO,task);
        task.setUser(user);
        taskRepository.save(task);
        return task;
    }

    public Task deleteTask(UUID tripId, UUID taskID, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getByIdAsOrganizer(tripId, currentUser);
        Task task=taskRepository.findById(taskID)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        taskRepository.delete(task);
        return task;
    }

    public Task changeStatus(UUID tripId, UUID taskID, boolean status, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        Trip trip=tripService.getById(tripId, currentUser);
        Task task=taskRepository.findById(taskID)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!task.getUser().getId().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCESS_DENIED.toString());
        if(status) {
            task.setFinish(status);
            task.setFinishDate(new Date());
        }
        else if(!status) {
            task.setFinish(status);
            task.setFinishDate(null);

        }
        taskRepository.save(task);
        return task;
    }
}
