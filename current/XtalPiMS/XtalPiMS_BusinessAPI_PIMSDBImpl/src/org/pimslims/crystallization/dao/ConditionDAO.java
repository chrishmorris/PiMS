/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.SampleComponent;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.crystallization.model.Condition}.
 * </p>
 * 
 * <p>
 * Condition.volatileCondition is derived - a Condition is volatile if one of its Components is volatile. If
 * you want a Condition to be marked as volatile then it must have a Component that is volatile.
 * </p>
 * 
 * <p>
 * The following are as yet unmapped:
 * </p>
 * <ul>
 * <li>TODO Condition.localNumber - remove this attribute</li>
 * <li>TODO Condition.safetyInformation</li>
 * </ul>
 * 
 * @author Jon Diprose
 */
public class ConditionDAO extends
    GenericDAO<org.pimslims.model.sample.RefSample, org.pimslims.business.crystallization.model.Condition> {

    public ConditionDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * <p>
     * Find or create an Organisation to represent the manufacturer of the specified Condition.
     * </p>
     * 
     * @param c
     * @param wv
     * @return
     * @throws ConstraintException
     */
    protected Organisation findOrCreateSupplier(final Condition c, final WritableVersion wv)
        throws ConstraintException {

        Organisation supplier =
            wv.findFirst(Organisation.class, Organisation.PROP_NAME, c.getManufacturerName());
        if (null == supplier) {
            supplier = new Organisation(wv, c.getManufacturerName());
            System.out.println("Created Organisation:" + supplier);
            wv.flush();
        }
        return supplier;

    }

    @Override
    protected void createPORelated(final RefSample pobject, final Condition xobject)
        throws ConstraintException, BusinessException, ModelException {
        // Create SampleComponents
        final ComponentQuantityDAO cqDAO = new ComponentQuantityDAO();
        final List<ComponentQuantity> componentQuantities = xobject.getComponents();
        for (final ComponentQuantity cq : componentQuantities) {

            cqDAO.createSampleComponent(cq, getWritableVersion(), pobject);

        }

        // If we have manufacturer info
        if (null != xobject.getManufacturerName() && xobject.getManufacturerName().length() > 0) {

            // Find or create manufacturer
            final Organisation supplier = findOrCreateSupplier(xobject, getWritableVersion());

            String catalogNum = xobject.getManufacturerCatalogueCode();
            if ((null == catalogNum) || (0 == catalogNum.length())) {
                catalogNum = DAOUtils.UNKNOWN;
            }

            // Build RefSampleSource
            final Map<String, Object> rattr = new HashMap<String, Object>();
            rattr.put(ReagentCatalogueEntry.PROP_CATALOGNUM, catalogNum);
            rattr.put(ReagentCatalogueEntry.PROP_REFSAMPLE, pobject);
            rattr.put(ReagentCatalogueEntry.PROP_PRODUCTGROUPNAME, xobject.getManufacturerScreenName());
            rattr.put(ReagentCatalogueEntry.PROP_PRODUCTNAME, xobject.getManufacturerCode());
            rattr.put(ReagentCatalogueEntry.PROP_SUPPLIER, supplier);

            // Create the RefSampleSource
            new ReagentCatalogueEntry(getWritableVersion(), rattr);

        }

    }

    @Override
    protected Map<String, Object> getFullAttributes(final Condition xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));
        attributes.put(RefSample.PROP_PH, xobject.getFinalpH());
        attributes.put(RefSample.PROP_ISSALTCRYSTAL, xobject.getSaltCondition());
        return attributes;
    }

    @Override
    protected Map<String, Object> getKeyAttributes(final Condition xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(RefSample.PROP_NAME, xobject.getLocalName());
        return attributes;

    }

    @Override
    protected Class<RefSample> getPClass() {
        return RefSample.class;
    }

    @Override
    protected Condition loadXAttribute(final RefSample pobject) throws BusinessException {
        final Condition c = new Condition();

        if (null != pobject.getPh()) {
            c.setFinalpH(pobject.getPh());
        }
        c.setSaltCondition(pobject.getIsSaltCrystal());
        c.setLocalName(pobject.getName());
        return c;
    }

    @Override
    protected Condition loadXRole(final Condition xobject, final RefSample pobject) throws BusinessException {
        // TODO c.setLocalNumber(localNumber);
        // TODO c.setSafetyInformation(safetyInformation);

        final Set<ReagentCatalogueEntry> refSampleSources = pobject.getRefSampleSources();
        if (!refSampleSources.isEmpty()) {
            final ReagentCatalogueEntry rss = refSampleSources.iterator().next();
            xobject.setManufacturerCatalogueCode(rss.getCatalogNum());
            xobject.setManufacturerScreenName(rss.getProductGroupName());
            xobject.setManufacturerCode(rss.getProductName());
            xobject.setManufacturerName(rss.getSupplier().getName());
        }

        final ComponentQuantityDAO cqDAO = new ComponentQuantityDAO();
        final List<ComponentQuantity> componentQuantities = new ArrayList<ComponentQuantity>();
        final Set<SampleComponent> components = pobject.getSampleComponents();
        for (final SampleComponent sc : components) {

            final ComponentQuantity cq = cqDAO.populateFromSampleComponent(sc);
            componentQuantities.add(cq);

            if (cq.getComponent().isVolatileComponent()) {
                xobject.setVolatileCondition(true);
            }

        }
        xobject.setComponents(componentQuantities);

        return xobject;
    }

    @Override
    protected void updatePORelated(final RefSample object, final Condition xobject) throws ModelException {
        // TODO Auto-generated method stub

    }

}
