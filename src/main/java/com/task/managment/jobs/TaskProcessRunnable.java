package com.task.managment.jobs;

import ch.qos.logback.core.net.server.Client;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.task.managment.model.Task;
import com.task.managment.service.GoogleCalenderService;
import lombok.extern.slf4j.Slf4j;
import com.google.api.services.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

@Slf4j
public class TaskProcessRunnable implements Runnable {

    private final Task task;

    @Autowired
    GoogleCalenderService googleCalenderService;


    public TaskProcessRunnable(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            while(googleCalenderService.client == null || !isHostAvailable("google.com")){
                System.out.println("Still Waiting");
            }
            process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process() throws IOException {
        createEvents();
    }

    public void createEvents() throws IOException {
        Event event = new Event()
                .setSummary(task.getTitle())
                .setLocation(task.getLocation())
                .setDescription(task.getDescription());

        DateTime startDateTime = new DateTime(task.getStart().toString());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Africa/Cairo");

        event.setStart(start);

        DateTime endDateTime = new DateTime(task.getEnd().toString());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Africa/Cairo");

        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));


        String calendarId = "primary";
        event = googleCalenderService.client.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

    private static boolean isHostAvailable(String hostName) throws IOException
    {
        try(Socket socket = new Socket())
        {
            int port = 80;
            InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
            socket.connect(socketAddress, 3000);

            return true;
        }
        catch(UnknownHostException unknownHost)
        {
            return false;
        }
    }

}
