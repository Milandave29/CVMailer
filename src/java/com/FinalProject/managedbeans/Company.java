/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.FinalProject.managedbeans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author DARPAN
 */
@ManagedBean
@RequestScoped
public class Company {
    private int id;
    private String name;
    private String email;
    ArrayList userList;
    Connection connection;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Connection getConnection() {
        try {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection = DriverManager.getConnection("jdbc:sqlserver://DARPAN-PC\\SQLEXPRESS:1433;databaseName=TempFinalProject;user=admin;password=admin", "admin", "admin");
        } catch (SQLException ex) {
            String except = ex.toString();
        }
        return connection;
    }
    
    public ArrayList companyList(int id){
        try {
            userList = new ArrayList();
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from company where id = "+id);
            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                company.setEmail(rs.getString("email"));
                userList.add(company);
            }
            connection.close();
        } catch (SQLException e) {
            String ex = e.toString();
        }
        return userList;
    }
    
    public String delete(String email){
        try{
            String sql = "delete from company where email = '" +email +"'";
            connection = getConnection();
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.executeUpdate();
        }catch(SQLException ex){
            String except = ex.toString();
        }
        return "welcome.xhtml?faces-redirect=true";
    }
    
    public String register(int id){
        try {
            connection = getConnection();
            PreparedStatement stmnt  = 
                    connection.prepareStatement("insert into company( "
                            + "id,name,email)"
                            + " values (?,?,?)");
            stmnt.setInt(1, id);
            stmnt.setString(2, name);
            stmnt.setString(3, email);
            stmnt.execute();
            connection.close();
        } catch (SQLException ex) {
            String except = ex.toString();
        }
        return "welcome.xhtml?faces-redirect=true";
    }
}