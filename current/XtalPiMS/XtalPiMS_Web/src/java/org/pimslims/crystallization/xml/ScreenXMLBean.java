package org.pimslims.crystallization.xml;

import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.view.ConditionViewDAO;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderSource;

@XmlRootElement(name = "screen")
@XmlType(name = "", propOrder = { "screenName", "screenSupplier", "screenCatNum", "screenDetails",
    "screenNumberOfSolutions", "conditions" })
public class ScreenXMLBean {

    String screenName;

    String screenSupplier;

    String screenCatNum;

    String screenDetails;

    int screenNumberOfSolutions;

    private Collection<ConditionXMLBean> conditions;

    public ScreenXMLBean(final RefHolder screen) throws BusinessException {
        this.screenName = screen.getName();
        this.screenDetails = screen.getDetails();
        final RefHolderSource rss = screen.getRefHolderSources().iterator().next();
        if (null != rss) {
            screenSupplier = (rss.getSupplier().getName());
            screenCatNum = rss.getCatalogNum();
        }
        this.screenNumberOfSolutions = screen.getRefSamplePositions().size();
        //conditions
        final ConditionViewDAO conditionViewDAO = new ConditionViewDAO(screen.get_Version());
        final BusinessCriteria criteria = new BusinessCriteria(conditionViewDAO);
        criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, screenName, true));
        final Collection<ConditionView> conditionViews = conditionViewDAO.findViews(criteria);
        assert conditionViews.size() == screenNumberOfSolutions;
        conditions = new LinkedList<ConditionXMLBean>();
        for (final ConditionView conditionView : conditionViews) {
            conditions.add(new ConditionXMLBean(conditionView));
        }
    }

    /**
     * @return Returns the screenName.
     */
    @XmlElement
    public String getScreenName() {
        return screenName;
    }

    /**
     * @return Returns the screenSupplier.
     */
    @XmlElement
    public String getScreenSupplier() {
        return screenSupplier;
    }

    /**
     * @return Returns the screenCatNum.
     */
    @XmlElement
    public String getScreenCatNum() {
        return screenCatNum;
    }

    /**
     * @return Returns the screenDetails.
     */
    @XmlElement
    public String getScreenDetails() {
        return screenDetails;
    }

    /**
     * @return Returns the screenNumberOfSolutions.
     */
    @XmlElement
    public int getScreenNumberOfSolutions() {
        return screenNumberOfSolutions;
    }

    // Required by jaxb
    @SuppressWarnings("unused")
    private ScreenXMLBean() {
        // Required by jaxb
    }

    @XmlElementWrapper(name = "solutions")
    @XmlElement(name = "solution")
    public Collection<ConditionXMLBean> getConditions() {
        return conditions;
    }

    public void setConditions(final Collection<ConditionXMLBean> conditions) {
        this.conditions = conditions;
    }
}
