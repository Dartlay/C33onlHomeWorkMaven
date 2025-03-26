package com.example;

import javax.servlet.*;
import java.util.Date;

public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(new Date() + " - Приложение C33onlHomeWorkMaven запущено и работает");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(new Date() + " - Приложение C33onlHomeWorkMaven остановлено");
    }
}