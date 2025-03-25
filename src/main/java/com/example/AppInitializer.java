package com.example;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    public AppInitializer() {
        System.out.println("DEBUG: AppInitializer constructor called!");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("====================================");
        System.out.println("  C33onlHomeWorkMaven STARTED");
        System.out.println("  Available at: http://localhost:8080");
        System.out.println("====================================");
    }
}