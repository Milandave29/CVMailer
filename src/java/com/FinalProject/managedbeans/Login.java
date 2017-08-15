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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author DARPAN
 */
@ManagedBean
@RequestScoped
public class Login {
    private String email;
    private String password;
    Connection connection;
    private Map<String, Object> sessionMap 
            = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap();
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    
    public String authenticate(){
        boolean test = false;
        try {
            connection = getConnection();
            String sql = "select * from users where email = '" + email +"' and password = '"+password+"'";
            Statement stmnt = connection.createStatement();
            ResultSet result = stmnt.executeQuery(sql);
            User user = null;
            while(result.next()){
                user = new User();
                user.setId(result.getInt("id"));
                user.setFirstName(result.getString("first_name"));
                user.setLastName(result.getString("last_name"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setProfession(result.getString("profession"));
                user.setGender(result.getString("gender"));
                this.sessionMap.put("editUser", user);
                test = true;
            }
            connection.close();
        } catch (SQLException ex) {
            String except = ex.toString();
        }
        if (test) {
            return "welcome.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid email or password!"));
            return null;
//return "index.xhtml?faces-redirect=true";
        }
    }
}
