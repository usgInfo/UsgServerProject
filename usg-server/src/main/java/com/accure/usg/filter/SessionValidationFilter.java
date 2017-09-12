
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.filter;

import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class SessionValidationFilter implements Filter {
     Logger logger = Logger.getLogger(SessionValidationFilter.class);

    private ArrayList<String> urlList;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String url = request.getServletPath();
        String redirectURL = request.getContextPath();
        String[] ch = redirectURL.split("/");
        boolean allowedRequest = false;
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String redirecturl = scheme + "://" + serverName + ":" + port + "/usg-ui/index.jsp";
        if (urlList.contains(url)) {
          allowedRequest = true;
        }
        if (!allowedRequest) {
            HttpSession session = request.getSession(false);
               //System.out.println(session);
            if (session == null || session.toString().isEmpty()) {
                //System.out.println(session);
               PrintWriter out=response.getWriter();
               request.setAttribute("statuscode", ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
                out.write(ApplicationConstants.HTTP_STATUS_INVALID_SESSION);
            // logger.info(Common.getLogMsg("SessionValidation", ApplicationConstants.FAIL, ApplicationConstants.INVALID_SESSION));
             } else {
//                long remainingTimeInMilliseconds =session.getMaxInactiveInterval()*60-session.getLastAccessedTime();
//                //System.out.println(session.getMaxInactiveInterval());
//                 //System.out.println(session.getLastAccessedTime());
//                //System.out.println(remainingTimeInMilliseconds);
                chain.doFilter(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String urls = config.getInitParameter("avoid-urls");
        StringTokenizer token = new StringTokenizer(urls, ",");

        urlList = new ArrayList<String>();
        while (token.hasMoreTokens()) {
            urlList.add(token.nextToken());

        }
    }
}
