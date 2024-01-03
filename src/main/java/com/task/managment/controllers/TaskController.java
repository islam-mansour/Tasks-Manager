package com.task.managment.controllers;

import com.task.managment.exciptions.RecordNotFoundException;
import com.task.managment.model.Task;
import com.task.managment.service.GoogleCalenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



import com.task.managment.service.TaskService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    GoogleCalenderService googleCalenderService;


    @GetMapping
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "index";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        Task entity = taskService.getTaskById(id);

        return new ResponseEntity<Task>(entity, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/add")
    public String showAddTaskForm(Task task) {
        return "add-task";
    }
    @PostMapping
    public String createTask(@Valid Task task, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()){
            return "add-task";
        }
        Task newTask = taskService.createTask(task);
        googleCalenderService.queueTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws RecordNotFoundException {
        Task task = taskService.getTaskById(id);

        model.addAttribute("task", task);
        return "update-task";
    }

    @PutMapping
    public String updateTask(@Valid Task task, BindingResult result, Model model)
            throws RecordNotFoundException {

        if (result.hasErrors()) {
            return "update-task";
        }

        Task updatedTask = taskService.updateTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTaskById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        taskService.deleteTaskById(id);
        return "redirect:/tasks";
    }
}
