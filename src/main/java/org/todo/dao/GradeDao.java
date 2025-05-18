package org.todo.dao;

import org.todo.models.Grade;
import org.todo.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;


public class GradeDao {
    private static StudentDao studentDao = new StudentDao();
    private static SubjectDao subjectDao = new SubjectDao();
    private static final String GRADES_FILE = "grades.json";
    private static final Gson gson = new Gson();

    public List<Grade> findAll() {
        try {
            String json = FileUtil.loadFromFile(GRADES_FILE);
            if (json != null) {
                Type listType = new TypeToken<ArrayList<Grade>>(){}.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Grade findById(int id) {
        return findAll().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Grade grade) {
        List<Grade> grades = findAll();
        if (grade.getId() == 0) {
            int maxId = grades.stream().mapToInt(Grade::getId).max().orElse(0);
            grade.setId(maxId + 1);
            grade.setDate(new Date().toString());
            grades.add(grade);
        } else {
            for (int i = 0; i < grades.size(); i++) {
                if (grades.get(i).getId() == grade.getId()) {
                    grades.set(i, grade);
                    break;
                }
            }
        }
        saveAll(grades);
    }

    public void update(Grade grade) {
        save(grade);
    }

    public void delete(int id) {
        List<Grade> grades = findAll();
        grades.removeIf(g -> g.getId() == id);
        saveAll(grades);
    }

    public List<Grade> findByStudentId(int studentId) {
        return findAll().stream()
                .filter(g -> g.getStudentId() == studentId)
                .toList();
    }
    public static StudentDao getStudentDao() { return studentDao; }
    public static SubjectDao getSubjectDao() { return subjectDao; }
    public List<Grade> findBySubjectId(int subjectId) {
        return findAll().stream()
                .filter(g -> g.getSubjectId() == subjectId)
                .toList();
    }
    public List<Grade> findAllWithDetails() {
        List<Grade> grades = findAll();
        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();

        for (Grade grade : grades) {
            grade.setStudent(studentDao.findById(grade.getStudentId()));
            grade.setSubject(subjectDao.findById(grade.getSubjectId()));
        }

        return grades;
    }
    private void saveAll(List<Grade> grades) {
        try {
            String json = gson.toJson(grades);
            FileUtil.saveToFile(GRADES_FILE, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public double getAverageGrade() {
        List<Grade> grades = findAll();
        if (grades.isEmpty()) return 0.0;

        DoubleSummaryStatistics stats = grades.stream()
                .mapToDouble(Grade::getValue)
                .summaryStatistics();

        System.out.println("Average grade calculated: " + stats.getAverage());
        return stats.getAverage();
    }

    public int getMaxGrade() {
        return findAll().stream()
                .mapToInt(Grade::getValue)
                .max()
                .orElse(0);
    }

    public int getMinGrade() {
        return findAll().stream()
                .mapToInt(Grade::getValue)
                .min()
                .orElse(0);
    }
}