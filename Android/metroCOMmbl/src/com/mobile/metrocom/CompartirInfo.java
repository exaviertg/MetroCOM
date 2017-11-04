package com.mobile.metrocom;

import java.util.Date;

import android.app.DownloadManager.Request;


public class CompartirInfo {
      private Request request = null;
        private String name = null;
        private Date date = null;

    // SINGLETON DEFINITION
    private static CompartirInfo INSTANCE = null;

    private CompartirInfo() {}
 
    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new CompartirInfo();
        }
        
    }
 
    public static CompartirInfo getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    // GETTERS AND SETTERS  
    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String geName() {
        return this.name;
    }

    public void setNamet(String name) {
        this.name = name;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
	
	


