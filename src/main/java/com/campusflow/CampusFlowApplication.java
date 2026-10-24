package com.campusflow;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.campusflow.model.CampusTask;
import com.campusflow.repository.CampusTaskRepository;
@EnableScheduling
@SpringBootApplication
public class CampusFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusFlowApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDemoData(CampusTaskRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new CampusTask("Data Structures Assignment", "Complete linked-list implementation", "ASSIGNMENT", LocalDate.now().plusDays(3), "Ajeem Asan A"));
                repository.save(new CampusTask("Discrete Mathematics Internal", "Revise relations and graph theory", "EXAM", LocalDate.now().plusDays(6), "Syed Jaffar"));
                repository.save(new CampusTask("Department Symposium", "Register and prepare event materials", "EVENT", LocalDate.now().plusDays(8), "Kaviyarasan"));
                repository.save(new CampusTask("Library Management Project", "Finish database schema", "GROUP PROJECT", LocalDate.now().plusDays(5), "Vignesh"));
            }
        };
    }
}
