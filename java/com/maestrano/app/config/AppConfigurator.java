package com.maestrano.app.config;

public class AppConfigurator {
  
  private static AppConfigurator instance = null;
  
  public String appName;
  public Boolean ssoEnabled;
  public String ssoInitUrl;
  public String ssoReturnUrl;
  
  public AppConfigurator() {
    String fullHost = "http://localhost:8888";
    
    // Application name
    this.appName = "myapp";

    // Enable Maestrano SSO for this app
    this.ssoEnabled = true;

    // SSO initialization URL
    this.ssoInitUrl = fullHost + "/maestrano/auth/saml/index.jsp";

    // SSO processing url
    this.ssoReturnUrl = fullHost + "/maestrano/auth/saml/consume.jsp";
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