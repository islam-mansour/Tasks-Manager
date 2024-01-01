package com.task.managment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name="TBL_TASKS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="title")
    private String Title;

    @Column(name="location")
    private String Location;

    @Column(name="description", nullable=false, length=200)
    private String Description;


    @Column(name="start", nullable=false, length=200)
    private LocalDateTime Start;

    @Column(name="end", nullable=false, length=200)
    private LocalDateTime End;



    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + Title + ", Description=" + Description + ", Location=" + Location +
                 ", start=" + Start.toString() + ", end=" + End.toString() + "]";
    }

}
