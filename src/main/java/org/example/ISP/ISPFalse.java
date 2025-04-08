package org.example.ISP;

    interface Worker {
        void work();
        void eat();
        void sleep();
    }

    class OfficeWorker implements Worker {
        @Override
        public void work() { /*...*/ }

        @Override
        public void eat() { /*...*/ }

        @Override
        public void sleep() { /*...*/ } // А зачем ?) Спать на работе плохо)
    }

    class Robot implements Worker {
        @Override
        public void work() { /*...*/ }

        @Override
        public void eat() { /* Робот не ест) */ }

        @Override
        public void sleep() { /* Робот не спит) */ }
    }