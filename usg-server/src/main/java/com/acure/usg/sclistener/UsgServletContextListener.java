package com.acure.usg.sclistener;


import com.accure.usg.common.manager.DBManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
@WebListener
public class UsgServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        DBManager.closeMongoClient();
    }

}
