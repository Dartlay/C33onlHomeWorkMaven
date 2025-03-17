package org.example.sax;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private boolean isTitle = false;
    private boolean isAuthor = false;
    private boolean isYear = false;
    private boolean isPrice = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("title")) {
            isTitle = true;
            String lang = attributes.getValue("lang");
            if (lang != null) {
                System.out.println("Язык заголовка: " + lang);
            }
        } else if (qName.equalsIgnoreCase("author")) {
            isAuthor = true;
        } else if (qName.equalsIgnoreCase("year")) {
            isYear = true;
        } else if (qName.equalsIgnoreCase("price")) {
            isPrice = true;
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) {

        currentValue.append(ch, start, length);
    }
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (isTitle) {
            System.out.println("Название: " + currentValue.toString());
            isTitle = false;
        } else if (isAuthor) {
            System.out.println("Автор: " + currentValue.toString());
            isAuthor = false;
        } else if (isYear) {
            System.out.println("Год: " + currentValue.toString());
            isYear = false;
        } else if (isPrice) {
            System.out.println("Цена: " + currentValue.toString());
            isPrice = false;
        }
    }
}