package org.todo.dao;

import org.todo.models.Student;
import org.todo.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDao {
    private static final String STUDENTS_FILE = "students.json";
    private static final Gson gson = new Gson();

    public List<Student> findAll() {
        try {
            String json = FileUtil.loadFromFile(STUDENTS_FILE);
            if (json != null) {
                Type listType = new TypeToken<ArrayList<Student>>(){}.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Student findById(int id) {
        return findAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Student student) {
        List<Student> students = findAll();
        if (student.getId() == 0) {
            int maxId = students.stream().mapToInt(Student::getId).max().orElse(0);
            student.setId(maxId + 1);
            students.add(student);
        } else {
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId() == student.getId()) {
                    students.set(i, student);
                    break;
                }
            }
        }
        saveAll(students);
    }

    public void update(Student student) {
        save(student);
    }

    public void delete(int id) {
        List<Student> students = findAll();
        students.removeIf(s -> s.getId() == id);
        saveAll(students);
    }

    private void saveAll(List<Student> students) {
        try {
            String json = gson.toJson(students);
            FileUtil.saveToFile(STUDENTS_FILE, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getGroupCount() {
        return findAll().stream()
                .map(Student::getGroup)
                .collect(Collectors.toSet())
                .size();
    }
}