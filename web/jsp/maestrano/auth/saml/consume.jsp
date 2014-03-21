<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.maestrano.core.*,com.maestrano.core.sso.*,com.maestrano.app.sso.*" %>
<%@ page import="com.maestrano.lib.onelogin.*,com.maestrano.lib.onelogin.saml.*,org.apache.log4j.Logger" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SAML Assertion Page</title>
</head>
<body>
<%
  // Get Maestrano Service
  MaestranoService maestrano = MaestranoService.getInstance();
  
  // user account specific settings. Import the certificate here
  AccountSettings accountSettings = new AccountSettings();
  accountSettings.setCertificate(maestrano.getSettings().getSsoX509Certificate());

  Response samlResponse = new Response(accountSettings);
  samlResponse.loadXmlFromBase64(request.getParameter("SAMLResponse"));

  if (samlResponse.isValid()) {
    
    // Get the Maestrano User
    MnoSsoUser sso_user = new MnoSsoUser(samlResponse,session);
    
    // Try to match the user with a local one
    sso_user.matchLocal();
    
    // If user was not matched then attempt
    // to create a new local user
    if (sso_user.getLocalId() == null) {
      sso_user.createLocalUserOrDenyAccess();
    }
    
    // If user is matched then sign it in
    // Refuse access otherwise
    if (sso_user.getLocalId() != null) {
      sso_user.signIn();
      response.sendRedirect(maestrano.getAfterSsoSignInPath());
    } else {
      response.sendRedirect(maestrano.getSsoUnauthorizedUrl());
    }
    
    // the signature of the SAML Response is valid. The source is trusted
  	//java.io.PrintWriter writer = response.getWriter();
  	//writer.write("OK!");
  	//String nameId = samlResponse.getNameId();
  	//writer.write(nameId);
  	//writer.flush();
	
  } else {

    // the signature of the SAML Response is not valid
  	java.io.PrintWriter writer = response.getWriter();
  	writer.write("There was an error during the authentication process.<br/>");
    writer.write("Please try again. If issue persists please contact support@maestrano.com<br/>");
  	writer.flush();

  }
%>
</body>
</html>