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
import tiennh.usertable.UserTableDAO;
import tiennh.usertable.UserTableDTO;
import tiennh.usertable.UserTableRegisterError;
import tiennh.util.AccountHelper;
import tiennh.util.DBHelper;

/**
 *
 * @author Will
 */
public class RegisterServlet extends HttpServlet {

    private final String REGISTRATION_SUCCESSFUL = "registerSuccess.html";
    private final String REGISTRATION_UNSUCCESSFUL = "register.jsp";
    
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
        
        String url = REGISTRATION_UNSUCCESSFUL;
        
        String email = request.getParameter("txtEmail");
        String nameF = request.getParameter("txtNameF");
        String nameL = request.getParameter("txtNameL");
        String password = request.getParameter("txtPassword");
        String passwordConf = request.getParameter("txtPasswordConf");
        
        email = DBHelper.filterHTML(email);
        nameF = DBHelper.filterHTML(nameF);
        nameL = DBHelper.filterHTML(nameL);
        
        boolean hasError = false;
        UserTableRegisterError error = new UserTableRegisterError();

        try{
            UserTableDAO dao = new UserTableDAO();

            // <editor-fold defaultstate="collapsed" desc="Email Validation">//GEN-BEGIN:initComponents
            if(email == null || !AccountHelper.validateEmail(email.trim())){
                error.setEmailError("invalid email field");
                hasError = true;
            } 
            else if(dao.checkDupeAccount(email.trim())){
                error.setEmailError("this email has already been registered");
                hasError = true;
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="NameF Validation">//GEN-BEGIN:initComponents
            if(nameF == null || (nameF = nameF.trim()).length() <= AccountHelper.NAME_MIN_LEN){
                error.setFirstNameError("empty name field");
                hasError = true;
            } else if(nameF.length() > AccountHelper.NAME_MAX_LEN){
                error.setFirstNameError("cannot exceed " + AccountHelper.NAME_MAX_LEN + " characters");
                hasError = true;
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="NameL Validation">//GEN-BEGIN:initComponents
            if(nameL == null || (nameL = nameL.trim()).length() <= AccountHelper.NAME_MIN_LEN){
                error.setLastNameError("empty name field");
                hasError = true;
            } else if(nameL.length() > AccountHelper.NAME_MAX_LEN){
                error.setLastNameError("cannot exceed "+AccountHelper.NAME_MAX_LEN+" characters");
                hasError = true;
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Password Validation">//GEN-BEGIN:initComponents
            if(password == null || (password = password.trim()).length() < AccountHelper.PASSWORD_MIN_LEN){
                error.setPasswordError("must have " + AccountHelper.PASSWORD_MIN_LEN + " characters minimum");
                hasError = true;
            }
            else if(!password.matches(AccountHelper.PASSWORD_REGEX)){
                error.setPasswordError("invalid password characters");
                hasError = true;
            }
            else if(password.length() > AccountHelper.PASSWORD_MAX_LEN){
                error.setPasswordError("cannot exceed " + AccountHelper.PASSWORD_MAX_LEN + " characters");
                hasError = true;
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="PasswordConf Validation">//GEN-BEGIN:initComponents
            if(passwordConf == null || !passwordConf.trim().equals(password)){
                error.setPasswordConfError("password does not match");
                hasError = true;
            }
            // </editor-fold>

            if(!hasError){
                UserTableDTO toAdd = new UserTableDTO();
                toAdd.setEmail(email);
                toAdd.setNameF(nameF);
                toAdd.setNameL(nameL);
                toAdd.setPassword(AccountHelper.hashPassword(password));
                
                boolean result = dao.registerAccount(toAdd);
                if(result){
                    url = REGISTRATION_SUCCESSFUL;
                } else {
                    error.setRegistrationError("something went wrong, please try again later");
                    hasError = true;
                }
            }
        }
        catch(Throwable t){
            error.setRegistrationError("something went wrong, please try again later");
            hasError = true;
            log(this.getClass().getSimpleName() + '-' + t.getMessage());
        }
        finally{
            if(hasError){
                request.setAttribute("REG_ERROR", error);
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
