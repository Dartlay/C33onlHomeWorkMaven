package org.example.OCP;

public class OCPFalse {

        public double width;
        public double height;
    }

    class Circle {
        public double radius;
    }
    class AreaCalculator {
        public double calculateArea(Object shape) {
            if (shape instanceof OCPFalse) {
                OCPFalse r = (OCPFalse) shape;
                return r.width * r.height;
            } else if (shape instanceof Circle) {
                Circle c = (Circle) shape;
                return Math.PI * c.radius * c.radius;
            }
            throw new IllegalArgumentException("Неизвестная фигура");
        }
    }