package com.students;

import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.hibernate.SessionFactory;

@SpringBootApplication
public class StudentsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StudentsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(StudentsApplication.class);
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Bean
    @PostConstruct
    public void initTestData(GroupService groupService, StudentService studentService) {
        if (groupService.getAllGroups().isEmpty()) {
            Group group1 = groupService.addGroup("C33Java");
            Group group2 = groupService.addGroup("C34Python");

            studentService.addStudent("John Doe", "john@example.com", group1);
            studentService.addStudent("Jane Smith", "jane@example.com", group2);
        }
    }
}