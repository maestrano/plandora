package com.pandora.gui.taglib.decorator;

import org.apache.taglibs.display.ColumnDecorator;

import com.pandora.LeaderTO;
import com.pandora.UserTO;
import com.pandora.delegate.UserDelegate;
import com.pandora.helper.HtmlUtil;

public class CancelTaskDecorator extends ColumnDecorator {

	public String decorate(Object columnValue) {
		String image = "&nbsp;";
		
		UserTO uto = (UserTO)this.getSession().getAttribute(UserDelegate.CURRENT_USER_SESSION);		
		if (uto instanceof LeaderTO) {
			String altValue = this.getBundleMessage("label.refuse.cancTask");
		    image ="<a href=\"#\" onclick=\"displayMessage('../do/refuse?operation=prepareForm&forwardAfterRefuse=goToTaskForm&refuseType=TSK&refusedId=" + columnValue + "', 475, 220);return false;\" border=\"0\"> \n";
			image += "<img border=\"0\" " + HtmlUtil.getHint(altValue) + " src=\"../images/stop.gif\" >";
			image += "</a>";		    
		}
		return image;
	}

	public String decorate(Object columnValue, String tag) {
        return decorate(columnValue);
	}

	public String contentToSearching(Object columnValue) {
    	return columnValue+"";
	}

}
