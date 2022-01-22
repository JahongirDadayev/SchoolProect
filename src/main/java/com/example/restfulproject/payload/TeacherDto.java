package com.example.restfulproject.payload;

import lombok.Data;

import java.util.List;

@Data
public class TeacherDto {
    private String firstName;
    private String lastName;
    private String city;
    private String district;
    private String street;
    private List<Integer> groups;
}
