package org.todo.models;

public class Grade {
    private int id;
    private int studentId;
    private int subjectId;
    private int value;
    private String date;
    private transient Student student;
    private transient Subject subject;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


}