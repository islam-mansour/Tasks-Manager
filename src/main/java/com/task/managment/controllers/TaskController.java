package com.task.managment.controllers;

import com.task.managment.exciptions.RecordNotFoundException;
import com.task.managment.model.Task;
import com.task.managment.service.GoogleCalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import com.task.managment.service.TaskService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    GoogleCalenderService googleCalenderService;

    @Autowired
    GoogleCalenderController googleCalenderController;

    @GetMapping
    public ResponseEntity<List<Task>> getAllEmployees() {
        List<Task> list = taskService.getAllTasks();

        return new ResponseEntity<List<Task>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        Task entity = taskService.getTaskById(id);

        return new ResponseEntity<Task>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) throws IOException {
        Task newTask = taskService.createTask(task);
        googleCalenderService.queueTask(task);
        return new ResponseEntity<Task>(task, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestBody Task task)
            throws RecordNotFoundException {
        Task updatedTask = taskService.updateTask(task);
        return new ResponseEntity<Task>(updatedTask, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTaskById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        taskService.deleteTaskById(id);
        return HttpStatus.OK;
    }
}
