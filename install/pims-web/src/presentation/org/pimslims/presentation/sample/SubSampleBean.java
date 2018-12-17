package org.pimslims.presentation.sample;

import org.pimslims.model.sample.Sample;

/**
 * Represents a sample in a list on a JSP page.
 * 
 * @see org.pimslims.model.sample.Sample
 * @author Marc Savitsky
 * 
 */

@Deprecated
// obsolete
public class SubSampleBean extends SampleBean {

    public SubSampleBean(final Sample sample) {
        super(sample);
    }

    /**
     * SubSampleBean.getMenu
     * 
     * @return
     */
    @Override
    public String getMenu() {
        String ret =
            "properties:[ { property:\'Name\', val:contextMenuName}" + this.getExtraContextMenuProperties()
                + " ],\r\n" + "actions:[ {text:\'View\', icon:\'actions/view.gif\', url:'/View/"
                + this.getHook() + "' } "
                + ",{text:'Remove', icon:'actions/next.gif',\r\n onclick:'submitRemove(contextMenuName,\\\'"
                + this.getHook() + "\\\')',\r\n url:'/Move/" + this.getHook() + "' }";

        if (this.getMayDelete()) {
            ret += this.extraActions;
            ret +=
                ",{text:'Delete', icon:'actions/delete.gif',\r\n onclick:'contextmenu_delete(contextMenuName,\\\'"
                    + this.getHook() + "\\\')',\r\n url:'/Delete/" + this.getHook() + "' }";
        }
        return ret + "]";
    }

}
