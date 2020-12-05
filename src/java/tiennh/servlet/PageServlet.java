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
import tiennh.util.ArticleHelper;

/**
 *
 * @author Will
 */
public class PageServlet extends HttpServlet {

    private final String SEARCH_SERVLET = "SearchServlet";
    
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
        String url = SEARCH_SERVLET;

        try{  
            int pageNumber; 
            int perPageNumber;
            String txtThisPage = request.getParameter("thisPage");
            String txtPerPage = request.getParameter("perPage");
            System.out.println("thisPage: "+ txtThisPage);
            try{
                perPageNumber = Integer.parseInt(txtPerPage);
                pageNumber = Integer.parseInt(txtThisPage);
            }
            catch(Exception e){
                perPageNumber = ArticleHelper.DEFAULT_PER_PAGE;
                pageNumber = 1;
            }

            String btn = request.getParameter("btnAction");
            
            if(btn.equals("Next Page")){
                pageNumber++;
            } else if (btn.equals("Previous Page")){
                pageNumber--;
            }

            pageNumber = pageNumber < 1 ? 1 : pageNumber;

            url = SEARCH_SERVLET + 
                "?txtSearch="+request.getParameter("txtSearch") +
                "&thisPage="+(pageNumber) + 
                "&perPage="+(perPageNumber);
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
