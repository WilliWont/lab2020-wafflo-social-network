/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.articletable;

import java.io.Serializable;


public class ArticleTablePostError implements Serializable{
    private String articleError;
    
    private String articleTitleError;
    
    private String articleContentError;
    
    private String articleImageError;

    public String getArticleError() {
        return articleError;
    }

    public void setArticleError(String articleError) {
        this.articleError = articleError;
    }

    public String getArticleTitleError() {
        return articleTitleError;
    }

    public void setArticleTitleError(String articleTitleError) {
        this.articleTitleError = articleTitleError;
    }

    public String getArticleContentError() {
        return articleContentError;
    }

    public void setArticleContentError(String articleContentError) {
        this.articleContentError = articleContentError;
    }

    public String getArticleImageError() {
        return articleImageError;
    }

    public void setArticleImageError(String articleImageError) {
        this.articleImageError = articleImageError;
    }
    
    
}
