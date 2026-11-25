package com.habitflow.Runner;

import com.habitflow.Entity.Habit;
import com.habitflow.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HabitPersistenceDemo implements CommandLineRunner {

    private final HabitRepository habitRepository;

    public HabitPersistenceDemo(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        /*
        Habit habit = new Habit("Read a Book","Read for 30 minutes everyday");

        Habit savedHabit = habitRepository.save(habit);
        System.out.println("Saved Id : " + savedHabit.getId());

        Optional<Habit> result = habitRepository.findById(savedHabit.getId());
        System.out.println(result);

        result.ifPresent(found->{
            System.out.println(found.getId());
            System.out.println(found.getName());
            System.out.println(found.getDescription());
        });
*/
    }
}
