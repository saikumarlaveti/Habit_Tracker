package com.habitflow.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class HabitService {


    public String getWelcomeMessage(){
        System.out.println("Reached Service Class");
        return "WELCOME TO THE HABIT TRACKER APP-Service Class";
    }
}
