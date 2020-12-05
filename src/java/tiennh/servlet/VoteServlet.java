/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tiennh.articletable.ArticleTableDAO;
import tiennh.usertable.UserTableDTO;
import tiennh.util.VoteHelper;
import tiennh.votetable.VoteTableDAO;
import tiennh.votetable.VoteTableDTO;

/**
 *
 * @author Will
 */
public class VoteServlet extends HttpServlet {

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
        
        UserTableDTO user = (UserTableDTO) 
                request.getSession().getAttribute("SES_USER");
        
        String txtVote = request.getParameter("txtVote");
        String txtArticleID = request.getParameter("txtArticleID");
        
        boolean hasError = false;
        
        String returnMsg = "vote unavailable, please try again later";
        
        String url = LOGIN_PAGE;
        boolean toRedirect = false;
        try {
            if(user != null){
            int voteValue = 0;
            int articleID = 0;
            if(txtVote != null && txtArticleID != null){
                try{
                    articleID = Integer.parseInt(txtArticleID);
                }
                catch(Exception ex){
                    hasError = true;
                }
                
                switch (txtVote) {
                    case "up":
                        voteValue = VoteHelper.UPVOTE_VALUE;
                        break;
                    case "down":
                        voteValue = VoteHelper.DOWNVOTE_VALUE;
                        break;
                    case "unvote":
                        voteValue = VoteHelper.UNVOTE_VALUE;
                        break;
                    default:
                        hasError = true;
                        break;
                }
            }
            if(!hasError){
                VoteTableDTO vote = new VoteTableDTO();
                vote.setArticleID(articleID);
                vote.setUserID(user.getEmail());
                vote.setArticleVote(voteValue);
                
                VoteTableDAO voteDAO = new VoteTableDAO();
                hasError = !voteDAO.voteArticle(vote);
            }
            if(!hasError){
                ArticleTableDAO articleDAO = new ArticleTableDAO();
                Long result = articleDAO.updateArticleVote(articleID);
                if(result != null){
                    returnMsg = result.toString();
                }
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
                out.write(returnMsg);
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
