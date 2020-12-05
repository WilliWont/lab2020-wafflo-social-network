/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.commenttable;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;
import tiennh.articletable.ArticleTableDTO;
import tiennh.usertable.UserTableDTO;
import tiennh.util.DBHelper;


public class CommentTableDAO implements Serializable{
        public int createComment(CommentTableDTO comment) 
            throws NamingException, SQLException{
        int result = -1;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "INSERT INTO commentTable "
                    + "VALUES(?, ?, ?, ?, ?, ?)";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            preStm.setInt(1, comment.getArticle().getId());
            preStm.setString(2, comment.getUser().getEmail());
            preStm.setString(3, comment.getDescription());
            preStm.setInt(4, DBHelper.DEFAULT_STATUS);
            preStm.setInt(5, DBHelper.DEFAULT_SEEN);
            preStm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            
            int rowsAffected = preStm.executeUpdate();
            System.out.println("row: "+ rowsAffected);
            rs = preStm.getGeneratedKeys();

            if(rs.next()){
                result = rs.getInt(1);
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
        
        public boolean hideComment(int commentID, int userRole, String userID) 
            throws NamingException, SQLException{
        boolean result = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "UPDATE commentTable "
                        + "SET status = ? "
                        + "FROM "
                        + "( "
                        + "SELECT a.email, b.id, a.role "
                        + "FROM "
                        + "(SELECT email, role FROM userTable) a "
                        + "JOIN "
                        + "(SELECT id, userID FROM commentTable WHERE id = ?) b "
                        + "ON a.email = b.userID "
                        + ") AS cmtTable "
                        + "WHERE cmtTable.id = commentTable.id "
                        + "AND (cmtTable.email = ? OR cmtTable.role > ?)";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);

            preStm.setInt(1, DBHelper.DELETE_STATUS);
            preStm.setInt(2, commentID);
            preStm.setString(3, userID);
            preStm.setInt(4, userRole);
            
            int rowsAffected = preStm.executeUpdate();

            result = rowsAffected > 0;
            
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
        
        public List<CommentTableDTO> getComments(int articleID)
            throws NamingException, SQLException{
        
        List<CommentTableDTO> result = null;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT a.id, a.userID, a.description, a.date, " + 
                            "b.firstName, b.lastName, b.role " + 
                            "FROM " +
                            "(SELECT id, userID, description, date " +
                            "FROM commentTable " +
                            "WHERE status = ? AND articleID = ?) a " +
                            "JOIN " +
                            "(SELECT firstName, lastName, email, role " +
                            "FROM userTable " +
                            "WHERE status = ?) b " +
                            "ON b.email = a.userID " + 
                            "ORDER BY a.date DESC";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, DBHelper.ACTIVE_STATUS);
            preStm.setInt(2, articleID);
            preStm.setInt(3, DBHelper.ACTIVE_STATUS);

            
            rs = preStm.executeQuery();
            
            while(rs.next()){
                if(result == null)
                    result = new LinkedList();
                
                CommentTableDTO comment = new CommentTableDTO();
                comment.setId(rs.getInt("id"));
                comment.setDescription(rs.getString("description"));
                comment.setPostDate(rs.getTimestamp("date"));
                
                UserTableDTO user = new UserTableDTO();
                user.setEmail(rs.getString("userID"));
                user.setNameF(rs.getString("firstName"));
                user.setNameL(rs.getString("lastName"));
                user.setRole(rs.getInt("role"));
                comment.setUser(user);
                
                result.add(comment);
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
        
        public int getUnseenCommentAmount(String userID)
            throws NamingException, SQLException{
        
        int result = 0;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT a.id " 
                        + "FROM commentTable a, (SELECT id FROM articleTable " 
                        + "WHERE userID = ? AND status = ?) b " 
                        + "WHERE a.articleID = b.id AND a.userID != ? "
                        + "AND a.isSeen = ? AND a.status = ?";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            int paramCount = 1;
            
            preStm.setString    (paramCount++, userID);
            preStm.setInt       (paramCount++, DBHelper.ACTIVE_STATUS);
            preStm.setString    (paramCount++, userID);
            preStm.setInt       (paramCount++, DBHelper.NOT_SEEN);
            preStm.setInt       (paramCount++, DBHelper.ACTIVE_STATUS);

            rs = preStm.executeQuery();
            
            while(rs.next()){
                result++;
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
        
        public List<CommentTableDTO> getUnseenCommentList(String userID)
                    throws NamingException, SQLException{
        
        List<CommentTableDTO> result = null;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT a1.articleID, b1.firstName, b1.lastName, " + 
                            "a1.description, a1.title, a1.date "+ 
                            "FROM " +
                            "(SELECT a.articleID, a.userID, a.date, a.description, b.title " + 
                            "FROM " +
                            "(SELECT articleID, userID, description, date " +
                            "FROM commentTable " +
                            "WHERE userID != ? AND isSeen = ? AND status = ?) a " +
                            "JOIN " +
                            "(SELECT id, title FROM articleTable " +
                            "WHERE userID = ? AND status = ?) b " +
                            "ON a.articleID = b.id " +
                            ") a1 " +
                            "JOIN " +
                            "(SELECT email, firstName, lastName " +
                            "FROM userTable " +
                            "WHERE status = ?) b1 " +
                            "ON b1.email = a1.userID " + 
                            "ORDER BY a1.date DESC ";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setString(1, userID);
            preStm.setInt(2, DBHelper.NOT_SEEN);
            preStm.setInt(3, DBHelper.ACTIVE_STATUS);
            preStm.setString(4, userID);
            preStm.setInt(5, DBHelper.ACTIVE_STATUS);
            preStm.setInt(6, DBHelper.ACTIVE_STATUS);


            rs = preStm.executeQuery();
            
            while(rs.next()){
                if(result == null)
                    result = new LinkedList();
                
                CommentTableDTO dto = new CommentTableDTO();
                dto.setDescription(rs.getString("description"));
                dto.setPostDate(rs.getTimestamp("date"));
                
                UserTableDTO user = new UserTableDTO();
                user.setNameF(rs.getString("firstName"));
                user.setNameL(rs.getString("lastName"));
                dto.setUser(user);
                
                ArticleTableDTO article = new ArticleTableDTO();
                article.setTitle(rs.getString("title"));
                article.setId(rs.getInt("articleID"));
                dto.setArticle(article);
                
                result.add(dto);
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
        
        public boolean clearUserNotification(int articleID)
            throws NamingException, SQLException{

        boolean result = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "UPDATE commentTable "
                        + "SET isSeen = ? "
                        + "WHERE articleID = ? ";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, DBHelper.IS_SEEN);
            preStm.setInt(2, articleID);
            
            result = preStm.executeUpdate() > 0;
            
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
