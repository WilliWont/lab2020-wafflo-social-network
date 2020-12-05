/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.votetable;

import java.io.Serializable;


public class VoteTableDTO implements Serializable{
    private String userID;
    private int articleID;
    private int articleVote;
    
    private int articleUpvoteAmount;
    private int articleDownvoteAmount;

    public int getArticleUpvoteAmount() {
        return articleUpvoteAmount;
    }

    public void setArticleUpvoteAmount(int articleUpvoteAmount) {
        this.articleUpvoteAmount = articleUpvoteAmount;
    }

    public int getArticleDownvoteAmount() {
        return articleDownvoteAmount;
    }

    public void setArticleDownvoteAmount(int articleDownvoteAmount) {
        this.articleDownvoteAmount = articleDownvoteAmount;
    }

    

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public int getArticleVote() {
        return articleVote;
    }

    public void setArticleVote(int articleVote) {
        this.articleVote = articleVote;
    }
    
}
