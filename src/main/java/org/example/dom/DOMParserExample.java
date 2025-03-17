package org.example.dom;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class DOMParserExample{
    public void parseXML(String filePath) {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

            if (inputStream == null) {
                throw new IllegalArgumentException("Файл не найден: " + filePath);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            System.out.println("Корневой элемент: " + root.getNodeName());
            NodeList nodeList = document.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element book = (Element) node;

                    String title = book.getElementsByTagName("title").item(0).getTextContent();
                    String author = book.getElementsByTagName("author").item(0).getTextContent();
                    String year = book.getElementsByTagName("year").item(0).getTextContent();
                    String price = book.getElementsByTagName("price").item(0).getTextContent();

                    System.out.println("\nКнига #" + (i + 1));
                    System.out.println("Название: " + title);
                    System.out.println("Автор: " + author);
                    System.out.println("Год: " + year);
                    System.out.println("Цена: " + price);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
