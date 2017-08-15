package com.FinalProject.listeners;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.FinalProject.utils.DbManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SetDbMgrServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext cntxt = sce.getServletContext();
        String url = (String) cntxt.getInitParameter("url");
        String jdbcClass = (String) cntxt.getInitParameter("className");
        String user = (String) cntxt.getInitParameter("user");
        String password = (String) cntxt.getInitParameter("password");
        DbManager DbMgr = null;
        try{
            DbMgr = DbManager.getInstance(url, user, password, jdbcClass);
            cntxt.setAttribute("DbMgr", DbMgr);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Db Failure " + e.toString());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
 System.out.print("context initialized");        
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}