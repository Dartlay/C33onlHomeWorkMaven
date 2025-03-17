package org.homework;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;


public class StAXSonnetParser {

    private String currentElementName;
    private StringBuilder linesContent = new StringBuilder();
    private String firstName;
    private String lastName;
    private String title;

    public void parseXML(String filePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("Файл не найден: " + filePath);
            }
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        handleStartElement(reader);
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        handleCharacters(reader);
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        handleEndElement(reader);
                        break;
                }
            }
            reader.close();
            writeToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleStartElement(XMLStreamReader reader) {
        currentElementName = reader.getLocalName();
    }
    private void handleCharacters(XMLStreamReader reader) {
        String text = reader.getText().trim();
        if (!text.isEmpty()) {
            switch (currentElementName) {
                case "firstName":
                    firstName = text;
                    break;
                case "lastName":
                    lastName = text;
                    break;
                case "title":
                    title = text;
                    break;
                case "line":
                    linesContent.append(text).append("\n"); // Сохраняем строку
                    break;
            }
        }
    }

    private void handleEndElement(XMLStreamReader reader) {
        String elementName = reader.getLocalName();
        if (elementName.equals("sonnet")) {
            System.out.println("----- Конец сонета -----");
        }
    }

    private void writeToFile() {
        if (firstName == null || lastName == null || title == null) {
            throw new IllegalStateException("Не удалось извлечь firstName, lastName или title");
        }
        String fileName = firstName + "_" + lastName + "_" + title + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(linesContent.toString());
            System.out.println("Файл успешно создан: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
