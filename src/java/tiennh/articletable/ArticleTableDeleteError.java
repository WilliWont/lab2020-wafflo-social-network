/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.articletable;

import java.io.Serializable;


public class ArticleTableDeleteError implements Serializable{
    private String deleteError;

    public String getDeleteError() {
        return deleteError;
    }

    public void setDeleteError(String deleteError) {
        this.deleteError = deleteError;
    }
}
