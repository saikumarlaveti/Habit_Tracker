package com.habitflow.Controller;

import com.habitflow.DTO.Request.HabitRequest;
import com.habitflow.DTO.Response.HabitResponse;
import com.habitflow.Entity.Habit;
import com.habitflow.Service.HabitService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
public ResponseEntity<HabitResponse> createHabit(@RequestBody HabitRequest request){
        HabitResponse response = habitService.createHabit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
}


    @GetMapping("/status")
    public String habitStatus(){
        return "Habit Flow is Running";
    }

}
