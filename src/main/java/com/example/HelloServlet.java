package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.rssreader.HttpResponseResult;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {

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
        
        HttpResponseResult respResult = null;
        String code = req.getParameter("code");
        if( code != null && !code.trim().equals("") ) {
            StringBuilder sbURL = new StringBuilder();
            sbURL.append("https://graph.facebook.com/oauth/access_token?");
            sbURL.append("client_id=");
            sbURL.append("598621856835572");
            sbURL.append("&client_secret=");
            sbURL.append("81a79e23e3e976a687eaa80365ba32ff");
            sbURL.append("&redirect_uri=");
            sbURL.append(URLEncoder.encode("https://ming-app-site-01.herokuapp.com/hello", "UTF-8"));
            sbURL.append("&code=");
            sbURL.append(code);
            
            respResult = doHttpGET(sbURL.toString(), 30000);
        }
        
        resp.setContentType("text/html");
        ServletOutputStream out = resp.getOutputStream();
        out.write("Hello World from Ming".getBytes());
        if( respResult != null && respResult.getStatusCode() == 200 
                && respResult.getData() != null && respResult.getData().length > 0 ) {
            out.write("<br/>".getBytes());
            out.write(respResult.getData());
        }
        out.write("<br/>".getBytes());
        out.write("Done!".getBytes());
                
        out.flush();
        out.close();
    }
    
    private HttpResponseResult doHttpGET(String url, int connTimeout) {
        HttpResponseResult result = new HttpResponseResult();
        result.setStatusCode(500);
        HttpURLConnection http = null;
        InputStream is = null;
        try {
            http = (HttpURLConnection) new URL(url).openConnection();
            http.setConnectTimeout(connTimeout);
            
            int statusCode = http.getResponseCode();
            result.setStatusCode(statusCode);
            if (statusCode >= 300) {
                throw new Exception("Received error in HTTP GET call due to status " + statusCode);
            }
            
            is = http.getInputStream();
            if( is != null ) {
                result.setCharset(getCharsetFromContentType(http.getContentType(), "UTF-8"));
                
                ByteArrayOutputStream baos = null;
                try {
                    baos = new ByteArrayOutputStream();
                    int bytesRead = 0;
                    while((bytesRead = is.read()) != -1){
                        baos.write(bytesRead);
                    }
                    baos.flush();
                
                    result.setData(baos.toByteArray());
                }
                finally {
                    if( baos != null ) {
                        try {
                            baos.close();
                        }
                        catch(IOException e) {}
                    }
                }
            }
        }
        catch(Throwable th) {
            System.err.println("Failed to do Http GET from URL " + url);
        }
        finally {
            // Clean up
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
            if (http != null) {
                http.disconnect();
            }
        }
        return result;
    }
    
    private String getCharsetFromContentType(String contentType, String defaultCharset) {
        String charset = defaultCharset;
        if( contentType != null ) {
            String[] values = contentType.split(";");
            String temp = null;
            for (String value : values) {
                temp = value.trim();
                if (temp.toLowerCase().startsWith("charset=")) {
                    temp = temp.substring("charset=".length());
                    if( temp.length() > 0 ) {
                        charset = temp;
                        break;
                    }
                }
            }
        }
        return charset;
    }
}
