import org.homework.ParserSelector;
import org.homework.stax.StAXSonnetParser;

public class MyMain {
    public static void main(String[] args) {

        /*DOMParserExample parser = new DOMParserExample();
        parser.parseXML("bookstore.xml");
         */

        /*
        SAXParserExample parser = new SAXParserExample();
        parser.parseXML("bookstore.xml");
         */

         /*
        SATXParserExample parser = new SATXParserExample();
        parser.parseXML("bookstore.xml");
        */


        // HomeWork

        /*StAXSonnetParser parser = new StAXSonnetParser();
        parser.parseXML("shakespearean.xml");
        */

        ParserSelector parserSelector = new ParserSelector();
        parserSelector.selectAndParse();
    }
}
