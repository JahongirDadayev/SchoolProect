package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbAddress;
import com.example.restfulproject.entity.DbGroup;
import com.example.restfulproject.entity.DbStudent;
import com.example.restfulproject.payload.StudentDto;
import com.example.restfulproject.repository.AddressRepository;
import com.example.restfulproject.repository.GroupRepository;
import com.example.restfulproject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping
    public List<DbStudent> getStudents(){
        return studentRepository.findAll();
    }

    @PostMapping
    public String postStudent(@RequestBody StudentDto studentDto){
        if(groupRepository.existsById(studentDto.getGroupId())){
            DbGroup dbGroup = groupRepository.findById(studentDto.getGroupId()).get();
            DbAddress dbAddress = new DbAddress(null, studentDto.getCity(), studentDto.getDistrict(), studentDto.getStreet());
            try{
                DbAddress saveAddress = addressRepository.save(dbAddress);
                DbStudent dbStudent = new DbStudent(null, studentDto.getFirstName(), studentDto.getLastName(), saveAddress, dbGroup);
                try{
                    studentRepository.save(dbStudent);
                    return "Saved student information";
                }catch (Exception e){
                    addressRepository.delete(saveAddress);
                    return "There is an error in the student table";
                }
            }catch (Exception e){
                return "There is an error in the address table";
            }
        }else {
            return "There is no group corresponding to the given id";
        }
    }

    @PutMapping(path = "/{id}")
    public String updateStudent(@RequestBody StudentDto studentDto, @PathVariable Integer id){
        if(studentRepository.existsById(id)){
            DbStudent dbStudent = studentRepository.findById(id).get();
            if (groupRepository.existsById(studentDto.getGroupId())){
                DbGroup dbGroup = groupRepository.findById(studentDto.getGroupId()).get();
                DbAddress dbAddress = dbStudent.getAddress();
                DbAddress oldDbAddress = dbStudent.getAddress();
                dbAddress.setCity(studentDto.getCity());
                dbAddress.setDistrict(studentDto.getDistrict());
                dbAddress.setStreet(studentDto.getStreet());
                try {
                    DbAddress saveAddress = addressRepository.save(dbAddress);
                    dbStudent.setFirstName(studentDto.getFirstName());
                    dbStudent.setLastName(studentDto.getLastName());
                    dbStudent.setGroups(dbGroup);
                    dbStudent.setAddress(saveAddress);
                    try{
                        studentRepository.save(dbStudent);
                        return "Update student information";
                    }catch (Exception e){
                        addressRepository.save(oldDbAddress);
                        return "Error student table";
                    }

                }catch (Exception e){
                    return "Error address table";
                }
            }else {
                return "There is no group corresponding to the given id";
            }
        }else {
            return "There is no group corresponding to the given id";
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteStudent(@PathVariable Integer id){
        if (studentRepository.existsById(id)){
            try{
                studentRepository.deleteById(id);
                return "Deleted student information";
            }catch (Exception e){
                return "Error student table. Not deleted student information";
            }
        }else {
            return "There is no student corresponding to the given id";
        }

    }
}
