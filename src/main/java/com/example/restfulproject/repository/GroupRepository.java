package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<DbGroup, Integer> {
}
