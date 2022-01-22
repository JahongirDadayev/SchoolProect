package com.example.restfulproject.payload;

import lombok.Data;

@Data
public class SmallTableDto {
    private Integer id;
    private String dateTime;
    private Integer subjectId;

    public SmallTableDto(String dateTime, Integer subjectId) {
        this.dateTime = dateTime;
        this.subjectId = subjectId;
    }
}
