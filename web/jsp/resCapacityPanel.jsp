<%@ taglib uri="/WEB-INF/lib/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/lib/struts-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lib/display" prefix="display" %>
<%@ taglib uri="/WEB-INF/lib/plandora-html" prefix="plandora-html" %>
<%@ taglib uri="/WEB-INF/lib/struts-logic" prefix="logic" %>

<jsp:include page="header.jsp" />

<script language="javascript">

    function changeType() {
	  	buttonClick("resCapacityPanelForm", "refresh");    	    
    }

    function refreshBody() {
	  	buttonClick("resCapacityPanelForm", "refreshBody");    	    
    }

    function editCap(sinceDt, projectId, resourceId, canRemoveCap) {
	  	displayMessage("../do/showResCapacityEdit?operation=showEditPopup&sinceDate=" + sinceDt + "&editProjectId=" + projectId + "&editResourceId=" + resourceId + "&canRemoveCap=" + canRemoveCap, 380, 210);    	    
    }
        
	function showIconEdit(argId){
		var el = document.getElementById( argId );
		if (el!=null) {
			el.style.display =  'block'; 
			behidden = false;  
		}	
	}
		
	function hideIconEdit(argId){
		var el = document.getElementById( argId );
		if (el!=null) {
			el.style.display =  'none' ; 
			behidden = true ;  
		}		
	}    
    
</script>

<html:form action="showResCapacityPanel">
	<html:hidden name="resCapacityPanelForm" property="operation"/>
	<html:hidden name="resCapacityPanelForm" property="id"/>
	<html:hidden name="resCapacityPanelForm" property="showEditCapacity"/>
	
	<plandora-html:shortcut name="resCapacityPanelForm" property="goToCapacityForm" fieldList="projectId"/>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td width="10">&nbsp;</td><td>
	
		<display:headerfootergrid width="100%" type="HEADER">
			<bean:message key="title.resCapacity"/>
		</display:headerfootergrid>
			
		<table width="97%" border="0" cellspacing="0" cellpadding="0">
		<tr class="gapFormBody">
		  <td colspan="5">&nbsp;</td>
		</tr>
	   
		<tr class="pagingFormBody">
		  <td width="10">&nbsp;</td>
		  <td width="120" class="formTitle"><bean:message key="label.resCapacity.type"/>:&nbsp;</td>
		  <td class="formBody" colspan="3">
				<html:select name="resCapacityPanelForm" property="type" styleClass="textBox" onkeypress="javascript:changeType();" onchange="javascript:changeType();">
					<html:options collection="resCapacityTypes" property="id" labelProperty="genericTag" filter="false"/>
				</html:select>
				
				<logic:equal name="resCapacityPanelForm" property="type" value="PRJ">
					<html:select name="resCapacityPanelForm" property="projectId" styleClass="textBox" onkeypress="javascript:refreshBody();" onchange="javascript:refreshBody();">
						<html:options collection="resCapacityList" property="id" labelProperty="name" filter="false"/>
					</html:select>
					<html:hidden name="resCapacityPanelForm" property="resourceId"/>			
				</logic:equal>

				<logic:equal name="resCapacityPanelForm" property="type" value="RES">
					<html:select name="resCapacityPanelForm" property="resourceId" styleClass="textBox" onkeypress="javascript:refreshBody();" onchange="javascript:refreshBody();">
						<html:options collection="resCapacityList" property="id" labelProperty="name" filter="false"/>
					</html:select>
					<html:hidden name="resCapacityPanelForm" property="projectId"/>			
				</logic:equal>
		  </td>
		</tr>
		
		<tr class="formBody">
			<td>&nbsp;</td>	
			<td class="formTitle"><bean:message key="label.resCapacity.initialDate"/>:&nbsp;</td>
			<td width="120"><plandora-html:calendar name="resCapacityPanelForm" property="initialDate" styleClass="textBoxDisabled" /></td>
			<td width="100" class="formTitle"><bean:message key="label.resCapacity.FinalDate"/>:&nbsp;</td>		
			<td><plandora-html:calendar name="resCapacityPanelForm" property="finalDate" styleClass="textBoxDisabled" /></td>
		</tr> 

		<tr class="formBody">
			<td>&nbsp;</td>	
			<td class="formTitle"><bean:message key="label.resCapacity.gran"/>:&nbsp;</td>		
			<td colspan="3">
				<html:select name="resCapacityPanelForm" property="granularity" styleClass="textBox" onkeypress="javascript:refreshBody();" onchange="javascript:refreshBody();">
					<html:options collection="resCapacityGran" property="id" labelProperty="genericTag" filter="false"/>
				</html:select>			
			</td>
		</tr> 

		<tr class="formBody">
			<td>&nbsp;</td>	
			<td class="formTitle"><bean:message key="label.resCapacity.viewMode"/>:&nbsp;</td>		
			<td colspan="3">
				<html:select name="resCapacityPanelForm" property="viewMode" styleClass="textBox" onkeypress="javascript:refreshBody();" onchange="javascript:refreshBody();">
					<html:options collection="resCapacityModes" property="id" labelProperty="genericTag" filter="false"/>
				</html:select>			
			</td>
		</tr> 

		<tr class="formBody">
			<td>&nbsp;</td>	
			<td>&nbsp;</td>
			<td class="formBody" colspan="3">
					<html:checkbox property="hideDisabledUsers" name="resCapacityPanelForm" >
						<bean:message key="label.resCapacity.igdisable"/>
					</html:checkbox>
			</td>
		</tr> 

		<tr class="formBody">
			<td>&nbsp;</td>	
			<td>&nbsp;</td>
			<td class="formBody" colspan="3">
					<html:checkbox property="hideZeroValues" name="resCapacityPanelForm" >
						<bean:message key="label.resCapacity.igzeros"/>
					</html:checkbox>
			</td>
		</tr> 
		
		
			
			
		<tr class="gapFormBody">
		  <td colspan="5">&nbsp;</td>
		</tr> 
		</table>

		<display:headerfootergrid type="FOOTER">			
			<table width="97%" border="0" cellspacing="0" cellpadding="0"><tr>
			  <td width="120">
				  <html:button property="refresh" styleClass="button" onclick="javascript:buttonClick('resCapacityPanelForm', 'refresh');">
					<bean:message key="button.refresh"/>
				  </html:button>    
			  </td>
			  <td>&nbsp;</td>
			</tr>
		  </table>
		</display:headerfootergrid> 
		
	</td><td width="10">&nbsp;</td>
	</tr></table>

	<div>&nbsp;</div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td width="10">&nbsp;</td><td>	
	
		<display:headerfootergrid width="100%" type="HEADER">
			<logic:equal name="resCapacityPanelForm" property="type" value="PRJ">
				<bean:message key="title.resCapacity.title.1"/>		
			</logic:equal>
			<logic:equal name="resCapacityPanelForm" property="type" value="RES">
				<bean:message key="title.resCapacity.title.2"/>
			</logic:equal>
			<bean:write name="resCapacityPanelForm" property="elementLabel" filter="false"/>
		</display:headerfootergrid>

		<table width="98%" id="cap_panel" border="0" cellspacing="0" cellpadding="0">
		<tr class="gapFormBody">
		  <td colspan="4">&nbsp;</td>
		</tr>
		</table>	

		<table width="98%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		  <td valign="top">
				<bean:write name="resCapacityPanelForm" property="capacityHtmlTitle" filter="false" />
		  </td>
		  <td valign="top">
				<div id="capacity_plan" style="width:950px; overflow-x: scroll; overflow-y: hidden;">
					<bean:write name="resCapacityPanelForm" property="capacityHtmlBody" filter="false" />
					<br>
				</div>      
		  </td>
		</tr>
		</table>	
	
	
		<display:headerfootergrid type="FOOTER">				
			<table width="98%" border="0" cellspacing="0" cellpadding="0"><tr>
			<td width="120">      
			  <html:button property="showEditPanel" styleClass="button" onclick="javascript:buttonClick('resCapacityPanelForm', 'showEditPanel');">
				<bean:message key="label.resCapacity.new"/>
			  </html:button>    
			</td>		
			<td>&nbsp;</td>
			<td width="120">
			  <html:button property="backward" styleClass="button" onclick="javascript:buttonClick('resCapacityPanelForm', 'backward');">
				<bean:message key="button.backward"/>
			  </html:button>    
			</td>		
			</tr></table>  	
		</display:headerfootergrid>

  </td><td width="10">&nbsp;</td>
  </tr></table>
  
</html:form>

<jsp:include page="footer.jsp" />

<!-- End of source-code -->
<script> 
	with(document.forms["resCapacityPanelForm"]){	
		if (showEditCapacity.value=='on') {
			displayMessage("../do/showResCapacityEdit?operation=showInsertPopup", 380, 210);
		}
		document.getElementById("capacity_plan").style.width = screen.width - 280;
	}
</script> 