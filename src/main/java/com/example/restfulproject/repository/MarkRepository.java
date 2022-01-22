package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkRepository extends JpaRepository<DbMark, Integer> {
}
