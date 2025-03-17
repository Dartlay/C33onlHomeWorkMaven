package org.homework.dom;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class DOMSonnetParser {

    public void parseXML(String filePath) {
        try {
            // Получаем InputStream для ресурса
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

            if (inputStream == null) {
                throw new IllegalArgumentException("Файл не найден: " + filePath);
            }

            // Создаем DOM парсер
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // Нормализуем структуру документа
            document.getDocumentElement().normalize();

            // Извлекаем данные
            String firstName = document.getElementsByTagName("firstName").item(0).getTextContent();
            String lastName = document.getElementsByTagName("lastName").item(0).getTextContent();
            String title = document.getElementsByTagName("title").item(0).getTextContent();

            // Собираем содержимое тегов <line>
            StringBuilder linesContent = new StringBuilder();
            NodeList lineNodes = document.getElementsByTagName("line");
            for (int i = 0; i < lineNodes.getLength(); i++) {
                linesContent.append(lineNodes.item(i).getTextContent()).append("\n");
            }

            // Записываем содержимое в файл
            writeToFile(firstName, lastName, title, linesContent.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        private void writeToFile(String firstName, String lastName, String title, String content) {
            String fileName = firstName + "_" + lastName + "_" + title + ".txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(content);
                System.out.println("Файл успешно создан: " + fileName);
            } catch (IOException e) {
                System.err.println("Ошибка при записи в файл: " + e.getMessage());
            }
        }
        }


