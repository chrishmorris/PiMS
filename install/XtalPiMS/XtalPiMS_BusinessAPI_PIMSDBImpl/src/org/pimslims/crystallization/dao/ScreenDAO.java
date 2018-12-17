/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.logging.Logger;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.ScreenType;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderSource;
import org.pimslims.model.holder.RefSamplePosition;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.search.Paging;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.crystallization.model.Screen}.
 * </p>
 * 
 * @author Bill Lin
 */
public class ScreenDAO extends GenericDAO<RefHolder, Screen> {

    public ScreenDAO(final ReadableVersion version) {
        super(version);
    }

    private static final Logger log = Logger.getLogger(ScreenDAO.class);

    /**
     * <p>
     * The name of the Screen HolderCategory.
     * </p>
     */
    public static final String HOLDER_CATEGORY_NAME = "Screen";

    /**
     * <p>
     * The name of the Additive HolderCategory.
     * </p>
     */
    public static final String ADDITIVE = "Additive";

    /**
     * <p>
     * The name of the Optimization HolderCategory.
     * </p>
     */
    public static final String OPTIMIZATION = "Optimization";

    /**
     * <p>
     * The name of the SparseMatrix HolderCategory.
     * </p>
     */
    public static final String SPARSE_MATRIX = "SparseMatrix";

    public static Collection<HolderCategory> getScreenHolderCategory(final ReadableVersion version,
        final ScreenType screenType) {
        final HolderCategory screenCategory = DAOUtils.findHolderCategory(version, HOLDER_CATEGORY_NAME);
        if (null == screenCategory) {
            throw new AssertionError("no " + HOLDER_CATEGORY_NAME
                + " category - xtalPiMS ref data not installed?");
        }
        final Set<HolderCategory> categories = new HashSet<HolderCategory>();
        categories.add(screenCategory);

        String typeName = null;
        if (screenType != null) {
            if (ScreenType.Matrix.equals(screenType)) {
                typeName = SPARSE_MATRIX;
            } else if (ScreenType.Optimisation.equals(screenType)) {
                typeName = OPTIMIZATION;
            } else if (ScreenType.Additive.equals(screenType)) {
                typeName = ADDITIVE;
            }
        }

        if (typeName != null) {
            final HolderCategory category = DAOUtils.findHolderCategory(version, typeName);
            if (null == screenCategory) {
                throw new RuntimeException("no " + typeName + " category - xtalPiMS ref data not installed?");
            }
            categories.add(category);
        }
        return categories;
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
    protected Organisation findOrCreateSupplier(final Screen s, final WritableVersion wv)
        throws ConstraintException {

        Organisation supplier =
            wv.findFirst(Organisation.class, Organisation.PROP_NAME, s.getManufacturerName());
        if (null == supplier) {
            supplier = new Organisation(wv, s.getManufacturerName());
            System.out.println("Created Organisation:" + supplier);
            wv.flush();
        }
        return supplier;

    }

    @Override
    protected void createPORelated(final RefHolder pobject, final Screen xobject) throws ConstraintException,
        BusinessException, ModelException {
        //Conditions
        final ConditionDAO conditionDAO = new ConditionDAO(version);
        final Map<WellPosition, Condition> conditionPositions = xobject.getConditionPositions();
        for (final Map.Entry<WellPosition, Condition> entry : conditionPositions.entrySet()) {

            final WellPosition wp = entry.getKey();
            final Condition c = entry.getValue();

            // Find or create condition
            final RefSample rs = conditionDAO.findOrCreate(c);

            // Create the RefSamplePosition
            final Map<String, Object> attr = new HashMap<String, Object>();
            attr.put(RefSamplePosition.PROP_COLPOSITION, wp.getColumn());
            attr.put(RefSamplePosition.PROP_REFHOLDER, pobject);
            attr.put(RefSamplePosition.PROP_REFSAMPLE, rs);
            attr.put(RefSamplePosition.PROP_ROWPOSITION, wp.getRow());
            attr.put(RefSamplePosition.PROP_SUBPOSITION, wp.getSubPosition());
            new RefSamplePosition(getWritableVersion(), attr);
        }
        //Manufacturer
        if ((null != xobject.getManufacturerName()) && (xobject.getManufacturerName().length() > 0)) {

            final Map<String, Object> rssattr = new HashMap<String, Object>();
            rssattr.put(RefHolderSource.PROP_REFHOLDER, pobject);
            rssattr.put(RefHolderSource.PROP_PRODUCTGROUPNAME, xobject.getName());
            rssattr.put(RefHolderSource.PROP_SUPPLIER, findOrCreateSupplier(xobject, getWritableVersion()));
            rssattr.put(RefHolderSource.PROP_CATALOGNUM, "Unknown");
            new RefHolderSource(getWritableVersion(), rssattr);

        }

    }

    @Override
    protected Map<String, Object> getFullAttributes(final Screen xobject) {

        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));

        return attributes;
    }

    @Override
    protected Map<String, Object> getKeyAttributes(final Screen xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(RefHolder.PROP_NAME, xobject.getName());
        // HolderCategory used to identify
        attributes.put(Holder.PROP_HOLDERCATEGORIES,
            getScreenHolderCategory(version, xobject.getScreenType()));
        return attributes;
    }

    @Override
    protected Class<RefHolder> getPClass() {
        return RefHolder.class;
    }

    @Override
    protected Screen loadXAttribute(final RefHolder pobject) throws BusinessException {
        if (pobject
            .findFirst(RefHolder.PROP_HOLDERCATEGORIES, HolderCategory.PROP_NAME, HOLDER_CATEGORY_NAME) == null) {
            //this is not a screen
            return null;
        }
        final Screen screen = new Screen();

        screen.setName(pobject.getName());
        //ManufacturerName
        if (!pobject.getRefHolderSources().isEmpty()) {
            final RefHolderSource rss = pobject.getRefHolderSources().iterator().next();
            if (null != rss) {
                screen.setManufacturerName(rss.getSupplier().getName());
            }
        }
        //HolderCategory
        for (final HolderCategory hc : pobject.getHolderCategories()) {
            //if (DAOUtils.NAMING_SYSTEM.equals(hc.getNamingSystem())) 
            {
                if (getXtalScreenType(hc.getName()) != null) {
                    screen.setScreenType(getXtalScreenType(hc.getName()));
                }
            }
        }
        return screen;
    }

    public static ScreenType getXtalScreenType(final String pimsScreenTypeName) {
        if (SPARSE_MATRIX.equals(pimsScreenTypeName)) {
            return (ScreenType.Matrix);
        } else if (OPTIMIZATION.equals(pimsScreenTypeName)) {
            return ScreenType.Optimisation;

        } else if (ADDITIVE.equals(pimsScreenTypeName)) {
            return ScreenType.Additive;
        }
        return null;
    }

    @Override
    protected Screen loadXRole(final Screen xobject, final RefHolder pobject) throws BusinessException {
        //conditions
        final ConditionDAO conditionDAO = new ConditionDAO(version);

        final Map<WellPosition, Condition> conditionPositions = new HashMap<WellPosition, Condition>();
        for (final RefSamplePosition rsp : pobject.getRefSamplePositions()) {
            WellPosition wp;
            if (null != rsp.getSubPosition()) {
                wp = new WellPosition(rsp.getRowPosition(), rsp.getColPosition(), rsp.getSubPosition());
            } else {
                wp = new WellPosition(rsp.getRowPosition(), rsp.getColPosition());
            }
            final Condition c = conditionDAO.getFullXO(rsp.getRefSample());
            conditionPositions.put(wp, c);
        }

        xobject.setConditionPositions(conditionPositions);

        return xobject;
    }

    @Override
    protected void updatePORelated(final RefHolder pobject, final Screen xobject) throws ModelException {
        // TODO Auto-generated method stub

    }

    public Collection<RefHolder> findAll(final Paging paging) {
        //for HolderCategory
        final Map<String, Object> hcCriteria = new HashMap<String, Object>();
        hcCriteria.put(HolderCategory.PROP_NAME, HOLDER_CATEGORY_NAME);
        //for refHolder
        final Map<String, Object> refHolderCriteria = new HashMap<String, Object>();
        refHolderCriteria.put(RefHolder.PROP_HOLDERCATEGORIES, hcCriteria);
        final Collection<RefHolder> refHolders = version.findAll(RefHolder.class, refHolderCriteria, paging);
        return refHolders;
    }

    public int getCount() {
        //for HolderCategory
        final Map<String, Object> hcCriteria = new HashMap<String, Object>();
        hcCriteria.put(HolderCategory.PROP_NAME, HOLDER_CATEGORY_NAME);
        //for refHolder
        final Map<String, Object> refHolderCriteria = new HashMap<String, Object>();
        refHolderCriteria.put(RefHolder.PROP_HOLDERCATEGORIES, hcCriteria);
        final Collection<RefHolder> refHolders = version.findAll(RefHolder.class, refHolderCriteria);

        return refHolders.size();

    }

    public static RefSample getCondition(final RefHolder screen, final WellPosition wellPosition) {
        if (screen == null) {
            return null;
        }
        final int col = wellPosition.getColumn();
        final int row = wellPosition.getRow();
        for (final RefSamplePosition conditionPosition : screen.getRefSamplePositions()) {
            if (conditionPosition.getColPosition() == col && conditionPosition.getRowPosition() == row) {
                return conditionPosition.getRefSample();
            }
        }
        return null;
    }

    public Screen getScreen(RefHolder refHolder) throws BusinessException {
        return this.getFullXO(refHolder);
    }
}
