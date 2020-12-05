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
import tiennh.articletable.ArticleTableDAO;
import tiennh.articletable.ArticleTableDTO;
import tiennh.commenttable.CommentTableDAO;
import tiennh.commenttable.CommentTableDTO;
import tiennh.usertable.UserTableDTO;
import tiennh.util.ArticleHelper;
import tiennh.votetable.VoteTableDAO;
import tiennh.votetable.VoteTableDTO;

/**
 *
 * @author Will
 */
public class ViewArticleServlet extends HttpServlet {
    
    private final String ARTICLE_PAGE = "article.jsp";
    
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
        
        String articleID = request.getParameter("txtArticleID");
        String url = ARTICLE_PAGE;
        
        int upVoteAmount = 0;
        int downVoteAmount = 0;
        try{
            UserTableDTO user = (UserTableDTO) request.getSession().getAttribute("SES_USER");
            ArticleTableDAO dao = new ArticleTableDAO();
            
            int id;
            try{
                id = Integer.parseInt(articleID);
            }
            catch(NumberFormatException ex){
                id = 0;
            }
            
            ArticleTableDTO article = dao.getArticle(id);
            String articleOP = article.getUser().getEmail();
        
            if(articleOP.equals(user.getEmail())){
                VoteTableDAO voteDAO = new VoteTableDAO();
                CommentTableDAO cmtDAO = new CommentTableDAO();
                boolean voteResult = voteDAO.clearUserNotification(id);
                boolean cmtResult = cmtDAO.clearUserNotification(id);
                
            }
            
            article.setImage("img/article/"+article.getId()+ArticleHelper.IMAGE_EXTENSION);
            request.setAttribute("ARTICLE_REQ", article);
            
            VoteTableDAO voteDAO = new VoteTableDAO();
            VoteTableDTO vote = voteDAO.getUserVote(id, user.getEmail());
            
            upVoteAmount = voteDAO.getArticleUpVote(id);
            downVoteAmount = voteDAO.getArticleDownvote(id);
            if(vote!=null){
                vote.setArticleDownvoteAmount(downVoteAmount);
                vote.setArticleUpvoteAmount(upVoteAmount);
            } else {
                vote = new VoteTableDTO();
                vote.setArticleVote(0);
                vote.setArticleDownvoteAmount(downVoteAmount);
                vote.setArticleUpvoteAmount(upVoteAmount);
            }

            request.setAttribute("ARTICLE_VOTE", vote);
            
            CommentTableDAO commentDAO = new CommentTableDAO();
            List<CommentTableDTO> commentList = commentDAO.getComments(id);
            
            request.setAttribute("ARTICLE_COMMENT", commentList);
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
