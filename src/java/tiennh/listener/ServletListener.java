/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import tiennh.util.ArticleHelper;

/**
 * Web application lifecycle listener.
 *
 * @author Will
 */
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String path = sce.getServletContext().getRealPath("/img/article/");
        
        path = path.substring(0, path.indexOf("\\build"));
        path = path+"\\web\\img\\article\\";
        
        ArticleHelper.setImagePath(path);
        
        System.out.println("image path: "+ ArticleHelper.getImagePath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
