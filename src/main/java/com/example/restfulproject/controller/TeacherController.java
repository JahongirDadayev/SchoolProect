package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbAddress;
import com.example.restfulproject.entity.DbGroup;
import com.example.restfulproject.entity.DbTeacher;
import com.example.restfulproject.payload.TeacherDto;
import com.example.restfulproject.repository.AddressRepository;
import com.example.restfulproject.repository.GroupRepository;
import com.example.restfulproject.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping
    public List<DbTeacher> getTeachers() {
        try {
            return teacherRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @PostMapping
    public String postTeacher(@RequestBody TeacherDto teacherDto) {
        DbAddress dbAddress = new DbAddress(null, teacherDto.getCity(), teacherDto.getDistrict(), teacherDto.getStreet());
        List<DbGroup> dbGroupList = new ArrayList<>();
        for (Integer groupId : teacherDto.getGroups()) {
            if (groupRepository.existsById(groupId)) {
                Optional<DbGroup> optionalDbGroup = groupRepository.findById(groupId);
                dbGroupList.add(optionalDbGroup.get());
            } else {
                return "There is no group corresponding to the given id";
            }
        }
        try {
            DbAddress saveAddress = addressRepository.save(dbAddress);
            DbTeacher dbTeacher = new DbTeacher(null, teacherDto.getFirstName(), teacherDto.getLastName(), saveAddress, dbGroupList);
            try {
                teacherRepository.save(dbTeacher);
                return "Teacher information saved database";
            } catch (Exception e) {
                addressRepository.delete(saveAddress);
                return "There is an error in the teacher table";
            }
        } catch (Exception e) {
            return "There is an error in the address table";
        }
    }

    @PutMapping(path = "/{id}")
    public String updateTeacher(@PathVariable Integer id, @RequestBody TeacherDto teacherDto) {
        Optional<DbTeacher> optionalDbTeacher = teacherRepository.findById(id);
        List<DbGroup> dbGroupList = new ArrayList<>();
        if (optionalDbTeacher.isPresent()) {
            DbTeacher dbTeacher = optionalDbTeacher.get();
            for (Integer groupId : teacherDto.getGroups()) {
                if (groupRepository.existsById(groupId)) {
                    Optional<DbGroup> optionalDbGroup = groupRepository.findById(groupId);
                    dbGroupList.add(optionalDbGroup.get());
                } else {
                    return "There is no group corresponding to the given id";
                }
            }
            DbAddress oldDbAddress = dbTeacher.getDbAddress();
            DbAddress dbAddress = dbTeacher.getDbAddress();
            dbAddress.setCity(teacherDto.getCity());
            dbAddress.setDistrict(teacherDto.getDistrict());
            dbAddress.setStreet(teacherDto.getStreet());
            try {
                DbAddress saveAddress = addressRepository.save(dbAddress);
                dbTeacher.setFirstName(teacherDto.getFirstName());
                dbTeacher.setLastName(teacherDto.getLastName());
                dbTeacher.setDbAddress(saveAddress);
                dbTeacher.setDbGroups(dbGroupList);
                try {
                    teacherRepository.save(dbTeacher);
                    return "Update teacher information";
                } catch (Exception e) {
                    addressRepository.save(oldDbAddress);
                    return "There is an error in the teacher table";
                }
            } catch (Exception e) {
                return "There is an error in the address table";
            }
        } else {
            return "There is no teacher corresponding to the given id";
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteTeacher(@PathVariable Integer id) {
        if (teacherRepository.existsById(id)){
            try {
                teacherRepository.deleteById(id);
                return "Deleted teacher information";
            } catch (Exception e) {
                return "Not deleted teacher information";
            }
        }else {
            return "There is no teacher corresponding to the given id";
        }

    }
}
