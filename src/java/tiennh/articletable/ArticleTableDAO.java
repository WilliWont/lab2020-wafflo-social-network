/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.articletable;

import java.io.IOException;
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
import javax.servlet.http.Part;
import tiennh.usertable.UserTableDTO;
import tiennh.util.ArticleHelper;
import tiennh.util.DBHelper;

public class ArticleTableDAO implements Serializable{
        public int createArticle(ArticleTableDTO article, 
                Part image) 
            throws NamingException, SQLException, IOException, InterruptedException{

        int result = -1;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "INSERT INTO articleTable "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?)";
            
            conn = DBHelper.getConnection();
            conn.setAutoCommit(false);
            preStm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            preStm.setString(1, article.getTitle());
            preStm.setString(2, article.getUser().getEmail());
            preStm.setString(3, article.getDescription());
            preStm.setBoolean(4, article.isImageStatus());
            preStm.setLong(5, ArticleHelper.DEFAULT_VOTE_AMOUNT);
            preStm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preStm.setInt(7, DBHelper.DEFAULT_STATUS);
            
            int rowsAffected = preStm.executeUpdate();

            System.out.println("row: "+ rowsAffected);
            rs = preStm.getGeneratedKeys();

            if(rs.next()){
                result = rs.getInt(1);
            }
            
            if(article.isImageStatus()){
                boolean uploadResult = 
                    ArticleHelper.uploadImage(Integer.toString(result), image);
                if(uploadResult){
                    conn.commit();
                }
                else {
                    conn.rollback();
                    result = -1;
                }
            } else {
                conn.commit();
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
        
        public boolean hideArticle(int articleID, int userRole, String userID) 
            throws NamingException, SQLException{
        boolean result = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "UPDATE articleTable "
                        + "SET status = ? "
                        + "FROM "
                        + "( "
                        + "SELECT a.email, b.id, a.role "
                        + "FROM "
                        + "(SELECT email, role FROM userTable) a "
                        + "JOIN "
                        + "(SELECT id, userID FROM articleTable WHERE id = ?) b "
                        + "ON a.email = b.userID "
                        + ") AS aTable "
                        + "WHERE aTable.id = articleTable.id "
                        + "AND (aTable.email = ? OR aTable.role > ?)";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);

            preStm.setInt(1, DBHelper.DELETE_STATUS);
            preStm.setInt(2, articleID);
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
        
        public List<ArticleTableDTO> searchArticle(int start, int end, String content)
            throws NamingException, SQLException{
        
        List<ArticleTableDTO> result = null;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT a.id, a.title, b.email, "
                        + "a.image, a.currentVote, a.postTime, b.firstName, b.lastName "
                        + "FROM"
                        + "(SELECT id, title, userID, image, "
                        + "currentVote, postTime "
                        + "FROM articleTable "
                        + "WHERE status = ? AND description LIKE ? "
                        + "ORDER BY postTime DESC "
                        + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY) a "
                        + "JOIN "
                        + "(SELECT email, firstName, lastName "
                        + "FROM userTable) b "
                        + "ON a.userID = b.email ";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, DBHelper.ACTIVE_STATUS);
            preStm.setString(2, '%'+content+'%');
            preStm.setInt(3, start);
            preStm.setInt(4, end);
            rs = preStm.executeQuery();

            while(rs.next()){
                if(result == null)
                    result = new LinkedList();
                
                ArticleTableDTO article = new ArticleTableDTO();
                UserTableDTO user = new UserTableDTO();
                user.setEmail(rs.getString("email"));
                user.setNameF(rs.getString("firstName"));
                user.setNameL(rs.getString("lastName"));
                article.setUser(user);
                article.setId(rs.getInt("id"));
                article.setTitle(rs.getString("title"));
                if(rs.getBoolean("image")){
                    article.setImage
                    (ArticleHelper.IMAGE_TAG_PATH+article.getId()+ArticleHelper.IMAGE_EXTENSION);
                } else {
                    article.setImage
                    (ArticleHelper.IMAGE_TAG_PATH+'0'+ArticleHelper.IMAGE_EXTENSION);
                }
                System.out.println("image: "+ article.getImage());
                article.setCurrentVote(rs.getLong("currentVote"));
                article.setPostDate(rs.getTimestamp("postTime"));
                
                result.add(article);
            }
            if(result!=null)
                System.out.println("got articles: "+result.size());
            else
                System.out.println("no articles");
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
        
        public ArticleTableDTO getArticle(int id)
            throws NamingException, SQLException{
        
        ArticleTableDTO result = null;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT a.id, a.title, b.email, a.image, "
                    + "a.currentVote, a.postTime, b.role, "
                    + "a.description, b.firstName, b.lastName "
                    + "FROM "
                    + "(SELECT id, title, userID, image, "
                    + "currentVote, postTime, description "
                    + "FROM articleTable "
                    + "WHERE status = ? AND id = ?) a "
                    + "JOIN "
                    + "(SELECT email, firstName, lastName, role "
                    + "FROM userTable) b "
                    + "ON a.userID = b.email ";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, DBHelper.ACTIVE_STATUS);
            preStm.setInt(2, id);
            
            rs = preStm.executeQuery();
            
            if(rs.next()){
                ArticleTableDTO article = new ArticleTableDTO();
                
                UserTableDTO user = new UserTableDTO();
                user.setEmail(rs.getString("email"));
                user.setNameF(rs.getString("firstName"));
                user.setNameL(rs.getString("lastName"));
                user.setRole(rs.getInt("role"));
                article.setUser(user);
                
                article.setId(rs.getInt("id"));
                article.setTitle(rs.getString("title"));
                article.setImageStatus(rs.getBoolean("image"));
                article.setCurrentVote(rs.getLong("currentVote"));
                article.setPostDate(rs.getTimestamp("postTime"));
                article.setDescription(rs.getString("description"));
                result = article;
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
        
        public Long updateArticleVote(int articleID)
            throws NamingException, SQLException{
        Long result = null;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "UPDATE a "
                    + "SET a.currentVote = v.sumVote "
                    + "OUTPUT Inserted.currentVote "
                    + "FROM articleTable AS a "
                    + "INNER JOIN"
                    + "("
                    + "SELECT articleID, SUM(vote) sumVote "
                    + "FROM voteTable "
                    + "WHERE articleID = ? "
                    + "GROUP BY articleID"
                    + ") v "
                    + "ON v.articleID = a.id "
                    + "WHERE a.id = ? AND a.status = ?";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, articleID);
            preStm.setInt(2, articleID);
            preStm.setInt(3, DBHelper.ACTIVE_STATUS);
            
//            int rowsAffected = preStm.executeUpdate();
            preStm.execute();
            rs = preStm.getResultSet();
//            System.out.println("row: "+ rowsAffected);
//            rs = preStm.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong("currentVote");
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
