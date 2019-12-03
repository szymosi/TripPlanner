package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.TaskDTO.CreateTaskDTO;
import com.szymonosicinski.tripplanner.Entity.Task;
import com.szymonosicinski.tripplanner.Service.TaskService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{tripId}/Task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping()
    public List<Task> getTasks(@PathVariable("tripId") final UUID tripId,
                               @CurrentUser final UserPrincipal currentUser){
        return taskService.getTasks(tripId,currentUser);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity getTaskById(@PathVariable("tripId") final UUID tripId,
                                      @PathVariable("taskId") final UUID taskId,
                                      @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(taskService.getTaskById(tripId,taskId,currentUser), HttpStatus.OK);
    }

    @PutMapping("/TaskAdd")
    public ResponseEntity addTask(@PathVariable("tripId") final UUID tripId,
                                  @RequestBody @Valid final CreateTaskDTO createTaskDTO,
                                  @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(taskService.addTask(tripId,createTaskDTO,currentUser),HttpStatus.CREATED);
    }

    @PostMapping("/TaskUpdate")
    public ResponseEntity updateTask(@PathVariable("tripId") final UUID tripId,
                                     @RequestParam(value = "taskId", defaultValue = "0") final UUID taskId,
                                  @RequestBody @Valid final CreateTaskDTO createTaskDTO,
                                  @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(taskService.updateTask(tripId,taskId,createTaskDTO,currentUser),HttpStatus.OK);
    }

    @DeleteMapping("/TaskDelete")
    public ResponseEntity deleteTask(@PathVariable("tripId") final UUID tripId,
                                     @RequestParam(value = "taskId", defaultValue = "0") final UUID taskId,
                                     @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(taskService.deleteTask(tripId,taskId,currentUser),HttpStatus.OK);
    }

    @PostMapping("/TaskStatus")
    public ResponseEntity changeStatus(@PathVariable("tripId") final UUID tripId,
                                       @RequestParam(value = "taskId", defaultValue = "0") final UUID taskId,
                                       @RequestParam(value = "status", defaultValue = "false") final boolean status,
                                       @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(taskService.changeStatus(tripId,taskId,status,currentUser),HttpStatus.OK);
    }
}
