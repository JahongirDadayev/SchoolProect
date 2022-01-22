package com.example.restfulproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class DbTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    DbAddress dbAddress;

    @ManyToMany
    List<DbGroup> dbGroups;
}
