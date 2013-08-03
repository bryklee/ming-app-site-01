package com.example.rssreader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class RSSReader {
    
    private static final String UTF_8 = "UTF-8";
    private static final int TIMEOUT = 10000;
    
    private static final String RSS_ITEM_TITLE = "title";
    private static final String RSS_ITEM_LINK = "link";
    private static final String RSS_ITEM_DESCRIPTION = "description";
    private static final String RSS_ITEM_PUB_DATE = "pubDate";
    
    public List<Map<String, String>> getRSSFeeds(String url) {
        List<Map<String, String>> rssObjects = new LinkedList<Map<String, String>>();
        try {
            String data = "";
            HttpResponseResult result = doHttpGET(url, TIMEOUT);
            if( result != null && result.getData() != null && result.getData().length > 0 ) {
                data = new String(result.getData(), result.getCharset());
                data = data.trim().replaceFirst("^([\\W]+)<","<");
            }
                    
            Document document = DocumentHelper.parseText(data);

            Element root = document.getRootElement();
            int listIndex = 0;
            Iterator<?> ielements = root.element("channel").elements().iterator();
            while (ielements.hasNext()) {
                Element e = (Element) ielements.next();
                String title = null;
                String link = null;
                String description = null;

                Element titleElement = e.element(RSS_ITEM_TITLE);
                Element linkElement = e.element(RSS_ITEM_LINK);
                Element descriptionElement = e.element(RSS_ITEM_DESCRIPTION);
                Element pubDateElement = e.element(RSS_ITEM_PUB_DATE);

                if (titleElement != null) {
                    title = titleElement.getText();
                }

                if (linkElement != null) {
                    link = linkElement.getText();
                }

                if (descriptionElement != null) {
                    description = descriptionElement.getText();
                }

                Map<String, String> rssObject = new HashMap<String, String>();
                if (title != null && title.length() > 0 && link != null && link.length() > 0) {
                    rssObject.put(RSS_ITEM_TITLE, title);
                    rssObject.put(RSS_ITEM_LINK, link);
                    rssObject.put(RSS_ITEM_DESCRIPTION, description);
                    if (pubDateElement != null)
                        rssObject.put(RSS_ITEM_PUB_DATE, pubDateElement.getText());
                    rssObjects.add(rssObject);
                    listIndex++;
                }
                if (listIndex >= 10) {
                    break;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return rssObjects;
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
                result.setCharset(getCharsetFromContentType(http.getContentType(), UTF_8));
                
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
