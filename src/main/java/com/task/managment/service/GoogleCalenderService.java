package com.task.managment.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.task.managment.jobs.TaskProcessRunnable;
import com.task.managment.model.Task;

import com.google.api.services.calendar.Calendar;

@Component
@Slf4j
public class GoogleCalenderService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static Calendar client;


    public void queueTask(Task task) {
        executor.execute(new TaskProcessRunnable(task));
        log.info("Queued file " + task);
    }

}