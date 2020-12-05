/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.util;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.Part;


public class ArticleHelper implements Serializable{
    private static String IMAGE_PATH = null;
    public static final String IMAGE_TAG_PATH = "img/article/";
    public static final String IMAGE_EXTENSION = ".jpg";
    private static final int UPLOAD_DELAY = 1000 * 2; // 2 seconds
    
    public static void setImagePath(String path){
        ArticleHelper.IMAGE_PATH = path;
    }
    
    public static String getImagePath(){
        return ArticleHelper.IMAGE_PATH;
    }

    public static final int TITLE_MIN_LEN = 0;
    public static final int TITLE_MAX_LEN = 128;

    public static final int IMAGE_MAX_SIZE = 5;
    public static final int IMAGE_MAX_SIZE_VALUE = 1000000 * IMAGE_MAX_SIZE; 
    // 1 MB = 1000000 bytes

    public static final int CONTENT_MIN_LEN = 0;
    public static final int CONTENT_MAX_LEN = 512;

    public static final int DEFAULT_VOTE_AMOUNT = 0;
    
    public static final int DEFAULT_PER_PAGE = 20;
    
    public static boolean uploadImage(String imageId, Part image) 
            throws IOException, InterruptedException{
        System.out.println("at uploadImage");
        StringBuilder address = new StringBuilder(ArticleHelper.IMAGE_PATH);

        address.append(imageId);
        address.append(IMAGE_EXTENSION);
        
        image.write(address.toString());
        Thread.sleep(UPLOAD_DELAY);
        return true;
    }
}
