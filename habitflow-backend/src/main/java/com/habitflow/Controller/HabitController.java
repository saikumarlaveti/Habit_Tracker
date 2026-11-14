package com.habitflow.Controller;

import com.habitflow.Service.HabitService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habit")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService serviceObj) {
        this.habitService = serviceObj;
    }


    @GetMapping("/welcome")
    public String welcome(){
        System.out.println("Habit Controller class Reached");
        return habitService.getWelcomeMessage();
    }

    @GetMapping("/status")
    public String habitStatus(){
        return "Habit Flow is Running";
    }

}
