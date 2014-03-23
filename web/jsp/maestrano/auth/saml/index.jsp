<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.maestrano.core.*,com.maestrano.app.config.*" %>
<%@ page import="com.maestrano.lib.onelogin.saml.*,com.maestrano.lib.onelogin.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Auth Request</title>
<%
  
  // Get the settings
  MnoSettings mnoSettings = new MnoSettings(AppConfigurator.getInstance(request), MnoConfigurator.getInstance());
  
  // Get the Maestrano Service
  MaestranoService.configure(mnoSettings);
  MaestranoService maestrano = MaestranoService.getInstance();
  
  // the appSettings object contain application specific settings used by the SAML library
  AppSettings appSettings = new AppSettings();

  // set the URL of the consume.jsp (or similar) file for this app. The SAML Response will be posted to this URL
  appSettings.setAssertionConsumerServiceUrl(maestrano.getSettings().getSsoReturnUrl());

  // set the issuer of the authentication request. This would usually be the URL of the issuing web application
  appSettings.setIssuer(maestrano.getSettings().getAppName());
  
  // the accSettings object contains settings specific to the users account. 
  // At this point, your application must have identified the users origin
  AccountSettings accSettings = new AccountSettings();

  // The URL at the Identity Provider where to the authentication request should be sent
  accSettings.setIdpSsoTargetUrl(maestrano.getSettings().getSsoUrl());
  
  // Generate an AuthRequest and send it to the identity provider
  AuthRequest authReq = new AuthRequest(appSettings, accSettings);
  String reqString = accSettings.getIdp_sso_target_url()+"?SAMLRequest=" + AuthRequest.getRidOfCRLF(URLEncoder.encode(authReq.getRequest(AuthRequest.base64),"UTF-8"));
  response.sendRedirect(reqString);
%>
</head>
<body>
</body>
</html>