package com.pandora.gui.struts.form;

import com.pandora.SurveyTO;

public class ShowSurveyForm extends GeneralStrutsForm {

	private static final long serialVersionUID = 1L;
	
	private String questionsBody = "";

	private String surveyTitle;
	
	private String surveyDescription;
	
	private boolean isAnonymous;
	
	private boolean allowAnonymous;
	
	private String key;
	
	private boolean show;
	
	private String questionid;
	
	private boolean showMandatoryNote = false;
	
	private SurveyTO currentSurvey;
	
	private String conectedUser;
	
	
	private String questionContent;
	
	private String questionSummary;
	
	private String htmlList;
	
	private String anonymousUri;
		
	private String pathContext;
	
	
	public String getProjectId() {
		return currentSurvey.getProject().getId();
	}

	
	//////////////////////////////////////////	
	public String getSurveyTitle() {
		return surveyTitle;
	}
	public void setSurveyTitle(String newValue) {
		this.surveyTitle = newValue;
	}
	
	
	//////////////////////////////////////////		
	public String getSurveyDescription() {
		return surveyDescription;
	}
	public void setSurveyDescription(String newValue) {
		this.surveyDescription = newValue;
	}


	//////////////////////////////////////////
	public String getQuestionsBody() {
		return questionsBody;
	}
	public void setQuestionsBody(String newValue) {
		this.questionsBody = newValue;
	}

	
	//////////////////////////////////////////
	public boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(boolean newValue) {
		this.isAnonymous = newValue;
	}

	
	//////////////////////////////////////////
	public boolean isAllowAnonymous() {
		return allowAnonymous;
	}
	public void setAllowAnonymous(boolean newValue) {
		this.allowAnonymous = newValue;
	}


	//////////////////////////////////////////
	public String getKey() {
		return key;
	}
	public void setKey(String newValue) {
		this.key = newValue;
	}

	
	//////////////////////////////////////////
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean newValue) {
		this.show = newValue;
	}

	
	//////////////////////////////////////////
	public String getQuestionid() {
		return questionid;
	}
	public void setQuestionid(String newValue) {
		this.questionid = newValue;
	}

	
	//////////////////////////////////////////
	public boolean isShowMandatoryNote() {
		return showMandatoryNote;
	}
	public void setShowMandatoryNote(boolean newValue) {
		this.showMandatoryNote = newValue;
	}

	
	//////////////////////////////////////////
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String newValue) {
		this.questionContent = newValue;
	}
	
	
	//////////////////////////////////////////
	public String getQuestionSummary() {
		return questionSummary;
	}
	public void setQuestionSummary(String newValue) {
		this.questionSummary = newValue;
	}
	
	public SurveyTO getCurrentSurvey() {
		return currentSurvey;
	}
	public void setCurrentSurvey(SurveyTO newValue) {
		this.currentSurvey = newValue;
	}

	
	//////////////////////////////////////////
	public String getHtmlList(){
		return htmlList;
	}
	public void setHtmlList(String newValue){
		htmlList = newValue;
	}

	
	//////////////////////////////////////////
	public String getConectedUser() {
		return conectedUser;
	}
	public void setConectedUser(String newValue) {
		this.conectedUser = newValue;
	}


	//////////////////////////////////////////
	public String getAnonymousUri() {
		String uri = "";
		if (this.allowAnonymous && this.anonymousUri!=null && !this.anonymousUri.trim().equals("")) {
			uri = this.pathContext + "/do/showSurvey?operation=anonymous&key=" + this.anonymousUri;
		}
		return uri;		
	}
	public void setAnonymousUri(String newValue) {
		this.anonymousUri = newValue;
	}

	
	public void setPathContext(String fullUrl, String contextApp) {
		this.pathContext=null;
		if (fullUrl!=null && contextApp!=null) {
			int i = fullUrl.indexOf(contextApp);
			if (i>0) {
				this.pathContext = fullUrl.substring(0, i + contextApp.length());	
			}
		}
		if (this.pathContext==null) {
			this.anonymousUri="";
		}
	}
	
	
}
