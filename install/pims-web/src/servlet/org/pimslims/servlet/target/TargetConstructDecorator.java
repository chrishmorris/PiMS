/*
 * Created on April 2007 - Code Style - Code Templates
 */
package org.pimslims.servlet.target;

import org.displaytag.decorator.TableDecorator;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * @author Petr Troshin
 * 
 *         These methods must override the ones in the Beans in order to be called Class is intended to be
 *         used for the decoration purpose only
 */
@Deprecated
// beleived not used
public class TargetConstructDecorator extends TableDecorator {

    /**
     * 
     */
    public TargetConstructDecorator() {
        super();
    }

    /*
     * public String getCommonName() { TargetConstructBean tb =
     * (TargetConstructBean)this.getCurrentRowObject(); String contextPath =
     * ((HttpServletRequest)this.getPageContext().getRequest()).getContextPath(); String link = "<a
     * href='"+contextPath+"/View/" + tb.target.get_Hook() + "' >" + tb.getCommonName() + "</a>"; return
     * link; }
     */
    /*
     * This also can be done using pims:sequence tag - see protein formatting on the
     * browseTargetConstructs.JSP
     */
    public String getDna() {
        final TargetConstructBean tb = (TargetConstructBean) this.getCurrentRowObject();
        return ValueFormatter.getFormatedSequence(tb.getDna());
    }

} // class
