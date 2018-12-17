/*
 * Created on 02.09.2005 Code Style - Code Templates
 */
package org.pimslims.servlet.target;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.decorator.TableDecorator;
import org.pimslims.presentation.TargetBeanForLists;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * @author Petr Troshin
 * 
 * 
 */
public class TargetDecorator extends TableDecorator {

    /**
     * 
     */
    public TargetDecorator() {
        super();
    }

    @Deprecated
    // no longer shown in JSPs
    public String getGeneName() {
        final String lName = ((TargetBeanForLists) this.getCurrentRowObject()).getCommonName();
        /*
         * if(lName != null && lName.length() > 1) { lName = lName.substring(0,1).toLowerCase() +
         * lName.substring(1).toUpperCase(); }
         */
        return lName;
    }

    public String getLink() {
        // int index = this.getListIndex();
        final TargetBeanForLists tb = (TargetBeanForLists) this.getCurrentRowObject();
        final String contextPath = ((HttpServletRequest) this.getPageContext().getRequest()).getContextPath();
        final String link =
            "<a href='" + contextPath + "/View/" + tb.getHook() + "'>" + " <img class='icon' src='"
                + contextPath + "/skins/default/images/icons/actions/view.gif' />" + "</a>";
        return link;
    }

    public String getCommonName() {
        final TargetBeanForLists tb = (TargetBeanForLists) this.getCurrentRowObject();
        return tb.getCommonName();
    }

    public String getStatusDateString() {
        final TargetBeanForLists tb = (TargetBeanForLists) this.getCurrentRowObject();
        final Calendar stdate = tb.getStatusDate();
        final String stringDate = ValueFormatter.formatDate(stdate);
        return stringDate;
    }

} // class
