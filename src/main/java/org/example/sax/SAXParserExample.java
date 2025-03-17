package org.example.sax;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.InputStream;

public class SAXParserExample {
        public void parseXML(String filePath) {
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
                if (inputStream == null) {
                    throw new IllegalArgumentException("Файл не найден: " + filePath);
                }
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                SAXParserHandler handler = new SAXParserHandler();
                saxParser.parse(inputStream, handler);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

