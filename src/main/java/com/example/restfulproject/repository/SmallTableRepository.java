package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbSmallTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallTableRepository extends JpaRepository<DbSmallTable, Integer> {
}
