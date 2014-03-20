package com.maestrano.core.sso;

import com.maestrano.core.MnoSettings;

class MnoSsoSession
{
  /**
   * Maestrano Settings object
   */
  public MnoSettings settings = null;
  
  /**
   * Session object
   */
  public session = null;
  
  /**
   * User UID
   */
  public uid = '';
  
  /**
   * Maestrano SSO token
   */
  public token = '';
  
  /**
   * When to recheck for validity of the sso session
   */
  public recheck = null;
  
  /**
   * Construct the MnoSsoSession object
   *
   * @param MnoSettings mnoSettings
   *   A Maestrano Settings object
   * @param Array session
   *   A session object, usually _SESSION
   *
   */
  public function MnoSsoSession(MnoSettings mnoSettings,&session)
  {
      // Populate attributes from params
      this.settings = mnoSettings;
      this.session = & session;
      this.uid = session['mnoUid'];
      this.token = session['mnoSession'];
      this.recheck = new DateTime(session['mnoSessionRecheck']);
  }
  
  /**
   * Check whether we need to remotely check the
   * session or not
   *
   * @return boolean
   */
   public function remoteCheckRequired()
   {
     if (this.uid && this.token && this.recheck) {
       if(this.recheck > (new DateTime('NOW'))) {
         return false;
       }
     }
     
     return true;
   }
   
   /**
    * Return the full url from which session check
    * should be performed
    *
    * @return string the endpoint url
    */
    public function sessionCheckUrl()
    {
      url = this.settings.ssoSessionCheckUrl . '/' . this.uid . '?session=' . this.token;
      return url;
    }
    
    /**
     * Fetch url and return content. Wrapper function.
     *
     * @param string full url to fetch
     * @return string page content
     */
    public function fetchUrl(url) {
      return fileGetContents(url);
    }
    
  /**
   * Perform remote session check on Maestrano
   *
   * @return boolean the validity of the session
   */
   public function performRemoteCheck() {
     json = this.fetchUrl(this.sessionCheckUrl());
     if (json) {
      response = jsonDecode(json,true);
      
      if (response['valid'] && response['recheck']) {
        this.recheck = new DateTime(response['recheck']);
        return true;
      }
     }
     
     return false;
   }
     
   /**
    * Perform check to see if session is valid
    * Check is only performed if current time is after
    * the recheck timestamp
    * If a remote check is performed then the mnoSessionRecheck
    * timestamp is updated in session.
    *
    * @return boolean the validity of the session
    */
    public function isValid() {
      if (this.remoteCheckRequired()) {
        if (this.performRemoteCheck()) {
          this.session['mnoSessionRecheck'] = this.recheck.format(DateTime::ISO8601);
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    }
}