/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.articletable;

import java.io.Serializable;
import java.sql.Timestamp;
import tiennh.usertable.UserTableDTO;


public class ArticleTableDTO implements Serializable{
    private int id;
    private String title;
    private UserTableDTO user;
    private String description;
    private long currentVote;
    private Timestamp postDate;
    private String image;
    private boolean imageStatus;

    public boolean isImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(boolean imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public long getCurrentVote() {
        return currentVote;
    }

    public void setCurrentVote(long currentVote) {
        this.currentVote = currentVote;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp date) {
        this.postDate = date;
    }
    
    
}
