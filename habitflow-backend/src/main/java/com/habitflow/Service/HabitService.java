package com.habitflow.Service;

import com.habitflow.DTO.Request.HabitRequest;
import com.habitflow.DTO.Response.HabitResponse;
import com.habitflow.Entity.Habit;
import com.habitflow.repository.HabitRepository;
import org.springframework.stereotype.Service;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    //Method - 1 : create Habit
    public HabitResponse createHabit(HabitRequest request) {
        // 1. Convert incoming request data into an entity.
        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());

        // 2. Save the entity in the database.
        Habit savedHabit = habitRepository.save(habit);

        // 3. Convert the saved entity into the API response DTO.
        return new HabitResponse(
                savedHabit.getId(),
                savedHabit.getName(),
                savedHabit.getDescription()
        );
    }

    //It was Created for the Testing purpose
    public String getWelcomeMessage() {
        System.out.println("Reached Service Class");
        return "WELCOME TO THE HABIT TRACKER APP-Service Class";
    }
}
