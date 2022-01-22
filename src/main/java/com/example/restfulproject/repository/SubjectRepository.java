package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<DbSubject, Integer> {
}
