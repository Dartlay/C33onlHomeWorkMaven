package org.example.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
public class SATXParserExample {
        private String currentElementName;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void handleStartElement(XMLStreamReader reader) {
            currentElementName = reader.getLocalName();
            if (currentElementName.equals("title")) {
                String lang = reader.getAttributeValue(null, "lang");
                if (lang != null) {
                    System.out.println("Язык заголовка: " + lang);
                }
            }
        }
        private void handleCharacters(XMLStreamReader reader) {
            String text = reader.getText().trim();
            if (!text.isEmpty()) {
                switch (currentElementName) {
                    case "title":
                        System.out.println("Название: " + text);
                        break;
                    case "author":
                        System.out.println("Автор: " + text);
                        break;
                    case "year":
                        System.out.println("Год: " + text);
                        break;
                    case "price":
                        System.out.println("Цена: " + text);
                        break;
                }
            }
        }
        private void handleEndElement(XMLStreamReader reader) {
            String elementName = reader.getLocalName();
            if (elementName.equals("book")) {
                System.out.println("----- Конец книги -----");
        }
    }
}