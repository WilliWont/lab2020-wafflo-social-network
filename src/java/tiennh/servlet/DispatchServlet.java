/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Will
 */
@MultipartConfig
public class DispatchServlet extends HttpServlet {
    private final String EMPTY_PAGE = "empty.html";
    
    private final String NOTIFICATION_SERVLET = "NotificationServlet";
    private final String GET_NOTIFICATION_SERVLET = "GetNotificationServlet";
    
    private final String SEARCH_SERVLET = "SearchServlet";
//    private final String SEARCH_PAGE = "search.jsp";
    
    private final String PAGE_SERVLET = "PageServlet";
    private final String VOTE_SERVLET = "VoteServlet";
    
    private final String COMMENT_SERVLET = "CommentServlet";
    private final String VIEW_ARTICLE_SERVLET = "ViewArticleServlet";
    
    private final String POST_SERVLET = "PostServlet";
    private final String POST_PAGE = "articleMaker.html";
        
    private final String REGISTRATION_PAGE = "register.html";
    private final String REGISTRATION_SERVLET = "RegisterServlet";
    
    private final String LOGIN_SERVLET = "LoginServlet";
    private final String LOGOUT_SERVLET = "LogoutServlet";
    
    private final String DELETE_COMMENT_SERVLET = "DeleteCommentServlet";
    private final String DELETE_ARTICLE_SERVLET = "DeleteArticleServlet";

    
    private String migrateRequest(String newServlet, HttpServletRequest request){
        StringBuilder toReturn = new StringBuilder(newServlet);
        Enumeration<String> params = request.getParameterNames();
        int count = 0;
        while(params.hasMoreElements()){
            String param = params.nextElement();
            if(count == 0){
                toReturn.append('?');

            } else {
                toReturn.append("&");
            }
            toReturn.append(param);
            toReturn.append('=');
            toReturn.append(request.getParameter(param));
            count++;
        }
        System.out.println("new req: "+ toReturn);
        return toReturn.toString();
    }
    
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
        PrintWriter out = response.getWriter();
        boolean toRedirect = false;
        String url = null;
        String btn = request.getParameter("btnAction");
        System.out.println("DispatchServlet-l1");
        System.out.println("btn: "+ btn);
        try  {
            if(btn == null){
                toRedirect = true;
                url = SEARCH_SERVLET;
            } else if(btn.equals("Register")){
                toRedirect = true;
                url = REGISTRATION_PAGE;
            } else if(btn.equals("Return to Home")){
                toRedirect = true;
                url = migrateRequest(SEARCH_SERVLET, request);
            } else if(btn.equals("Register Account")){
                url = REGISTRATION_SERVLET;
            } else if(btn.equals("Login")){
                url = LOGIN_SERVLET;
            } else if(btn.equals("Logout")){
                toRedirect = true;
                url = LOGOUT_SERVLET;
            } else if(btn.equals("Make an Article")){
                url = POST_PAGE;
            } else if(btn.equals("Post Article")){
                url = POST_SERVLET;
            } else if(btn.equals("Search Article")){
                toRedirect = true;
                url = migrateRequest(SEARCH_SERVLET, request);
            } else if(btn.equals("Next Page")){
                url = PAGE_SERVLET;
            } else if(btn.equals("Previous Page")){
                url = PAGE_SERVLET;
            } else if(btn.equals("Vote")){
                toRedirect = true;
                url = migrateRequest(VOTE_SERVLET, request);
            } else if(btn.equals("View")){
                toRedirect = true;
                url = migrateRequest(VIEW_ARTICLE_SERVLET, request);
            } else if(btn.equals("Post Comment")){
                url = COMMENT_SERVLET;
            } else if(btn.equals("Check Notification")){
                toRedirect = true;
                url = NOTIFICATION_SERVLET;
            } else if(btn.equals("Notification")){
                toRedirect = true;
                url = GET_NOTIFICATION_SERVLET;
            } else if(btn.equals("Delete Comment")){
                toRedirect = true;
                url = migrateRequest(DELETE_COMMENT_SERVLET, request);
            } else if(btn.equals("Delete Article")){
                toRedirect = true;
                url = migrateRequest(DELETE_ARTICLE_SERVLET, request);
            } else {
                toRedirect = true;
                url = EMPTY_PAGE;
            }
            System.out.println(url);
        }
        catch(Throwable t){
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
        }
        // TODO: CURRENT LIBARIES IMPORTED:
        // COMMON CODEC
        // JQUERY
        // GSON
        finally{
            if(toRedirect)
                response.sendRedirect(url);
            else{
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
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
