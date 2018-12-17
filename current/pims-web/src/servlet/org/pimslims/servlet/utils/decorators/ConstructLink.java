package org.pimslims.servlet.utils.decorators;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.pimslims.model.experiment.Experiment;

/**
 * @author Petr Troshin To find uses of this, search *.jsp for 'decorator=' Date July 2008
 */
public class ConstructLink implements org.displaytag.decorator.DisplaytagColumnDecorator {

    /**
     * 
     */
    public ConstructLink() {
        super();
    }

    /**
     * DisplaytagColumnDecorator.decorate
     * 
     * @see org.displaytag.decorator.DisplaytagColumnDecorator#decorate(java.lang.Object,
     *      javax.servlet.jsp.PageContext, org.displaytag.properties.MediaTypeEnum)
     */
    public Object decorate(final Object construct, final PageContext context, final MediaTypeEnum mediaType)
        throws DecoratorException {

        if (construct == null) {
            return "";
        }
        if (!(construct instanceof Experiment)) {
            return "";
        }

        final Experiment entryClExp = (Experiment) construct;
        String link = "<a href='" + ((HttpServletRequest) context.getRequest()).getContextPath();
        link +=
            "/Construct/" + entryClExp.get_Hook() + "' title='view Entry Clone / Expression construct' >"
                + entryClExp.getName() + " </a>";

        return link;
    }
}
