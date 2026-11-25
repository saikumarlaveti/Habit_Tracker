package com.habitflow.DTO.Request;

import com.habitflow.Entity.Habit;
import com.habitflow.repository.HabitRepository;

public class HabitRequest {
    private String name;
    private String description;

    public HabitRequest(){

    }
    public HabitRequest(String name,String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
