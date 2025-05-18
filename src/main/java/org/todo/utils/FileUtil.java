package org.todo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private static final String DATA_DIR = "data";
    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(String filename, String content) throws IOException {
        Path filePath = Paths.get(DATA_DIR, filename);
        Files.write(filePath, content.getBytes());
    }

    public static String loadFromFile(String filename) throws IOException {
        Path filePath = Paths.get(DATA_DIR, filename);
        if (Files.exists(filePath)) {
            return new String(Files.readAllBytes(filePath));
        }
        return null;
    }

    public static void deleteFile(String filename) throws IOException {
        Path filePath = Paths.get(DATA_DIR, filename);
        Files.deleteIfExists(filePath);
    }
}