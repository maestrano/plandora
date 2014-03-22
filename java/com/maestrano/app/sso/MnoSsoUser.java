package com.maestrano.app.sso;

import com.maestrano.core.sso.MnoSsoBaseUser;
import com.maestrano.lib.onelogin.saml.Response;
import java.util.Random;
import javax.servlet.http.HttpSession;

import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.pandora.dao.DataAccess;

import com.pandora.helper.LogUtil;

import com.pandora.TransferObject;
import com.pandora.UserTO;
import com.pandora.AreaTO;
import com.pandora.DepartmentTO;
import com.pandora.FunctionTO;
import com.pandora.MetaFormTO;
import com.pandora.SurveyTO;
import com.pandora.ProjectTO;
import com.pandora.LeaderTO;
import com.pandora.ResourceTO;

import com.pandora.bus.PasswordEncrypt;

import com.pandora.dao.UserDAO;
import com.pandora.dao.AreaDAO;
import com.pandora.dao.DepartmentDAO;
import com.pandora.dao.FunctionDAO;
import com.pandora.dao.ProjectDAO;

import com.pandora.delegate.MetaFormDelegate;
import com.pandora.delegate.SurveyDelegate;
import com.pandora.delegate.UserDelegate;

import com.pandora.gui.struts.action.ShowSurveyAction;

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
     UserTO user = null;
     
     try {
       UserTO userFilter = new UserTO();
       userFilter.setUsername(this.uid);
       user = (new UserDAO()).getObjectByUsername(userFilter);
     
     } catch (Exception e) {
       LogUtil.log(this, LogUtil.LOG_ERROR, "Error while retrieving user for authentication", e);
     }
     
     if (user != null) {
    		MetaFormDelegate mfrmDel = new MetaFormDelegate();
    		SurveyDelegate sDel = new SurveyDelegate();
    		
        try {    
    		    //create a new User TO with informations of username/password
    		    //this.clearMessages(request);

    		    //authenticate user!
    		    UserDelegate deleg = new UserDelegate();
		        //user = deleg.authenticateUser(user, true);
            
		        UserTO childUser = deleg.getUserTopRole(user);
		        childUser.setPreference(user.getPreference());
		        //childUser.setBundle(getResources(request));
        
		        childUser.setLanguage(user.getLanguage());
		        childUser.setCountry(user.getCountry());
        
		        //update the user with the newest information about locale
		        deleg.updateUser(childUser);
        
		        //...store current user into http session
		        this.session.removeAttribute(UserDelegate.CURRENT_USER_SESSION);
		        this.session.setAttribute(UserDelegate.CURRENT_USER_SESSION, childUser);

    				Vector<MetaFormTO> v = mfrmDel.getMetaFormList();
    				this.session.setAttribute("metaFormList", v);			

    			  this.session.removeAttribute(ShowSurveyAction.PARTIAL_ANSWERS);
    				this.session.removeAttribute(UserDelegate.USER_SURVEY_LIST);
    				Vector<SurveyTO> vs = sDel.getSurveyListByUser(childUser, true);
   				
           if (vs!=null && vs.size()>0) {
    					this.session.setAttribute(UserDelegate.USER_SURVEY_LIST, vs);					
    				}
          
           return true;

    		} catch (Exception e) {
    			LogUtil.log(this, LogUtil.LOG_ERROR, "Error while authenticating user", e);
    		}
     }
     
     return false;
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
        UserDAO dao = new UserDAO();
        dao.insert(user,this.connection);
        dao.updatePassword(user);
        
        // Add user to root project if admin
        if (this.isUserAdmin()) {
          ProjectDAO pdao = new ProjectDAO();
          
          ProjectTO filterProject = new ProjectTO();
          filterProject.setId("0");
          
          ProjectTO rootProject = pdao.getProjectById(filterProject,true);
          
          // Set updateResource to avoid error
          rootProject.setUpdateResources(new Vector<ResourceTO>());
          
          // Set as resource
          ResourceTO resource = new ResourceTO(user.getId());
          Vector<ResourceTO> insertResources = new Vector<ResourceTO>();
          insertResources.addElement(resource);
          rootProject.setInsertResources(insertResources);
          
          
          // Set as leader
          LeaderTO leader = new LeaderTO(user);
          Vector<LeaderTO> insertLeaders = new Vector<LeaderTO>();
          insertLeaders.addElement(leader);
          rootProject.setInsertLeaders(insertLeaders);
          
          
          // Update
          pdao.update(rootProject,this.connection);
        }
        
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
    
    String encPassword = this.generatePassword();
    try {
      encPassword = PasswordEncrypt.getInstance().encrypt(this.generatePassword());
    } catch (Exception e) {
      LogUtil.log(this, LogUtil.LOG_ERROR, "Error encrypting password",e);
    }
    
    user.setUsername(this.uid);
    user.setEmail(this.email);
    user.setName(this.name + ' ' + this.surname);
    user.setPassword(encPassword);
    user.setArea(area);
    user.setDepartment(department);
    user.setFunction(function);
    user.setCountry("BR");
    user.setLanguage("pt");
    
      
    return user;
  }
  
  /**
   * Check whether the user should
   * be set as admin or not
   */
  public Boolean isUserAdmin()
  {
    Boolean isAdmin = false;
    
    if (this.appOwner) {
      isAdmin = true;
    } else {
      for (Map.Entry<String, HashMap<String,String>> entry : this.organizations.entrySet())
      {
        HashMap<String,String> organization = entry.getValue();
        if (organization.get("role").equals("Admin") || organization.get("role").equals("Super Admin")) {
          isAdmin = true;
        } else {
          isAdmin = false;
        }
      }
    }
    
    return isAdmin;
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
