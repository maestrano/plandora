package com.maestrano.core.sso;

public class SessionJsonData {
  private Boolean valid;
  private String recheck;
  
  public Boolean getValid() { return this.valid; }
  public String getRecheck() { return this.recheck; }

  public void setValid(Boolean valid) { this.valid = valid; }
  public void setRecheck(String recheck) { this.recheck = recheck; }

  public String toString() {
    return String.format("mno_uid:%s,mno_session:%s", this.valid, this.recheck);
  }
}