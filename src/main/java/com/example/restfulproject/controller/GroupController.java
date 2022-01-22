package com.example.restfulproject.controller;

import com.example.restfulproject.entity.DbGroup;
import com.example.restfulproject.entity.DbTimeTable;
import com.example.restfulproject.payload.GroupDto;
import com.example.restfulproject.repository.GroupRepository;
import com.example.restfulproject.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/group")
public class GroupController {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    TimeTableRepository timeTableRepository;

    @GetMapping
    public List<DbGroup> getGroups() {
        try {
            return groupRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @PostMapping
    public String postGroups(@RequestBody GroupDto groupDto) {
        Optional<DbTimeTable> optionalDbTimeTable = timeTableRepository.findById(groupDto.getTimeTableId());
        if (optionalDbTimeTable.isPresent()) {
            DbTimeTable dbTimeTable = optionalDbTimeTable.get();
            DbGroup dbGroup = new DbGroup(null, groupDto.getName(), dbTimeTable);
            try{
                groupRepository.save(dbGroup);
                return "Group information saved to database";
            }catch (Exception e){
                return "Group information not saved database";
            }
        }else {
            return "There is no timeTable for such id";
        }
    }

    @PutMapping(path = "/{id}")
    public String putMapping(@RequestBody GroupDto groupDto, @PathVariable Integer id) {
        Optional<DbGroup> optionalDbGroup = groupRepository.findById(id);
        if (optionalDbGroup.isPresent()){
            DbGroup dbGroup = optionalDbGroup.get();
            Optional<DbTimeTable> optionalDbTimeTable = timeTableRepository.findById(groupDto.getTimeTableId());
            if (optionalDbTimeTable.isPresent()){
                DbTimeTable dbTimeTable = optionalDbTimeTable.get();
                dbGroup.setName(groupDto.getName());
                dbGroup.setDbTimeTable(dbTimeTable);
                try {
                    groupRepository.save(dbGroup);
                    return "Updating group information";
                }catch (Exception e){
                    return "Error group table. Not updating group information";
                }
            }else{
                return "There is no timeTable for such id";
            }

        }else {
            return "There is no group for such id";
        }
    }

    @GetMapping(path = "/{id}")
    public DbGroup getGroup(@PathVariable Integer id) {
        Optional<DbGroup> optionalDbGroup = groupRepository.findById(id);
        if (optionalDbGroup.isPresent()) {
            DbGroup dbGroup = optionalDbGroup.get();
            return dbGroup;
        } else {
            return new DbGroup();
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteGroup(@PathVariable Integer id) {
        if (groupRepository.existsById(id)){
            try {
                groupRepository.deleteById(id);
                return "Deleted group information";
            } catch (Exception e) {
                return "Not deleted groups information";
            }
        }else {
            return "There is no group for such id";
        }

    }
}
