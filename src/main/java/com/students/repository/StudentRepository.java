package com.students.repository;

import com.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.group.id = :groupId")
    List<Student> findByGroupId(@Param("groupId") int groupId);

    @Modifying
    @Query("UPDATE Student s SET s.email = :email WHERE s.id = :id")
    void updateEmail(@Param("id") int id, @Param("email") String email);

    @Query("SELECT s FROM Student s WHERE s.name LIKE %:name%")
    List<Student> searchByName(@Param("name") String name);
}