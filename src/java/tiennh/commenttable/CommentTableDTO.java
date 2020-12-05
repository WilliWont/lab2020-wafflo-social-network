/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.commenttable;

import java.io.Serializable;
import java.sql.Timestamp;
import tiennh.articletable.ArticleTableDTO;
import tiennh.usertable.UserTableDTO;


public class CommentTableDTO implements Serializable{
    private int id;
    private ArticleTableDTO article;
    private UserTableDTO user;
    private String description;
    private boolean isSeen;
    private Timestamp postDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArticleTableDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleTableDTO article) {
        this.article = article;
    }

    

    public UserTableDTO getUser() {
        return user;
    }

    public void setUser(UserTableDTO user) {
        this.user = user;
    }

    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }
    
    
}
