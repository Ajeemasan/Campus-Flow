
package com.campusflow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusflow.model.CampusTask;

public interface CampusTaskRepository extends JpaRepository<CampusTask, Long> {

    List<CampusTask> findByUser_IdOrderByDueDateAsc(Long userId);

    List<CampusTask> findByUser_IdAndCategoryIgnoreCaseOrderByDueDateAsc(
            Long userId, String category);

    List<CampusTask> findByDueDateAndCompletedFalse(LocalDate dueDate);
    List<CampusTask> findByDueDateAndCompletedFalseAndReminderSentFalse(
        LocalDate dueDate);
}
