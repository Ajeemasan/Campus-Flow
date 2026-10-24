package com.campusflow.repository;

import com.campusflow.model.CampusTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampusTaskRepository extends JpaRepository<CampusTask, Long> {
    List<CampusTask> findAllByOrderByDueDateAsc();
    List<CampusTask> findByCategoryIgnoreCaseOrderByDueDateAsc(String category);
}
