import org.example.dom.DOMParserExample;
import org.example.sax.SAXParserExample;
import org.example.stax.SATXParserExample;
import org.homework.StAXSonnetParser;

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
        StAXSonnetParser parser = new StAXSonnetParser();

        parser.parseXML("shakespearean.xml");
    }
}
