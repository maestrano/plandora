package com.maestrano.core.sso;

class SessionJsonData {
  private String valid;
  private Boolean recheck;
  
  public String getvalid() { return valid; }
  public String getRecheck() { return recheck; }

  public void setvalid(String valid) { this.valid = valid; }
  public void setRecheck(String recheck) { this.recheck = recheck; }

  public String toString() {
    return String.format("mno_uid:%s,mno_session:%s", valid, recheck);
  }
}