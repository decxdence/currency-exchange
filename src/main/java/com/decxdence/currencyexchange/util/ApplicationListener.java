package com.decxdence.currencyexchange.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        DatabaseInitializator.initialize();
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
