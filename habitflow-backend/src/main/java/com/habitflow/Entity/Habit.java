package com.habitflow.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Habit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="habit_name",nullable = false,length = 150)
    private String name;

    @Column(name="description",length = 500)
    private String description;


    public Habit(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
