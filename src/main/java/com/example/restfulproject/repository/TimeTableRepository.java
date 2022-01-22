package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<DbTimeTable, Integer> {
}
