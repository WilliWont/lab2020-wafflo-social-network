/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tiennh.articletable.ArticleTableDTO;
import tiennh.commenttable.CommentTableDAO;
import tiennh.commenttable.CommentTableDTO;
import tiennh.usertable.UserTableDTO;
import tiennh.votetable.VoteTableDAO;

/**
 *
 * @author Will
 */
public class GetNotificationServlet extends HttpServlet {

    private final String NOTIFICATION_PAGE = "notification.jsp";
    
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
        UserTableDTO user = (UserTableDTO) ses.getAttribute("SES_USER");
        
        String url = NOTIFICATION_PAGE;
        
        try {
            VoteTableDAO voteDAO = new VoteTableDAO();
            List<ArticleTableDTO> voteList = voteDAO.getUnseenVoteList(user.getEmail());
            request.setAttribute("VOTE_NOTIF", voteList);
            
            CommentTableDAO cmtDAO = new CommentTableDAO();
            List<CommentTableDTO> cmtList = cmtDAO.getUnseenCommentList(user.getEmail());
            request.setAttribute("CMT_NOTIF", cmtList);
        }
        catch(Throwable t){
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
        }
        finally{
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
