package com.maestrano.core;

class MaestranoService
{
  private static MaestranoService instance = null;
  
  public String afterSsoSignInPath = '/';
  private MnoSettings settings;
  
  /**
   * constructor
   *
   * this is private constructor (use getInstance to get an instance of this class)
   */
  private void MaestranoService() {}
  
  /**
   * Configure the service by assigning settings
   */
  public static void configure(MnoSettings configSettings)
  {
      this.getInstance().settings = configSettings;
  }
   
  /**
   * Returns an instance of this class
   * (this class uses the singleton pattern)
   */
  public static MaestranoService getInstance()
  {
      if (this.instance == null) {
        this.instance = new MaestranoService();
      }
      return this.instance;
  }
   
   /**
    * Return the maestrano settings
    */
  public MnoSettings getSettings()
  {
    return this.settings;
  }
   
   /**
    * Return the maestrano sso session
    */
   // public function getPhpSession()
   // {
   //   return $_SESSION;
   // }
   
   /**
    * Return the maestrano sso session
    */
  public MnoSsoSession getSsoSession()
  {
    return new MnoSsoSession(this.settings, null);
  }
  
  /**
   * Check if Maestrano SSO is enabled
   */
   public Boolean isSsoEnabled()
   {
     return (this.settings != null && this.settings.getSsoEnabled());
   }
  
  /**
   * Return wether intranet sso mode is enabled (no public pages)
   */
  public Boolean isSsoIntranetEnabled()
  {
    return (this.isSsoEnabled() && this.settings.getSsoIntranetMode());
  }
  
  /**
   * Return where the app should redirect internally to initiate
   * SSO request
   */
  public String getSsoInitUrl()
  {
    return this.settings.getSsoInitUrl();
  }
  
  /**
   * Return where the app should redirect after logging user
   * out
   *
   * @return string url
   */
  public String getSsoLogoutUrl()
  {
    return this.settings.getSsoAccessLogoutUrl();
  }
  
  /**
   * Return where the app should redirect if user does
   * not have access to it
   */
  public String getSsoUnauthorizedUrl()
  {
    return this.settings.getSsoAccessUnauthorizedUrl();
  }
  
  /**
   * Set the after sso signin path
   */
  public static void setAfterSsoSignInPath(path)
  {
    this.instance.afterSsoSignInPath = path;
  }
  
  /**
   * Return the after sso signin path
   */
  public String getAfterSsoSignInPath()
  {
		return this.afterSsoSignInPath;
  }
}