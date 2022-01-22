package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbAddress;
import com.example.restfulproject.entity.DbSchool;
import com.example.restfulproject.payload.SchoolDto;
import com.example.restfulproject.repository.AddressRepository;
import com.example.restfulproject.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/school")
public class SchoolController {
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    AddressRepository addressRepository;

    @GetMapping
    public List<DbSchool> getDbSchools() {
        return schoolRepository.findAll();
    }

    @PostMapping
    public String postSchool(@RequestBody SchoolDto dbSchoolDto) {
        DbAddress dbAddress = new DbAddress(null, dbSchoolDto.getCity(), dbSchoolDto.getDistrict(), dbSchoolDto.getStreet());
        try {
            DbAddress saveAddress = addressRepository.save(dbAddress);
            DbSchool dbSchool = new DbSchool(null, dbSchoolDto.getName(), saveAddress);
            try {
                schoolRepository.save(dbSchool);
                return "School information saved database";
            } catch (Exception e) {
                return "School information not saved. Because an error occurred while saving to the database.";
            }
        } catch (Exception e) {
            return "Address not saved. Because an error occurred while saving to the database.";
        }
    }

    @PutMapping(path = "/{id}")
    public String putSchool(@PathVariable Integer id, @RequestBody SchoolDto schoolDto) {
        Optional<DbAddress> optionalDbAddress = addressRepository.findById(id);
        if (optionalDbAddress.isPresent()) {
            DbAddress dbAddress = optionalDbAddress.get();
            String oldCity = dbAddress.getCity();
            String oldDistrict = dbAddress.getDistrict();
            String oldStreet = dbAddress.getStreet();
            dbAddress.setCity(schoolDto.getCity());
            dbAddress.setDistrict(schoolDto.getDistrict());
            dbAddress.setStreet(schoolDto.getStreet());
            Optional<DbSchool> optionalDbSchool = schoolRepository.findById(id);
            if (optionalDbSchool.isPresent()) {
                try {
                    DbAddress saveAddress = addressRepository.save(dbAddress);
                    DbSchool dbSchool = optionalDbSchool.get();
                    dbSchool.setName(schoolDto.getName());
                    dbSchool.setDbAddress(saveAddress);
                    try {
                        schoolRepository.save(dbSchool);
                        return "Update school information";
                    } catch (Exception e) {
                        saveAddress.setCity(oldCity);
                        saveAddress.setDistrict(oldDistrict);
                        saveAddress.setStreet(oldStreet);
                        addressRepository.save(saveAddress);
                        return "Not saved school information";
                    }
                } catch (Exception e) {
                    return "There is an error in the school address";
                }

            } else {
                return "There is no school with such an id";
            }
        } else {
            return "There is an error in the school address";
        }
    }

    @GetMapping(path = "/{id}")
    public DbSchool getDbSchool(@PathVariable Integer id) {
        Optional<DbSchool> optionalDbSchool = schoolRepository.findById(id);
        if (optionalDbSchool.isPresent()) {
            return optionalDbSchool.get();
        } else {
            return new DbSchool();
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteSchool(@PathVariable Integer id) {
        try {
            schoolRepository.deleteById(id);
            addressRepository.deleteById(id);
            return "Deleted school information";
        } catch (Exception e) {
            return "Not deleted school information";
        }
    }
}
