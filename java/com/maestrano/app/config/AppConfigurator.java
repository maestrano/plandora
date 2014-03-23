package com.maestrano.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

public class AppConfigurator {
  
  private static AppConfigurator instance = null;
  
  public String appName;
  public Boolean ssoEnabled;
  public String ssoInitPath;
  public String ssoReturnPath;
  public String ssoInitUrl;
  public String ssoReturnUrl;
  
  public AppConfigurator() {
    // Load Properties
    Properties prop = new Properties();
    InputStream input = null;
    String filename = "1_app.properties";
    input = AppConfigurator.class.getResourceAsStream(filename);
    try {
      prop.load(input);
    } catch (Exception e) {}
    
    // Application name
    this.appName = prop.getProperty("appname");

    // Enable Maestrano SSO for this app
    this.ssoEnabled = prop.getProperty("ssoenabled").equals("true");

    // SSO initialization URL
    this.ssoInitPath = prop.getProperty("ssoinitpath");

    // SSO processing url
    this.ssoReturnPath = prop.getProperty("ssoreturnpath");
    
  }
  
  public void refreshConfiguration(HttpServletRequest request) {
    // Get context
    String scheme = request.getScheme();
    String serverName = request.getServerName();
    Integer serverPort = request.getServerPort();
    
    String realServerPort = "";
    if (serverPort != null && serverPort != 80 && serverPort != 443) {
      realServerPort = ":" + serverPort.toString();
    }
    
    String fullHost = scheme + "://" + serverName + realServerPort;
    
    this.ssoInitUrl = fullHost + this.ssoInitPath;
    this.ssoReturnUrl = fullHost + this.ssoReturnPath;
  }
  
  /**
   * Returns an instance of this class
   * (this class uses the singleton pattern)
   */
  public static AppConfigurator getInstance(HttpServletRequest request)
  {
      if (instance == null) {
        instance = new AppConfigurator();
      }
      
      // Rebuild configuration
      instance.refreshConfiguration(request);
      
      return instance;
  }
}