/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import tiennh.articletable.ArticleTableDAO;
import tiennh.articletable.ArticleTableDTO;
import tiennh.articletable.ArticleTablePostError;
import tiennh.usertable.UserTableDTO;
import tiennh.util.ArticleHelper;
import tiennh.util.DBHelper;

/**
 *
 * @author Will
 */
public class PostServlet extends HttpServlet {

    private final String POST_SUCCESSFUL = "ViewArticleServlet";
    private final String POST_UNSUCCESSFUL = "articleMaker.jsp";
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession ses = request.getSession();
        String url = POST_UNSUCCESSFUL;
        UserTableDTO user = (UserTableDTO) ses.getAttribute("SES_USER");
        
        String title = request.getParameter("txtPostTitle");
        String content = request.getParameter("txtPostContent");
        
        title = DBHelper.filterHTML(title);
        content = DBHelper.filterHTML(content);

        Part imagePart = request.getPart("txtImage");
        
        boolean hasError = false;
        
        ArticleTablePostError error = new ArticleTablePostError();

        try {
            if(user!=null){
                System.out.println("title validation");
                // <editor-fold defaultstate="collapsed" desc="Title Validation">//GEN-BEGIN:initComponents
                if(title == null || (title = title.trim()).length() <= ArticleHelper.TITLE_MIN_LEN){
                    error.setArticleTitleError("empty title field");
                    hasError = true;
                }
                else if(title.length() > ArticleHelper.TITLE_MAX_LEN){
                    error.setArticleTitleError("cannot exceed " + ArticleHelper.TITLE_MAX_LEN + " characters");
                    hasError = true;
                }
                // </editor-fold>
            
                System.out.println("content validation");
                // <editor-fold defaultstate="collapsed" desc="Content Validation">//GEN-BEGIN:initComponents
                if(content == null || (content = content.trim()).length() <= ArticleHelper.CONTENT_MIN_LEN){
                    error.setArticleContentError("empty content field");
                    hasError = true;
                }
                else if(content.length() > ArticleHelper.CONTENT_MAX_LEN){
                    error.setArticleContentError("cannot exceed " + ArticleHelper.CONTENT_MAX_LEN + " characters");
                    hasError = true;
                }
                // </editor-fold>

                System.out.println("image validation");
                // <editor-fold defaultstate="collapsed" desc="Image Validation">//GEN-BEGIN:initComponents
                if(imagePart.getSize() > ArticleHelper.IMAGE_MAX_SIZE_VALUE){
                    error.setArticleImageError("image cannot exceed "+
                            ArticleHelper.IMAGE_MAX_SIZE+" MBs");
                    hasError = true;
                }
                // </editor-fold>
                
                System.out.println("hasError: " + hasError);
                if(!hasError ){
                        ArticleTableDAO dao = new ArticleTableDAO();
                        ArticleTableDTO article = new ArticleTableDTO();
                        
                        article.setTitle(title);
                        article.setDescription(content);
                        article.setImageStatus(imagePart.getSize()>0);
                        article.setUser(user);
                        
                        int result = dao.createArticle(article, imagePart);
                        
                        System.out.println("result: "+ result);
                        
                        if(result > 0){
                            url = POST_SUCCESSFUL + "?txtArticleID="+result;
                        } else {
                            hasError = true;
                            error.setArticleError("something went wrong, please try again later");
                        }   
                }
            } else {
                url = LOGIN_PAGE;
            }
        }
        catch(Throwable t){
            hasError = true;
            error.setArticleError("something went wrong, please try again later");
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
        }
        finally{
            
            System.out.println("url: "+ url);
            if(hasError){
                request.setAttribute("POST_ERROR", error);
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            } else {
                response.sendRedirect(url);
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
