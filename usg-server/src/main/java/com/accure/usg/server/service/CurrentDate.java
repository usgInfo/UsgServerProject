package com.accure.usg.server.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author chaitu
 */
public class CurrentDate extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        try {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            JSONObject obj = new JSONObject();
            obj.put("month", (cal.get(Calendar.MONTH) + 1));
            obj.put("year", cal.get(Calendar.YEAR));
            obj.put("day", cal.get(Calendar.DAY_OF_MONTH));
            obj.put("date", date);
            obj.put("millis", cal.getTimeInMillis());
            cal = null;

            //getting active financial year
//            FinancialYear fy = FinancialYearManager.getActiveFinancialYear();
            String currentFinancialYear = (String) session.getAttribute("currentFinancialYear");
            //System.out.println(currentFinancialYear);
            if (currentFinancialYear != null) {
                if (currentFinancialYear.contains("~")) {
                    String[] dates = currentFinancialYear.split("~");
                    String[] financialYears = new String[dates.length];
                    if (dates != null && dates.length > 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        for (int i = 0; i < dates.length; i++) {
                            Date temp = sdf.parse(dates[i]);
                            cal = Calendar.getInstance();
                            cal.setTime(temp);
                            financialYears[i] = cal.get(Calendar.YEAR) + "";
                        }
                        obj.put("finacialYears", financialYears);
                    }
                }
            }

            out.write(obj.toString());
        } catch (Exception ex) {
            Logger.getLogger(CurrentDate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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
