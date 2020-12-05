/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tiennh.articletable.ArticleTableDTO;
import tiennh.commenttable.CommentTableDAO;
import tiennh.commenttable.CommentTableDTO;
import tiennh.usertable.UserTableDTO;
import tiennh.util.CommentHelper;
import tiennh.util.DBHelper;

/**
 *
 * @author Will
 */
public class CommentServlet extends HttpServlet {

    private final String LOGIN_PAGE = "login.html";
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        String txtComment = request.getParameter("txtComment");
        String txtArticleID = request.getParameter("txtArticleID");
        
        String url = LOGIN_PAGE;
        boolean toRedirect = false;
        
        txtComment = DBHelper.filterHTML(txtComment);
        txtArticleID = DBHelper.filterHTML(txtArticleID);
        
        int articleID;
        UserTableDTO user = (UserTableDTO)
                            (request.getSession().getAttribute("SES_USER"));
        boolean hasError = false;
        CommentTableDTO comment = new CommentTableDTO();
        JsonObject commentReturn = new JsonObject();
        String returnMsg = "comment unavailable";
        try{
            if(user != null){
            try{
                articleID = Integer.parseInt(txtArticleID);
            }
            catch(NumberFormatException ex){
                articleID = 0;
            }
            
            if(articleID != 0){
                if(txtComment != null && 
                    (txtComment = txtComment.trim()).length() > CommentHelper.COMMENT_MIN_LEN){
                    if(txtComment.length() <= CommentHelper.COMMENT_MAX_LEN){
                        CommentTableDAO dao = new CommentTableDAO();
                        
                        ArticleTableDTO article = new ArticleTableDTO();
                        article.setId(articleID);
                        comment.setArticle(article);
                        
                        comment.setUser(user);
                        comment.setDescription(txtComment);
                        
                        int id = dao.createComment(comment);
                        
                        if(id > 0){
                            
                            comment.setId(id);
                            commentReturn.addProperty
                            ("id", Integer.toString(comment.getId()));
                            commentReturn.addProperty
                            ("name", comment.getUser().getNameF()+' '+comment.getUser().getNameL());
                            commentReturn.addProperty
                            ("description", comment.getDescription());
//                            commentReturn.addProperty
//                            ("articleID", Integer.toString(comment.getArticle().getId()));
                            
                        } else {
                            hasError = true;
                        }
                    } else {
                        hasError = true;
                        returnMsg = "exceeded " + 
                        CommentHelper.COMMENT_MAX_LEN + " characters";
                        commentReturn.addProperty("error", returnMsg);
                    }
                } else {
                    hasError = true;
                }
            } else {
                hasError = true;
            }
        } else {
                toRedirect = true;
            }
        }
        catch(Throwable t){
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
        }
        finally{
            if(toRedirect){
                response.sendRedirect(url);
            } else {
                if(hasError){
                   returnMsg = "empty comment field";
                    commentReturn.addProperty("error", returnMsg);
                }
                out.write(new Gson().toJson(commentReturn));   
            }
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
