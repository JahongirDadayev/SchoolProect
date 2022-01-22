package com.example.restfulproject.repository;

import com.example.restfulproject.entity.DbAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<DbAddress, Integer> {
}
