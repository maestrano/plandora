package com.maestrano.app.config;

public class MnoConfigurator {
  
  private static MnoConfigurator instance = null;
  
  public String ssoUrl;
  public String ssoSessionCheckUrl;
  public String ssoAccessUnauthorizedUrl;
  public String ssoAccessLogoutUrl;
  public Boolean ssoIntranetMode;
  public String ssoX509Certificate;
  
  public MnoConfigurator() {
    // Get Maestrano SSO Host
    String mnoSsoHost = "http://localhost:3000";

    // Endpoint to reach for SSO Identification
    this.ssoUrl = mnoSsoHost + "/api/v1/auth/saml";

    // Endpoint to reach for session information (/api/v1/auth/saml/user-xyz?session=df4sd4g3fd345sfgd534)
    this.ssoSessionCheckUrl = mnoSsoHost + "/api/v1/auth/saml";

    // Access unauthorized page
    this.ssoAccessUnauthorizedUrl = mnoSsoHost + "/appAccessUnauthorized";

    // Access Logout page
    this.ssoAccessLogoutUrl = mnoSsoHost + "/appLogout";

    // Intranet Mode - If enabled then ALL pages require authentication
    this.ssoIntranetMode = false;

    // Maestrano X509 Certificate
    this.ssoX509Certificate = "(MIIDezCCAuSgAwIBAgIJAOehBr+YIrhjMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD)"+
    "(VQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT)"+
    "(EU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ)"+
    "(KoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyMjM5)"+
    "(WhcNMzMxMjMwMDUyMjM5WjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP)"+
    "(MA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG)"+
    "(A1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz)"+
    "(dHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVkIqo5t5Paflu)"+
    "(P2zbSbzxn29n6HxKnTcsubycLBEs0jkTkdG7seF1LPqnXl8jFM9NGPiBFkiaR15I)"+
    "(5w482IW6mC7s8T2CbZEL3qqQEAzztEPnxQg0twswyIZWNyuHYzf9fw0AnohBhGu2)"+
    "(28EZWaezzT2F333FOVGSsTn1+u6tFwIDAQABo4HuMIHrMB0GA1UdDgQWBBSvrNxo)"+
    "(eHDm9nhKnkdpe0lZjYD1GzCBuwYDVR0jBIGzMIGwgBSvrNxoeHDm9nhKnkdpe0lZ)"+
    "(jYD1G6GBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE)"+
    "(BxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN)"+
    "(bWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u)"+
    "(Y29tggkA56EGv5giuGMwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCc)"+
    "(MPgV0CpumKRMulOeZwdpnyLQI/NTr3VVHhDDxxCzcB0zlZ2xyDACGnIG2cQJJxfc)"+
    "(2GcsFnb0BMw48K6TEhAaV92Q7bt1/TYRvprvhxUNMX2N8PHaYELFG2nWfQ4vqxES)"+
    "(Rkjkjqy+H7vir/MOF3rlFjiv5twAbDKYHXDT7v1YCg==)";
  }
  
  /**
   * Returns an instance of this class
   * (this class uses the singleton pattern)
   */
  public static MnoConfigurator getInstance()
  {
      if (instance == null) {
        instance = new MnoConfigurator();
      }
      return instance;
  }
}