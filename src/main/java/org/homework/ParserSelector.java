package org.homework;
import org.homework.dom.DOMSonnetParser;
import org.homework.stax.StAXSonnetParser;

import java.util.Scanner;
public class ParserSelector {
    public void selectAndParse() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите парсер (1 - STAX, 2 - DOM):");
        int choice = scanner.nextInt();
        if (choice == 1) {
            StAXSonnetParser saxParser = new StAXSonnetParser();
            saxParser.parseXML("shakespearean.xml");
        } else if (choice == 2) {
            // Используем DOM-парсер
            DOMSonnetParser domParser = new DOMSonnetParser();
            domParser.parseXML("shakespearean.xml");
        } else {
            System.out.println("Неверный выбор. Введите 1 или 2.");
        }

        scanner.close();
    }
}
