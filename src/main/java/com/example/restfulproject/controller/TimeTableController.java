package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbSmallTable;
import com.example.restfulproject.entity.DbSubject;
import com.example.restfulproject.entity.DbTimeTable;
import com.example.restfulproject.payload.SmallTableDto;
import com.example.restfulproject.repository.SmallTableRepository;
import com.example.restfulproject.repository.SubjectRepository;
import com.example.restfulproject.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/time_table")
public class TimeTableController {
    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    SmallTableRepository smallTableRepository;

    @GetMapping
    public List<DbTimeTable> getTimeTables() {
        try {
            return timeTableRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @PostMapping
    public String postTimeTable(@RequestBody List<SmallTableDto> smallTableDtoList) {
        List<DbSmallTable> dbSmallTables = new ArrayList<>();

        for (int i = 0; i < smallTableDtoList.size(); i++) {
            Integer subjectId = smallTableDtoList.get(i).getSubjectId();
            Optional<DbSubject> optionalDbSubject = subjectRepository.findById(subjectId);
            if (optionalDbSubject.isPresent()) {
                DbSubject dbSubject = optionalDbSubject.get();
                DbSmallTable dbSmallTable = new DbSmallTable(null, smallTableDtoList.get(i).getDateTime(), dbSubject);
                dbSmallTables.add(dbSmallTable);
            } else {
                return "Non-existent subjectId included";
            }
        }

        for (int i = 0; i < dbSmallTables.size(); i++) {
            try {
                smallTableRepository.save(dbSmallTables.get(i));
            } catch (Exception e) {
                for (int j = i; j >= 0; j--) {
                    smallTableRepository.deleteById(j);
                }
                return "SmallTable error. Information not saved database";
            }
        }

        DbTimeTable dbTimeTable = new DbTimeTable(null, dbSmallTables);
        try {
            timeTableRepository.save(dbTimeTable);
            return "Database saved TimeTable information";
        } catch (Exception e) {
            for (int j = dbSmallTables.size() - 1; j >= 0; j--) {
                smallTableRepository.deleteById(j);
            }
            return "TimeTable errors. Information not saved database";
        }
    }

    @PutMapping(path = "/{id}")
    public String updateTimeTable(@PathVariable Integer id, @RequestBody List<SmallTableDto> smallTableDtoList) {
        Optional<DbTimeTable> optionalDbTimeTable = timeTableRepository.findById(id);
        if (optionalDbTimeTable.isPresent()) {
            DbTimeTable dbTimeTable = optionalDbTimeTable.get();
            List<DbSmallTable> dbSmallTableList = dbTimeTable.getDbSmallTables();

            for (int i = 0; i < smallTableDtoList.size(); i++) {
                Optional<DbSubject> optionalDbSubject = subjectRepository.findById(smallTableDtoList.get(i).getSubjectId());
                if (optionalDbSubject.isPresent()) {
                    DbSubject dbSubject = optionalDbSubject.get();
                    dbSmallTableList.get(smallTableDtoList.get(i).getId() - 1).setDateTime(smallTableDtoList.get(i).getDateTime());
                    dbSmallTableList.get(smallTableDtoList.get(i).getId() - 1).setDbSubjects(dbSubject);
                } else {
                    return "Non-existent subjectId included";
                }
            }

            for (int i = 0; i < dbSmallTableList.size(); i++) {
                try {
                    smallTableRepository.save(dbSmallTableList.get(i));
                } catch (Exception e) {
                    for (int j = i; j >= 0; j--) {
                        smallTableRepository.deleteById(j);
                    }
                    return "SmallTable error. Information not updated database";
                }
            }

            try {
                dbTimeTable.setDbSmallTables(dbSmallTableList);
                timeTableRepository.save(dbTimeTable);
                return "Database update TimeTable information";
            } catch (Exception e) {
                for (int j = dbSmallTableList.size() - 1; j >= 0; j--) {
                    smallTableRepository.deleteById(j);
                }
                return "TimeTable errors. Information not updated database";
            }
        } else {
            return "Non-existent TimeTable id included";
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteTimeTable(@PathVariable Integer id) {
        if (timeTableRepository.existsById(id)){
            timeTableRepository.deleteById(id);
            return "Deleted TimeTable information";
        }else {
            return "Such a timeTable id does not exist";
        }
    }

    @GetMapping(path = "/{id}")
    public DbTimeTable getTimeTable(@PathVariable Integer id) {
        Optional<DbTimeTable> optionalDbTimeTable = timeTableRepository.findById(id);
        if (optionalDbTimeTable.isPresent()) {
            return optionalDbTimeTable.get();
        } else {
            return new DbTimeTable();
        }
    }
}
