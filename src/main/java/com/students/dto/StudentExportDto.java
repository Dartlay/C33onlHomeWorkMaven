package com.students.dto;

import lombok.Data;

@Data
public class StudentExportDto {
    private String name;
    private String email;
    private String groupName;

    public StudentExportDto(String name, String email, String groupName) {
        this.name = name;
        this.email = email;
        this.groupName = groupName;
    }
}