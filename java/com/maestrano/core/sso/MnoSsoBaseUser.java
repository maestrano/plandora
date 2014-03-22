package com.maestrano.core.sso;

import com.maestrano.lib.onelogin.saml.Response;
import java.util.Random;
import java.util.Calendar;

import java.util.Map;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.lang.reflect.Type;

import javax.servlet.http.HttpSession;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MnoSsoBaseUser
{
  /**
   * Session Object
   */
  public HttpSession session = null;
  
  /**
   * User UID
   */
  public String uid;
  
  /**
   * User email
   */
  public String email;
  
  /**
   * User name
   * @var string
   */
  public String name;
  
  /**
   * User surname
   */
  public String surname;
  
  /**
   * Maestrano specific user sso session token
   */
  public String ssoSession;
  
  /**
   * When to recheck for validity of the sso session
   * @var datetime
   */
  public Calendar ssoSessionRecheck = null;
  
  /**
   * Is user owner of the app
   */
  public Boolean appOwner = false;
  
  /**
   * An associative array containing the Maestrano 
   * organizations using this app and to which the
   * user belongs.
   * Keys are the maestrano organization uid.
   * Values are an associative array containing the
   * name of the organization as well as the role 
   * of the user within that organization.
   * ---
   * List of Organization Roles
   * - Member
   * - Power User
   * - Admin
   * - Super Admin
   * ---
   * e.g:
   * { "org-876" => {
   *      "name" => "SomeOrga",
   *      "role" => "Super Admin"
   *   }
   * }
   */
  public HashMap<String,HashMap<String,String>> organizations;
  
  /**
   * User Local Id
   */
  public Integer localId = null;
  
  
  public Integer getLocalId() { return this.localId; }
  
  /**
   * Construct the MnoSsoBaseUser object from a SAML response
   *
   * @param OneLoginSamlResponse samlResponse
   *   A SamlResponse object from Maestrano containing details
   *   about the user being authenticated
   */
  public MnoSsoBaseUser(Response samlResponse, HttpSession session)
  {
      // First get the assertion attributes from the SAML
      // response
      HashMap<String,String> assertAttrs = samlResponse.getAttributes();
      
      // Populate user attributes from assertions
      this.session = session;
      this.uid = assertAttrs.get("mno_uid");
      this.ssoSession = assertAttrs.get("mno_session");
      this.ssoSessionRecheck = DatatypeConverter.parseDateTime(assertAttrs.get("mno_session_recheck"));
      this.name = assertAttrs.get("name");
      this.surname = assertAttrs.get("surname");
      this.email = assertAttrs.get("email");
      this.appOwner = (assertAttrs.get("app_owner").equals("true"));
      
      Gson gson = new Gson();
      Type stringStringMap = new TypeToken<HashMap<String, HashMap<String, String>>>(){}.getType();
      HashMap<String,HashMap<String,String>> map = gson.fromJson(assertAttrs.get("organizations"), stringStringMap);
      this.organizations = map;
  }
  
  /**
   * Try to find a local application user matching the sso one
   * using uid first, then email address.
   * If a user is found via email address then then setLocalUid
   * is called to update the local user Maestrano UID
   * ---
   * Internally use the following interface methods:
   *  - getLocalIdByUid
   *  - getLocalIdByEmail
   *  - setLocalUid
   * 
   * @return localId if a local user matched, null otherwise
   */
  public Integer matchLocal()
  {
    // Try to get the local id from uid
    this.localId = this.getLocalIdByUid();
    
    // Get local id via email if previous search
    // was unsuccessful
    if (this.localId != null) {
      this.localId = this.getLocalIdByEmail();
      
      // Set Maestrano UID on user
      if (this.localId != null) {
        this.setLocalUid();
      }
    }
    
    // Sync local details if we have a match
    if (this.localId != null) {
      this.syncLocalDetails();
    }
    
    return this.localId;
  }
  
  /**
   * Return whether the user is private (
   * local account or app owner or part of
   * organization owning this app) or public
   * (no link whatsoever with this application)
   *
   * @return "public" or "private"
   */
  public String accessScope()
  {
    if (this.localId != null || this.appOwner || this.organizations.size() > 0) {
      return "private";
    }
      
    return "public";
  }
  
  /**
   * Create a local user by invoking createLocalUser
   * and set uid on the newly created user
   * If createLocalUser returns null then access
   * is refused to the user
   */
   public Integer createLocalUserOrDenyAccess()
   {
     if (this.localId == null) {
       this.localId = this.createLocalUser();

        // If a user has been created successfully
        // then make sure UID is set on it
        if (this.localId != null) {
          this.setLocalUid();
        }
     }
     
     return this.localId;
   }
  
  /**
   * Create a local user based on the sso user
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   */
  public Integer createLocalUser()
  {
    return null;
  }
  
  /**
   * Get the ID of a local user via Maestrano UID lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   * @return a user ID if found, null otherwise
   */
  public Integer getLocalIdByUid()
  {
    return null;
  }
  
  /**
   * Get the ID of a local user via email lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   */
  public Integer getLocalIdByEmail()
  {
    return null;
  }
  
  /**
   * Set the Maestrano UID on a local user via email lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   */
  public Boolean setLocalUid()
  {
    return null;
  }
  
  /**
   * Set all "soft" details on the user (like name, surname, email)
   * This is a convenience method that must be implemented in
   * MnoSsoUser but is not mandatory.
   *
   */
   public Boolean syncLocalDetails()
   {
     return true;
   }
  
  /**
   * Sign the user in the application. By default,
   * set the mnoUid, mnoSession and mnoSessionRecheck
   * in session.
   * It is expected that this method get extended with
   * application specific behavior in the MnoSsoUser class
   *
   * @return boolean whether the user was successfully signedIn or not
   */
  public Boolean signIn()
  {
    if (this.setInSession()) {
      this.session.setAttribute("mno_uid",this.uid);
      this.session.setAttribute("mno_session", this.ssoSession);
      
      TimeZone tz = TimeZone.getTimeZone("UTC");
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
      df.setTimeZone(tz);
      String recheckISO = df.format(this.ssoSessionRecheck.getTime());
      
      this.session.setAttribute("mno_session_recheck", recheckISO);
      
      return true;
    }
    
    return false;
  }
  
  /**
   * Generate a random password.
   * Convenient to set dummy passwords on users
   *
   * @return string a random password
   */
  public String generatePassword()
  {
    Integer length = 20;
    String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String randomString = "";
    Random randomGenerator = new Random();
    for (Integer i = 0; i < length; i++) {
        randomString += characters.charAt(randomGenerator.nextInt((characters.length() - 1)));
    }
    return randomString;
  }
  
  /**
   * Set user in session. Called by signIn method.
   * This method should be overriden in MnoSsoUser to
   * reflect the app specific way of putting an authenticated
   * user in session.
   *
   * @return boolean whether the user was successfully set in session or not
   */
   public Boolean setInSession()
   {
     return null;
   }
}