package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<DbStudent, Integer> {
}
