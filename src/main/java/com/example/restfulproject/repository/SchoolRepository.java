package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbSchool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<DbSchool, Integer> {
}
