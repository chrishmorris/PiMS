/**
 * pims-web-current RecordLink.java
 * 
 * @author Ed Daniel
 * @date 01-Oct-2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 ejd53
 * 
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * RecordLink
 * 
 */
@Deprecated
// obsolete
public class RecordLink extends PIMSTag {

    private static class LinkFormatter {

        /**
         * the path to the image
         */
        private final String icon;

        public LinkFormatter(final String icon) {
            super();
            this.icon = icon;
        }

        public final String getIcon() {
            return this.icon;
        }

        /**
         * Override this if there are extra links
         * 
         * @param hook
         * @return extra links
         */
        String getExtraLinks(final String hook, final String path) {
            return "";
        }
    }

    private static final Map<String, LinkFormatter> FORMATTERS = new HashMap();
    static {

        RecordLink.FORMATTERS.put(Sample.class.getName(), new LinkFormatter("types/sample.gif") {
            @Override
            public String getExtraLinks(final String hook, final String path) {
                return RecordLink.makeMenuItem("/Graph/" + hook, "actions/viewdiagram.gif", "View diagram",
                    path) + ",";
            }
        });

        RecordLink.FORMATTERS.put(Experiment.class.getName(), new LinkFormatter("types/experiment.gif") {
            @Override
            public String getExtraLinks(final String hook, final String path) {
                return RecordLink.makeMenuItem("/Graph/" + hook, "actions/viewdiagram.gif", "View diagram",
                    path) + ",";
            }
        });

        RecordLink.FORMATTERS.put(Protocol.class.getName(), new LinkFormatter("types/protocol.gif") {
            @Override
            public String getExtraLinks(final String hook, final String path) {
                return "";
                //TODO Onclick handler for POST to CreateExperiment                
                //                return  makeMenuItem("/Create/org.pimslims.model.experiment.Experiment?protocol="+hook, "actions/create/experiment.gif", "New experiment", path)+",";
            }

        });

        RecordLink.FORMATTERS.put(Target.class.getName(), new LinkFormatter("types/target.gif") {
            @Override
            public String getExtraLinks(final String hook, final String path) {
                return RecordLink.makeMenuItem("/Graph/" + hook, "actions/viewdiagram.gif", "View diagram",
                    path)
                    + ","
                    //TODO only correct for an ORF, should sometimes be "/DNAConstructWizard/"
                    + RecordLink.makeMenuItem("/spot/SpotNewConstructWizard/" + hook + "?wizard_step=1",
                        "actions/create/construct.gif", "New construct...", path) + ",";
            }
        });

        RecordLink.FORMATTERS
            .put(ResearchObjective.class.getName(), new LinkFormatter("types/construct.gif"));

        RecordLink.FORMATTERS.put(ExperimentGroup.class.getName(), new LinkFormatter("types/plate.gif") {
            @Override
            public String getExtraLinks(final String hook, final String path) {
                return RecordLink.makeMenuItem("/Graph/" + hook, "actions/viewdiagram.gif", "View diagram",
                    path) + ",";
            }
        });

        RecordLink.FORMATTERS.put(Person.class.getName(), new LinkFormatter("types/person.gif"));

        RecordLink.FORMATTERS.put(Location.class.getName(), new LinkFormatter("types/location.gif") {
            @Override
            public String getExtraLinks(final String hook, final String path) {
                return RecordLink.makeMenuItem("/Graph/" + hook, "actions/viewdiagram.gif", "View diagram",
                    path) + ",";
            }
        });

        RecordLink.FORMATTERS.put(Holder.class.getName(), new LinkFormatter("types/holder.gif"));
    }

    private LinkFormatter getFormatter(final String className) {
        if (!RecordLink.FORMATTERS.containsKey(className)) {
            return new LinkFormatter("types/blank.gif");
        }
        return RecordLink.FORMATTERS.get(className);

    }

    private ModelObjectShortBean bean;

    /**
     * Formats a link for inclusion in the context menu.
     * 
     * @param url The URL to go to on clicking the link
     * @param icon The icon to label the link, e.g.,"actions/delete.gif". This is assumed to be in
     *            /images/icons/.
     * @param label The text label of the link
     */
    static String makeMenuItem(final String page, final String icon, final String label, final String path) {
        return RecordLink.makeMenuItem(page, icon, label, null, path);
    }

    /**
     * Formats a link with an onclick handler for inclusion in the context menu.
     * 
     * @param url The URL to go to on clicking the link
     * @param icon The icon to label the link, e.g.,"actions/delete.gif". This is assumed to be in
     *            /images/icons/.
     * @param label The text label of the link
     * @param onclick The Javascript onclick handler
     */
    static String makeMenuItem(final String page, final String icon, final String label,
        final String onclick, final String path) {
        final StringBuffer item = new StringBuffer();
        item.append("{url:'").append(path).append(page).append("',icon:'").append(icon).append("',label:'")
            .append(label).append("'");
        if (null != onclick) {
            item.append(",onclick:'").append(onclick.replace("'", "\\'")).append("'");
        }
        item.append("}");
        return item.toString();
    }

    /**
     * 
     */
    @Override
    public int doStartTag() throws JspException {
        final String path = this.getPath();

        final String hook = this.bean.getHook();
        final String name = StringEscapeUtils.escapeHtml(this.bean.getName());
        final String className = this.bean.getClassName();
        final String displayName = this.bean.getClassDisplayName();

        final LinkFormatter formatter = this.getFormatter(className);

        //TODO There must be a neater way - put all menu items into a String[] and 
        //concatenate with "'"?
        String menuItems = "";
        menuItems +=
            RecordLink.makeMenuItem("/View/" + hook, "actions/view.gif", "View", this.getPath()) + ",";
        menuItems += formatter.getExtraLinks(hook, path);
        if (this.bean.getMayDelete()) {
            menuItems +=
                RecordLink.makeMenuItem("/Delete/" + hook, "actions/delete.gif", "Delete",
                    "contextmenu_delete('" + name + "','" + hook + "', displayName)", this.getPath());
        }

        final StringBuffer html = new StringBuffer();
        html.append("<span class=\"recordlink\">");
        html.append(formatter.getIcon()).append("\" alt=\"").append(displayName).append("\" title=\"")
            .append(displayName).append("\" />");
        html.append("<a href=\"").append(path).append("/View/").append(hook).append("\">").append(name)
            .append("</a>");
        html.append("</span>");
        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(html);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    /**
     * 
     */

    @Override
    public int doEndTag() throws JspException {
        String html = "";
        html += "";

        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(html);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return super.doEndTag();
    }

    /**
     * Returns the ModelObjectBean representing the object to be linked to.
     */
    public ModelObjectShortBean getBean() {
        return this.bean;
    }

    /**
     * Sets the bean for which the link should be written.
     * 
     * @param bean A ModelObjectBean representing the object to be linked to.
     */
    public void setBean(final ModelObjectShortBean bean) {
        this.bean = bean;
    }

}
