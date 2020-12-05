/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.usertable;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import tiennh.util.DBHelper;
import tiennh.util.AccountHelper;


public class UserTableDAO implements Serializable{
    
    public boolean registerAccount(UserTableDTO account) 
            throws NamingException, SQLException{
        
        boolean result = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "INSERT INTO userTable "
                    + "VALUES(?, ?, ?, ?, ?, ?)";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setString(1, account.getEmail());
            preStm.setString(2, account.getNameF());
            preStm.setString(3, account.getNameL());
            preStm.setString(4, account.getPassword());
            preStm.setInt(5, AccountHelper.DEFAULT_ROLE);
            preStm.setInt(6, DBHelper.DEFAULT_STATUS);
            
            int rowsAffected = preStm.executeUpdate();
            
            if(rowsAffected > 0)
                result = true;
        }
        finally{
            if (rs != null)
                rs.close();
            if (preStm != null)
                preStm.close();
            if (conn != null)
                conn.close();
        }
        
        return result;
    }
    
    public boolean checkDupeAccount(String accountName)
            throws NamingException, SQLException{
        boolean result = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT email "
                    + "FROM UserTable "
                    + "WHERE email = ? ";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setString(1, accountName);
            
            rs = preStm.executeQuery();
            
            if(rs.next())
                result = true;
        }
        finally{
            if (rs != null)
                rs.close();
            if (preStm != null)
                preStm.close();
            if (conn != null)
                conn.close();
        }
        
        return result;
    }
    
    public UserTableDTO loginAccount(String accountName, String accountPassword)
            throws NamingException, SQLException{
        UserTableDTO result = null;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT email, firstName, lastName, password, role "
                    + "FROM UserTable "
                    + "WHERE email = ? AND status = ? AND password = ?";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setString(1, accountName);
            preStm.setInt(2, DBHelper.ACTIVE_STATUS);
            preStm.setString(3, accountPassword);
            
            rs = preStm.executeQuery();
            
            if(rs.next()){
                UserTableDTO dto = new UserTableDTO();
                dto.setEmail(rs.getString("email"));
                dto.setNameF(rs.getString("firstName"));
                dto.setNameL(rs.getString("lastName"));
                dto.setPassword(rs.getString("password"));
                dto.setRole(rs.getInt("role"));
                result = dto;
            }
        }
        finally{
            if (rs != null)
                rs.close();
            if (preStm != null)
                preStm.close();
            if (conn != null)
                conn.close();
        }
        
        return result;
    }
    
}
