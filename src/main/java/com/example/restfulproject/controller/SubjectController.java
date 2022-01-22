package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbSubject;
import com.example.restfulproject.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    @GetMapping
    public List<DbSubject> getSubjects() {
        try {
            return subjectRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @PostMapping
    public String postSubject(@RequestBody DbSubject dbSubject) {
        try {
            subjectRepository.save(dbSubject);
            return "Subject saved";
        } catch (Exception e) {
            return "Subject not saved.";
        }
    }

    @PutMapping(path = "/{id}")
    public String updateSubject(@RequestBody DbSubject dbSubject, @PathVariable Integer id) {
        Optional<DbSubject> optionalDbSubject = subjectRepository.findById(id);
        if (optionalDbSubject.isPresent()) {
            DbSubject updateDbSubject = optionalDbSubject.get();
            updateDbSubject.setName(dbSubject.getName());
            updateDbSubject.setClassNumber(dbSubject.getClassNumber());
            try {
                subjectRepository.save(updateDbSubject);
                return "Update subject information";
            } catch (Exception e) {
                return "Error database";
            }
        } else {
            return "Not updating subject";
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteSubject(@PathVariable Integer id) {
        try {
            subjectRepository.deleteById(id);
            return "Subject deleted";
        } catch (Exception e) {
            return "Subject not deleted";
        }
    }
}
