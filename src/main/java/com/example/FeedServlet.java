package com.example;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.rssreader.RSSReader;

@SuppressWarnings("serial")
public class FeedServlet extends HttpServlet {
    
    private static final String URL = "http://www.atex.com/cmlink/atex-press-releases-1.1472";
    private static final String PARAM_TARGET = "target";
    private static final String TARGET_FEEDDIALOG = "feeddialog";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }
    
    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/createstory.jsp");
        if( TARGET_FEEDDIALOG.equalsIgnoreCase(req.getParameter(PARAM_TARGET)) ) {
            dispatcher = getServletContext().getRequestDispatcher("/feeddialog.jsp");
        }
        
        List<Map<String, String>> rssObjects = new RSSReader().getRSSFeeds(URL);
        if( rssObjects.isEmpty() ) {
            System.out.println("rss feeds is empty");
        }
        else {
            req.setAttribute("rssItems", rssObjects);
        }
        dispatcher.forward(req,resp);
    }

}
