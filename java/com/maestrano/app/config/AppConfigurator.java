package com.maestrano.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfigurator {
  
  private static AppConfigurator instance = null;
  
  public String appName;
  public Boolean ssoEnabled;
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
    this.ssoInitUrl = prop.getProperty("ssoiniturl");

    // SSO processing url
    this.ssoReturnUrl = prop.getProperty("ssoreturnurl");
  }
  
  /**
   * Returns an instance of this class
   * (this class uses the singleton pattern)
   */
  public static AppConfigurator getInstance()
  {
      if (instance == null) {
        instance = new AppConfigurator();
      }
      return instance;
  }
}