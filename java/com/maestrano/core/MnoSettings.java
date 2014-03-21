package com.maestrano.core;

import com.maestrano.app.config.AppConfigurator;
import com.maestrano.app.config.MnoConfigurator;

public class MnoSettings {
  /**
   * The name of the application.
   */
  public String appName = "myapp";
  
  /**
   * Is SSO enabled for this application
   */
  public Boolean ssoEnabled = false;
  
  /**
   * If enabled then public access will be completely
   * denied (ALL pages will require authentication)
   */
  public Boolean ssoIntranetMode = false;
  
  /**
   * Maestrano Single Sign On url
   */
  public String ssoUrl = "";
  
  /**
   * The URL where the SSO request should be initiated.
   */
  public String ssoInitUrl = "";
  
  /**
   * The URL where the SSO response will be posted.
   */
  public String ssoReturnUrl = "";
  
  /**
   * The URL where the application should redirect if
   * user is not given access.
   */
  public String ssoAccessUnauthorizedUrl = "";
  
  /**
   * The URL where the application should redirect when
   * user logs out
   */
  public String ssoAccessLogoutUrl = "";
  
  /**
   * The x509 certificate used to authenticate the request.
   */
  public String ssoX509Certificate = "";
  
  /**
   * Specifies what format to return the identification token (Maestrano user UID)
   */
  public String ssoNameIdFormat;
  
  /**
   * The Maestrano endpoint in charge of providing session information
   */
  public String ssoSessionCheckUrl = "";
  
  /**
   * Constructor
   * Use variables set in AppConfigurator and MnoConfigurator
   */
  public MnoSettings(AppConfigurator aConfig, MnoConfigurator mConfig) {
    // Application specific configuration
    this.appName = aConfig.appName;
    this.ssoEnabled = aConfig.ssoEnabled;
    this.ssoInitUrl = aConfig.ssoInitUrl;
    this.ssoReturnUrl = aConfig.ssoReturnUrl;
    
    // Maestrano environment configuration
    this.ssoUrl = mConfig.ssoUrl;
    this.ssoSessionCheckUrl = mConfig.ssoSessionCheckUrl;
    this.ssoAccessUnauthorizedUrl = mConfig.ssoAccessUnauthorizedUrl;
    this.ssoAccessLogoutUrl = mConfig.ssoAccessLogoutUrl;
    this.ssoIntranetMode = mConfig.ssoIntranetMode;
    this.ssoX509Certificate = mConfig.ssoX509Certificate;
  }
  
  public String getAppName() {
    return this.appName;
  }
  
  public Boolean getSsoEnabled() {
    return this.ssoEnabled;
  }
  
  public Boolean getSsoIntranetMode() {
    return this.ssoIntranetMode;
  }
  
  public String getSsoUrl() {
    return this.ssoUrl;
  }
  
  public String getSsoInitUrl() {
    return this.ssoInitUrl;
  }
  
  public String getSsoReturnUrl() {
    return this.ssoReturnUrl;
  }
  
  public String getSsoAccessUnauthorizedUrl() {
    return this.ssoAccessUnauthorizedUrl;
  }
  
  public String getSsoAccessLogoutUrl() {
    return this.ssoAccessLogoutUrl;
  }
  
  public String getSsoX509Certificate() {
    return this.ssoX509Certificate;
  }
  
  public String getSsoNameIdFormat() {
    return this.ssoNameIdFormat;
  }
  
  public String getSsoSessionCheckUrl() {
    return this.ssoSessionCheckUrl;
  }
}