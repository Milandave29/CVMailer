/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.FinalProject.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DARPAN
 */
public class DbManager {
    private final Connection conn;
    private static DbManager DbMgr = null;
    private static Object lock = new Object();
    
    private DbManager(String url,String user,String password,String jdbcClass) throws ClassNotFoundException, SQLException{
        Class.forName(jdbcClass);
        this.conn = DriverManager.getConnection(url,user,password);
    }
    
    public static DbManager getInstance(String url,String user,String password,String jdbcClass){
       synchronized(lock){
           if(DbMgr == null){
                try {
                    DbMgr = new DbManager(url,user,password,jdbcClass);
                 } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
       }
       return DbMgr;
    }
    
    public Connection getConnection(){
        return this.conn;
    }
}