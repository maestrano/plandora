package com.maestrano.app.sso;

import com.maestrano.core.sso.MnoSsoBaseUser;
import com.maestrano.lib.onelogin.saml.Response;
import java.util.Random;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.pandora.dao.DataAccess;

import com.pandora.helper.LogUtil;

import com.pandora.UserTO;
import com.pandora.AreaTO;
import com.pandora.DepartmentTO;
import com.pandora.FunctionTO;
import com.pandora.dao.UserDAO;
import com.pandora.dao.AreaDAO;
import com.pandora.dao.DepartmentDAO;
import com.pandora.dao.FunctionDAO;

public class MnoSsoUser extends MnoSsoBaseUser 
{
  
  private Connection connection = null;
  
  public String message;
  
  /**
   * Construct the MnoSsoBaseUser object from a SAML response
   *
   */
  public MnoSsoUser(Response samlResponse, HttpSession session)
  {
    super(samlResponse, session);
    
    try {
      this.connection = (new DataAccess()).getConnection();
    } catch (Exception e) {
      LogUtil.log(this, LogUtil.LOG_ERROR, "Error while initializing connection", e);
    }
    
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
  
  /**
   * Create a local user based on the sso user
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   */
  @Override
  public Integer createLocalUser()
  {
    Integer lid = null;
    
    if (this.accessScope().equals("private") && this.connection != null) {
      // First build the user
      UserTO user = this.buildLocalUser();
      
      try {
        // Save it
        (new UserDAO()).insert(user,this.connection);
        
        //Get the local user id
        lid = Integer.parseInt(user.getId());
          
      } catch (Exception e) {
        LogUtil.log(this, LogUtil.LOG_ERROR, "Error while inserting user", e);
      }
      
    }
    
    return lid;
  }
  
  /**
   * Build a local user for creation
   */
  public UserTO buildLocalUser()
  {
    UserTO user = new UserTO();
    AreaTO area = new AreaTO();
    DepartmentTO department = new DepartmentTO();
    FunctionTO function = new FunctionTO();
    
    try {
      area = (AreaTO) (new AreaDAO()).getList(this.connection).firstElement();
      department = (DepartmentTO) (new DepartmentDAO()).getList(this.connection).firstElement();
      function = (FunctionTO) (new FunctionDAO()).getList(this.connection).firstElement();
    } catch (Exception e) {
      LogUtil.log(this, LogUtil.LOG_ERROR, "Error while building user", e);
    }
    
    user.setUsername(this.uid);
    user.setEmail(this.email);
    user.setName(this.name + ' ' + this.surname);
    user.setPassword(this.generatePassword());
    user.setArea(area);
    user.setDepartment(department);
    user.setFunction(function);
    user.setCountry("BR");
    user.setLanguage("pt");
    
      
    return user;
  }
  
  /**
   * Get the ID of a local user via Maestrano UID lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   * @return a user ID if found, null otherwise
   */
  @Override
  public Integer getLocalIdByUid()
  {
    Integer lid = null;
    
    if (this.connection != null) {  
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
  			pstmt = this.connection.prepareStatement("select id FROM tool_user WHERE mno_uid = ? LIMIT 1");
        pstmt.setString(1, this.uid);
  			rs = pstmt.executeQuery();
      
        if (rs.next()) {
          lid = rs.getInt("id");
        }
        
      } catch (SQLException e) {
        LogUtil.log(this, LogUtil.LOG_ERROR, "Error while retrieving user id by uid", e);
      } finally {
        if (pstmt != null) { try { pstmt.close(); } catch (SQLException e){} }
      }
    }
    
    
    return lid;
  }
  
  /**
   * Get the ID of a local user via email lookup
   * This method must be re-implemented in MnoSsoUser
   * (raise an error otherwise)
   *
   */
  @Override
  public Integer getLocalIdByEmail()
  {
    Integer lid = null;
    
    if (this.connection != null) {  
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
  			pstmt = this.connection.prepareStatement("select id FROM tool_user WHERE email = ? LIMIT 1");
        pstmt.setString(1, this.email);
  			rs = pstmt.executeQuery();
      
        if (rs.next()) {
          lid = rs.getInt("id");
        }
        
      } catch (SQLException e) {
        LogUtil.log(this, LogUtil.LOG_ERROR, "Error while retrieving user id by email", e);
      } finally {
        if (pstmt != null) { try { pstmt.close(); } catch (SQLException e){} }
      }
    }
    
    return lid;
  }
  
  
  
  /**
   * Set all "soft" details on the user (like name, surname, email)
   * This is a convenience method that must be implemented in
   * MnoSsoUser but is not mandatory.
   *
   */
   @Override
   public Boolean syncLocalDetails()
   {
     Integer upd = 0;
     
     if (this.connection != null && this.localId != null) {  
       PreparedStatement pstmt = null;
        
       try {
   			 pstmt = this.connection.prepareStatement("UPDATE tool_user " + 
           "SET username = ?, name = ?, email = ?" +
           "WHERE id = ?"
         );
         pstmt.setString(1, this.uid);
         pstmt.setString(2, this.name + ' ' + this.surname);
         pstmt.setString(3, this.email);
         pstmt.setInt(4, this.localId);
   			 upd = pstmt.executeUpdate();
        
       } catch (SQLException e) {
         LogUtil.log(this, LogUtil.LOG_ERROR, "Error while syncing local details for user " + this.localId.toString(), e);
       } finally {
         if (pstmt != null) { try { pstmt.close(); } catch (SQLException e){} }
       }
     }
     return (upd > 0);
   }
    
    
   /**
    * Set the Maestrano UID on a local user via id lookup
    * This method must be re-implemented in MnoSsoUser
    * (raise an error otherwise)
    */
   @Override
   public Boolean setLocalUid()
   {
     Integer upd = 0;
     
     if (this.connection != null && this.localId != null) {  
       PreparedStatement pstmt = null;
        
       try {
   			 pstmt = this.connection.prepareStatement("UPDATE tool_user " + 
           "SET mno_uid = ?" +
           "WHERE id = ?"
         );
         pstmt.setString(1, this.uid);
         pstmt.setInt(2, this.localId);
   			 upd = pstmt.executeUpdate();
        
       } catch (SQLException e) {
         LogUtil.log(this, LogUtil.LOG_ERROR, "Error while setting uid on user " + this.localId.toString(), e);
       } finally {
         if (pstmt != null) { try { pstmt.close(); } catch (SQLException e){} }
       }
     }
     return (upd > 0);
   }
  
}
