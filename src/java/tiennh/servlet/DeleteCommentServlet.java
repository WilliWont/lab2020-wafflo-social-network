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
import tiennh.commenttable.CommentTableDAO;
import tiennh.commenttable.CommentTableDeleteError;
import tiennh.usertable.UserTableDTO;

/**
 *
 * @author Will
 */
public class DeleteCommentServlet extends HttpServlet {

    private final String ARTICLE_SERVLET = "ViewArticleServlet";
    
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
        
        boolean hasError = false;
        
        HttpSession ses = request.getSession();
        UserTableDTO user = (UserTableDTO) ses.getAttribute("SES_USER");
        
        String txtCmtID = request.getParameter("txtCmtID");
        int cmtID = 0;
        try{
            cmtID = Integer.parseInt(txtCmtID);
        }
        catch(Throwable t){
            hasError = true;
        }
        
        String url = ARTICLE_SERVLET;
        try {
            if(!hasError && user!=null){
                CommentTableDAO dao = new CommentTableDAO();
                hasError = !dao.hideComment(cmtID, user.getRole(), user.getEmail());
            }
        }
        catch(Throwable t){
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
            hasError = true;
        }
        finally{
            if(hasError){
                CommentTableDeleteError error = new CommentTableDeleteError();
                error.setDeleteError("delete unavailable, please try again later");
                request.setAttribute("DELETE_CMT_ERROR", error);
            }
            
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            
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
