package com.students.dto;

import lombok.*;

@Data
public class StudentDto {
    private String name;
    private String surname;
    private String email;
    private boolean hasPaid;
    private Long groupId;
}