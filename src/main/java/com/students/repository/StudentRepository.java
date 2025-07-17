package com.students.repository;

import com.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);

    List<Student> findByGroupId(Long groupId);

    List<Student> findByHasPaidFalse();

    Optional<Student> findByName(String name);

    Optional<Student> findBySurname(String surname);

    List<Student> findByGroupName(String groupName);

    @Modifying
    @Query("UPDATE Student s SET s.group.id = :newGroupId WHERE s.id = :studentId")
    void changeStudentGroup(@Param("studentId") Long studentId, @Param("newGroupId") Long newGroupId);
}