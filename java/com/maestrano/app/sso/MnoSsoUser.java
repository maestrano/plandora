package com.maestrano.app.sso;

import com.maestrano.core.sso.MnoSsoBaseUser;
import com.maestrano.lib.onelogin.saml.Response;
import java.util.Random;
import javax.servlet.http.HttpSession;

public class MnoSsoUser extends MnoSsoBaseUser 
{
  
  /**
   * Construct the MnoSsoBaseUser object from a SAML response
   *
   */
  public MnoSsoUser(Response samlResponse, HttpSession session)
  {
    super(samlResponse, session);
  }
  
  /**
   * Set user in session. Called by signIn method.
   * This method should be overriden in MnoSsoUser to
   * reflect the app specific way of putting an authenticated
   * user in session.
   *
   * @return boolean whether the user was successfully set in session or not
   */
   private Boolean setInSession()
   {
     return null;
   }
  
  /**
   * Create a local user based on the sso user
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   */
  private Integer createLocalUser()
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
  private Integer getLocalIdByUid()
  {
    return null;
  }
  
  /**
   * Get the ID of a local user via email lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   */
  private Integer getLocalIdByEmail()
  {
    return null;
  }
  
  /**
   * Set the Maestrano UID on a local user via email lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   */
  private Boolean setLocalUid()
  {
    return null;
  }
  
  /**
   * Set all "soft" details on the user (like name, surname, email)
   * This is a convenience method that must be implemented in
   * MnoSsoUser but is not mandatory.
   *
   */
   private Boolean syncLocalDetails()
   {
     return true;
   }
  
  
}
