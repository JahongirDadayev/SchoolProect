package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbMark;
import com.example.restfulproject.entity.DbStudent;
import com.example.restfulproject.entity.DbSubject;
import com.example.restfulproject.payload.MarkDto;
import com.example.restfulproject.repository.MarkRepository;
import com.example.restfulproject.repository.StudentRepository;
import com.example.restfulproject.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/mark")
public class MarkController {
    @Autowired
    MarkRepository markRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @GetMapping
    public List<DbMark> getMarks() {
        return markRepository.findAll();
    }

    @PostMapping
    public String postMark(@RequestBody MarkDto markDto) {
        Optional<DbStudent> optionalDbStudent = studentRepository.findById(markDto.getStudentId());
        if (optionalDbStudent.isPresent()) {
            DbStudent dbStudent = optionalDbStudent.get();
            Optional<DbSubject> optionalDbSubject = subjectRepository.findById(markDto.getSubjectId());
            if (optionalDbSubject.isPresent()) {
                DbSubject dbSubject = optionalDbSubject.get();
                try {
                    markRepository.save(new DbMark(null, dbStudent, dbSubject, markDto.getMarkRating()));
                    return "Database saved mark information";
                } catch (Exception e) {
                    return "There is an error in the DbMark table";
                }
            } else {
                return "There is no subject matching the given id";
            }
        } else {
            return "There is no student matching the given id";
        }
    }

    @PutMapping(path = "/{id}")
    public String updateMark(@PathVariable Integer id, @RequestBody MarkDto markDto) {
        Optional<DbMark> optionalDbMark = markRepository.findById(id);
        if (optionalDbMark.isPresent()) {
            DbMark dbMark = optionalDbMark.get();
            Optional<DbStudent> optionalDbStudent = studentRepository.findById(markDto.getStudentId());
            if (optionalDbStudent.isPresent()) {
                DbStudent dbStudent = optionalDbStudent.get();
                Optional<DbSubject> optionalDbSubject = subjectRepository.findById(markDto.getSubjectId());
                if (optionalDbSubject.isPresent()) {
                    DbSubject dbSubject = optionalDbSubject.get();
                    dbMark.setDbStudent(dbStudent);
                    dbMark.setDbSubject(dbSubject);
                    dbMark.setMarkRating(markDto.getMarkRating());
                    try {
                        markRepository.save(dbMark);
                        return "Update mark information";
                    } catch (Exception e) {
                        return "There is an error in the DbMark table";
                    }
                } else {
                    return "There is no subject matching the given id";
                }
            } else {
                return "There is no student matching the given id";
            }
        } else {
            return "There is no mark information matching the given id";
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteMark(@PathVariable Integer id) {
        if (markRepository.existsById(id)) {
            try {
                markRepository.deleteById(id);
                return "Deleted mark information";
            } catch (Exception e) {
                return "There is an error in the dbMark table";
            }
        } else {
            return "There is no mark information matching the given id";
        }
    }
}
