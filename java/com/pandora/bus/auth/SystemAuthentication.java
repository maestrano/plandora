package com.pandora.bus.auth;

import com.pandora.UserTO;
import com.pandora.bus.PasswordEncrypt;
import com.pandora.exception.BusinessException;

public class SystemAuthentication extends Authentication{

    public boolean authenticate(UserTO fullUTo, String formPassword, boolean isPassEncrypted) throws BusinessException{
        boolean response = true;
        
 
        String encPass = formPassword;
        if (!isPassEncrypted && !encPass.equals("")) {
            //encrypt the password
            encPass = PasswordEncrypt.getInstance().encrypt(formPassword);
        }
                
        //verify if password is correct...
        if (fullUTo!=null){
        	
            //LogUtil.log(LogUtil.SUMMARY_LOGIN, this, fullUTo.getUsername(), LogUtil.LOG_INFO, 
            //       "[" + fullUTo.getUsername() + "] [" + formPassword + "] [" + encPass + "]");
        	
            if (!fullUTo.getPassword().equals(encPass)){
                response = false; 
            }
        }
        
        return response;
    }

    
	public String getUniqueName() {
		return "label.authMode.DB";
	}
	
}
