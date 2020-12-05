/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.votetable;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;
import tiennh.articletable.ArticleTableDTO;
import tiennh.util.DBHelper;
import tiennh.util.VoteHelper;


public class VoteTableDAO implements Serializable{
    public boolean voteArticle(VoteTableDTO vote) 
                    throws NamingException, SQLException{
        boolean result = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "UPDATE voteTable "
                    + "SET vote = ?, isSeen = ? "
                    + "WHERE userID = ? AND articleID = ? "
                    + "IF @@ROWCOUNT = 0 "
                    + "INSERT INTO voteTable "
                    + "VALUES(?, ?, ?, ?)";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, vote.getArticleVote());
            preStm.setInt(2, DBHelper.DEFAULT_SEEN);
            preStm.setString(3, vote.getUserID());
            preStm.setInt(4, vote.getArticleID());
            preStm.setInt(5, vote.getArticleID());
            preStm.setString(6, vote.getUserID());
            preStm.setInt(7, vote.getArticleVote());
            preStm.setInt(8, DBHelper.DEFAULT_SEEN);

            
            int rowsAffected = preStm.executeUpdate();

            if(rowsAffected > 0){
                result = true;
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
    
    public VoteTableDTO getUserVote(int articleID, String userID)
                    throws NamingException, SQLException{
        VoteTableDTO result = null;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT vote "
                    + "FROM voteTable "
                    + "WHERE userID = ? AND articleID = ?";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setString(1, userID);
            preStm.setInt(2, articleID);

            
            rs = preStm.executeQuery();

            if(rs.next()){
                VoteTableDTO vote = new VoteTableDTO();
                vote.setArticleVote(rs.getInt("vote"));
                result = vote;
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

    public int getArticleUpVote(int articleID)
                    throws NamingException, SQLException{
        int result = 0;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
    
        try{
            String query = "SELECT SUM(vote) as ArticleUpvote " +
                            "FROM voteTable " +
                            "WHERE vote=? AND articleID = ?";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, VoteHelper.UPVOTE_VALUE);
            preStm.setInt(2, articleID);
            
            rs = preStm.executeQuery();

            if(rs.next()){
                result = rs.getInt("ArticleUpvote");
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
    
    public int getArticleDownvote(int articleID)
                    throws NamingException, SQLException{
        int result = 0;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
    
        try{
            String query = "SELECT SUM(vote) as ArticleDownvote " +
                            "FROM voteTable " +
                            "WHERE vote=? AND articleID = ?";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            preStm.setInt(1, VoteHelper.DOWNVOTE_VALUE);
            preStm.setInt(2, articleID);
            
            rs = preStm.executeQuery();

            if(rs.next()){
                result = rs.getInt("ArticleDownvote");
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
    
    public int getUnseenVoteAmount(String userID)
                    throws NamingException, SQLException{
        
        int result = 0;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT a.articleID " 
                        + "FROM voteTable a, (SELECT id FROM articleTable " 
                        + "WHERE userID = ? AND status = ?) b " 
                        + "WHERE a.articleID = b.id "
                        + "AND a.userID != ? AND a.isSeen = ? "
                        + "GROUP BY a.articleID";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            int paramCount = 1;
            
            preStm.setString    (paramCount++, userID);
            preStm.setInt       (paramCount++, DBHelper.ACTIVE_STATUS);
            preStm.setString    (paramCount++, userID);
            preStm.setInt       (paramCount++, DBHelper.NOT_SEEN);

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
    
    public List<ArticleTableDTO> getUnseenVoteList(String userID)
                    throws NamingException, SQLException{
        
        List<ArticleTableDTO> result = null;
        
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        
        try{
            String query = "SELECT b.id, b.title, COUNT(b.id) AS VoteAmount " + 
                            "FROM " +
                            "(SELECT articleID, userID " +
                            "FROM voteTable " +
                            "WHERE vote != ? AND isSeen = ?) a " +
                            "JOIN " +
                            "(SELECT id, title FROM articleTable " +
                            "WHERE userID = ? AND status = ?) b " +
                            "ON a.articleID = b.id AND userID != ? " +
                            "GROUP BY b.id, b.title";
            
            conn = DBHelper.getConnection();
            
            preStm = conn.prepareStatement(query);
            
            int paramCount = 1;
            
            preStm.setInt       (paramCount++, VoteHelper.UNVOTE_VALUE);
            preStm.setInt       (paramCount++, DBHelper.NOT_SEEN);
            preStm.setString    (paramCount++, userID);
            preStm.setInt       (paramCount++, DBHelper.ACTIVE_STATUS);
            preStm.setString    (paramCount++, userID);


            rs = preStm.executeQuery();
            
            while(rs.next()){
                if(result == null)
                    result = new LinkedList();
                
                ArticleTableDTO dto = new ArticleTableDTO();
                dto.setId(rs.getInt("id"));
                dto.setCurrentVote(rs.getInt("VoteAmount"));
                dto.setTitle(rs.getString("title"));
                
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
            String query = "UPDATE voteTable "
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
