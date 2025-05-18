package org.todo.dao;

import org.todo.models.Subject;
import org.todo.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubjectDao {
    private static final String SUBJECTS_FILE = "subjects.json";
    private static final Gson gson = new Gson();

    public List<Subject> findAll() {
        try {
            String json = FileUtil.loadFromFile(SUBJECTS_FILE);
            if (json != null) {
                Type listType = new TypeToken<ArrayList<Subject>>(){}.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Subject findById(int id) {
        return findAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Subject subject) {
        List<Subject> subjects = findAll();
        if (subject.getId() == 0) {
            int maxId = subjects.stream().mapToInt(Subject::getId).max().orElse(0);
            subject.setId(maxId + 1);
            subjects.add(subject);
        } else {
            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId() == subject.getId()) {
                    subjects.set(i, subject);
                    break;
                }
            }
        }
        saveAll(subjects);
    }

    public void update(Subject subject) {
        save(subject);
    }

    public void delete(int id) {
        List<Subject> subjects = findAll();
        subjects.removeIf(s -> s.getId() == id);
        saveAll(subjects);
    }

    private void saveAll(List<Subject> subjects) {
        try {
            String json = gson.toJson(subjects);
            FileUtil.saveToFile(SUBJECTS_FILE, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}