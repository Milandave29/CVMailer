/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.FinalProject.managedbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
import javax.mail.MessagingException;
import javax.mail.Part;
import sun.misc.IOUtils;

/**
 *
 * @author DARPAN
 */
@ManagedBean
@RequestScoped
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private String profession;
    private byte[] resume;
    private Part file;
    Connection connection;
    private Map<String, Object> sessionMap 
            = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }
    
    public String submit(){
        return "index.html";
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
    
    public String delete(int id){
        try{
            String sql = "delete from users where id = '" +id +"'";
            connection = getConnection();
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.executeUpdate();
        }catch(SQLException ex){
            String except = ex.toString();
        }
        return "index.xhtml?faces-redirect=true";
    }
    
    public void save() throws IOException, MessagingException{
        try (InputStream input = file.getInputStream()) {
            //this.resume = IOUtils.readFully(input,10, true);
    Files.copy(input, new File("file", "upload").toPath());
    }
    catch (IOException e) {
       String ex = e.toString();
    }
    }
    
    public String update(User editUser){
        int id = editUser.id;
        String firstname = editUser.firstName;
        String lastname = editUser.lastName;
        String email = editUser.email;
        String password = editUser.password;
        String profession = editUser.profession;
        String gender = editUser.gender;
        
        try{
            connection = getConnection();
            String sql = "update users set first_name=?, last_name=?, password=?, email=?, profession=?, gender=? where id=?";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setString(1, firstname);
            stmnt.setString(2, lastname);
            stmnt.setString(3, password);
            stmnt.setString(4, email);
            stmnt.setString(5, profession);
            stmnt.setString(6, gender);
            stmnt.setInt(7, id);
            
            stmnt.execute();
        }catch(SQLException e){
            String ex=e.toString();
        }
        
        try {
            connection = getConnection();
            String sql = "select * from users where email = '" + email +"' and password = '"+password+"'";
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            User user = null;
            while(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setProfession(rs.getString("profession"));
                user.setGender(rs.getString("gender"));
                this.sessionMap.put("editUser", user);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Updated Successfully!"));
            }
            connection.close();
            } catch (SQLException ex) {
                String except = ex.toString();
            }
        
            return "welcome.xhtml?faces-redirect=true";
    }
    
    public String register(){
        boolean result = true;
        try {
            connection = getConnection();
            PreparedStatement stmnt  = 
                    connection.prepareStatement("insert into users( "
                            + "first_name, last_name, email, password, gender, profession)"
                            + " values (?,?,?,?,?,?)");
            stmnt.setString(1, firstName);
            stmnt.setString(2, lastName);
            stmnt.setString(3, email);
            stmnt.setString(4, password);
            stmnt.setString(5, gender);
            stmnt.setString(6, profession);
            result = stmnt.execute();
            connection.close();
        } catch (SQLException ex) {
            String except = ex.toString();
        }
        if (result != true) {
            try {
            connection = getConnection();
            String sql = "select * from users where email = '" + email +"' and password = '"+password+"'";
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            User user = null;
            while(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setProfession(rs.getString("profession"));
                user.setGender(rs.getString("gender"));
                this.sessionMap.put("editUser", user);
            }
            connection.close();
            } catch (SQLException ex) {
                String except = ex.toString();
            }
            return "welcome.xhtml?faces-redirect=true";
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Try Later! Issue with server"));
            return null;
        }
    }
}
