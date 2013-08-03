package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FBLoginRedirectServlet extends HttpServlet {
    
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
    
    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletOutputStream out = resp.getOutputStream();
        
        String code = req.getParameter("code");
        if( code != null && !code.trim().equals("") ) {
            String cid = req.getParameter("cid");
            if( code != null && !code.trim().equals("") ) {
                out.write("Parameter cid: ".getBytes());
                out.write(cid.getBytes());
                out.write("<br/>".getBytes());
            }
            out.write("Facebook redirect with the code: ".getBytes());
            out.write("<br/>".getBytes());
            out.write(code.getBytes());
        }
        else {
            out.write("User has not grant the facebook permission".getBytes());
        }
        
        out.write("<br/>".getBytes());
        out.write("Done!".getBytes());
                
        out.flush();
        out.close();
    }

}
