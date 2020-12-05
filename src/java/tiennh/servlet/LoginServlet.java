/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiennh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tiennh.usertable.UserTableDAO;
import tiennh.usertable.UserTableDTO;
import tiennh.util.AccountHelper;
import tiennh.util.DBHelper;

/**
 *
 * @author Will
 */
public class LoginServlet extends HttpServlet {
    private final String LOGIN_SUCCESSFUL = "SearchServlet";
    private final String LOGIN_UNSUCCESSFUL = "loginFail.html";

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
        
        String url = LOGIN_UNSUCCESSFUL;
        
        String email = request.getParameter("txtEmail");
        email = DBHelper.filterHTML(email);
        
        String password = AccountHelper
                .hashPassword(request.getParameter("txtPassword"));
        try {
            UserTableDAO dao = new UserTableDAO();
            UserTableDTO dto = dao.loginAccount(email, password);
        
            if(dto != null){
                Cookie user = new Cookie("s2020l1",email + '.' + password);
                user.setMaxAge(60*AccountHelper.COOKIE_AGE);
                response.addCookie(user);
                url = LOGIN_SUCCESSFUL;
                
                HttpSession ses = request.getSession();
                ses.setAttribute("SES_USER", dto);
            }
        }
        catch(Throwable t){
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
        }
        finally{
            response.sendRedirect(url);
            
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
