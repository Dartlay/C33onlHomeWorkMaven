package com.students.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    // ... остальные поля
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Student> students;


}
