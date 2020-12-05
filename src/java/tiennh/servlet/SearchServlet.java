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
import tiennh.util.ArticleHelper;
import tiennh.util.DBHelper;

/**
 *
 * @author Will
 */
public class SearchServlet extends HttpServlet {

    private final String SEARCH_PAGE = "search.jsp";

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
        System.out.println("SearchServlet");
        String url = SEARCH_PAGE;
        int startIndex;
        int endIndex;
        int perPage;
        int curPage;

        String searchQuery = request.getParameter("txtSearch");
        searchQuery = DBHelper.filterHTML(searchQuery);
        
        if(searchQuery == null) searchQuery = "";
        
        String txtCurPage = request.getParameter("thisPage");
        String txtPerPage = request.getParameter("perPage");
        try{
            curPage = Integer.parseInt(txtCurPage);
            perPage = Integer.parseInt(txtPerPage);
            if(curPage <= 0 || perPage <= 0)
                throw new Exception();
        }
        catch(Exception e){
            perPage = ArticleHelper.DEFAULT_PER_PAGE;
            curPage = 1;
        }
        try {
//            if(searchQuery != null && searchQuery.trim().length()>0){
                ArticleTableDAO dao = new ArticleTableDAO();
                System.out.println("curPage: " + curPage);
                startIndex = (curPage-1) * perPage;
                endIndex = startIndex + perPage + 1;
                List<ArticleTableDTO> articles = dao.searchArticle(startIndex, endIndex, searchQuery);
                
                request.setAttribute("ARTICLE_RESULT", articles);
//            }
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
