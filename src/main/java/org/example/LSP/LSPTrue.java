package org.example.LSP;


    interface Shape {
        int calculateArea();
    }

    class LSPTrue implements Shape {
        private int width;
        private int height;

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public int calculateArea() {
            return width * height;
        }
    }

    class Square2 implements Shape {
        private int side;

        public void setSide(int side) {
            this.side = side;
        }

        @Override
        public int calculateArea() {
            return side * side;
        }
    }