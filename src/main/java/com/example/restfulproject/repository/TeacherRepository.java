package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<DbTeacher, Integer> {
}
