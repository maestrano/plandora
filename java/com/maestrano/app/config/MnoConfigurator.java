package com.maestrano.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MnoConfigurator {
  
  private static MnoConfigurator instance = null;
  
  public String ssoUrl;
  public String ssoSessionCheckUrl;
  public String ssoAccessUnauthorizedUrl;
  public String ssoAccessLogoutUrl;
  public Boolean ssoIntranetMode;
  public String ssoX509Certificate;
  
  public MnoConfigurator() {
    // Load Properties
    Properties prop = new Properties();
    InputStream input = null;
    String filename = "2_maestrano.properties";
    input = MnoConfigurator.class.getResourceAsStream(filename);
    
    try {
      prop.load(input);
    } catch (Exception e) {}

    // Endpoint to reach for SSO Identification
    this.ssoUrl = prop.getProperty("ssourl");;

    // Endpoint to reach for session information (/api/v1/auth/saml/user-xyz?session=df4sd4g3fd345sfgd534)
    this.ssoSessionCheckUrl = prop.getProperty("ssosessioncheckurl");;

    // Access unauthorized page
    this.ssoAccessUnauthorizedUrl = prop.getProperty("ssoaccessunauthorizedurl");;

    // Access Logout page
    this.ssoAccessLogoutUrl = prop.getProperty("ssoaccesslogouturl");;

    // Intranet Mode - If enabled then ALL pages require authentication
    this.ssoIntranetMode = prop.getProperty("ssointranetmode").equals("true");

    // Maestrano X509 Certificate
    this.ssoX509Certificate = prop.getProperty("ssox509certificate");
  }
  
  /**
   * Returns an instance of this class
   * (this class uses the singleton pattern)
   */
  public static MnoConfigurator getInstance()
  {
      if (instance == null) {
        instance = new MnoConfigurator();
      }
      return instance;
  }
}