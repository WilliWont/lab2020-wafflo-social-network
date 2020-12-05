/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class DBHelper implements Serializable{
    
    public static final int DELETE_STATUS = 0;
    public static final int ACTIVE_STATUS = 1;
    public static final int DEFAULT_STATUS = 1;
    public static final int DEFAULT_SEEN = 0;
    public static final int NOT_SEEN = 0;
    public static final int IS_SEEN = 1;
    
    public static Connection getConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        
        Context cnCtx = (Context) ctx.lookup("java:comp/env");
        
        DataSource source = (DataSource) cnCtx.lookup("lab1DB");
        
        Connection conn = (Connection) source.getConnection();
                
        return conn;
    }
    
    public static String filterHTML(String input){
        System.out.println("filteringHTML");
        if(input != null){
            System.out.println("not null");
            return input.replaceAll("\\<.*?\\>", "");
        }
        else{
            System.out.println("null");
            return null;
        }
    }
}
