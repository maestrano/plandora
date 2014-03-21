package com.maestrano.core.sso;

import com.maestrano.core.MnoSettings;
import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.net.*;
import java.io.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;

import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MnoSsoSession
{
  /**
   * Maestrano Settings object
   */
  public MnoSettings settings = null;
  
  /**
   * Session object
   */
  public HttpSession session = null;
  
  /**
   * User UID
   */
  public String uid;
  
  /**
   * Maestrano SSO token
   */
  public String token;
  
  /**
   * When to recheck for validity of the sso session
   */
  public Calendar recheck = null;
  
  /**
   * Construct the MnoSsoSession object
   *
   */
  public MnoSsoSession(MnoSettings mnoSettings, HttpSession session)
  {
      // Populate attributes from params
      this.settings = mnoSettings;
      this.session = session;
      this.uid = (String) session.getAttribute("mno_uid");
      this.token = (String) session.getAttribute("mno_session");
      this.recheck = DatatypeConverter.parseDateTime((String) session.getAttribute("mno_session_recheck"));
      
  }
  
  /**
   * Check whether we need to remotely check the
   * session or not
   */
   public Boolean remoteCheckRequired()
   {
     if (this.uid != null && this.token != null && this.recheck != null) {
       if (this.recheck.compareTo(Calendar.getInstance()) > 0 ) {
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
    public String sessionCheckUrl()
    {
      String url = this.settings.getSsoSessionCheckUrl() + "/" + this.uid + "?session=" + this.token;
      return url;
    }
    
    /**
     * Fetch url and return content. Wrapper function.
     *
     * @param string full url to fetch
     * @return string page content
     */
    public String fetchUrl(String url) {
      String outputLine = "";
      
      try {
        URL urlObj = new URL(url);
        
        BufferedReader in = new BufferedReader(
        new InputStreamReader(urlObj.openStream()));
        String inputLine;
      
      
        while ((inputLine = in.readLine()) != null)
            outputLine = outputLine + inputLine;
        in.close();
      } catch (Exception e) {}
      
      return outputLine;
    }
    
  /**
   * Perform remote session check on Maestrano
   */
   public Boolean performRemoteCheck() {
     String json = this.fetchUrl(this.sessionCheckUrl());
     if (json != null) {
      SessionJsonData response = new Gson().fromJson(json, SessionJsonData.class);
      
      if (response.getValid() && (response.getRecheck() != null)) {
        this.recheck = DatatypeConverter.parseDateTime(response.getRecheck());
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
    */
    public Boolean isValid() {
      if (this.remoteCheckRequired()) {
        if (this.performRemoteCheck()) {
          
          TimeZone tz = TimeZone.getTimeZone("UTC");
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
          df.setTimeZone(tz);
          String recheckISO = df.format(this.recheck.getTime());
          
          this.session.setAttribute("mno_session_recheck", recheckISO);
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    }
}