package com.students.model;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private int id;
    private String name;
    private String email;
    private Group group;

}