package com.example.rssreader;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HttpResponseResult implements Serializable {

    int statusCode;
    byte[] data;
    String charset;
    
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
}
