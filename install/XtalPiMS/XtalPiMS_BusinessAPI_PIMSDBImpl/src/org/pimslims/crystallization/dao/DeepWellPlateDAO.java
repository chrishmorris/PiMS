/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.logging.Logger;
import org.pimslims.business.crystallization.model.ConditionQuantity;
import org.pimslims.business.crystallization.model.DeepWellPlate;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderOffset;
import org.pimslims.model.holder.RefSamplePosition;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.crystallization.model.Condition}.
 * </p>
 * 
 * <p>
 * The following are as yet unmapped:
 * </p>
 * <ul>
 * <li>TODO DeepWellPlate.creationDate</li>
 * </ul>
 * 
 * @author Jon Diprose
 */
public class DeepWellPlateDAO {

    /**
     * <p>
     * The name of the DeepWellPlate HolderCategory.
     * </p>
     * 
     * <p>
     * TODO I'm not sure this is appropriate - one with specific meaning to XtalPiMS might be better. If so,
     * don't forget to add it to data/HolderCats.csv and rerun RefDataImporter
     * </p>
     */
    public static final String HOLDER_CATEGORY_NAME = "96 deep well plate";// JMD Was "DeepWellPlate";

    public static final String SCREEN_CATEGORY_NAME = "Screen";

    private static final Logger log = Logger.getLogger(DeepWellPlateDAO.class);

    /**
     * <p>
     * Store the specified DeepWellPlate in the specified WritableVersion.
     * </p>
     * 
     * <p>
     * TODO Currently unable to persist createDate.
     * </p>
     * 
     * @param dwp - the DeepWellPlate to store
     * @param wv - the WritableVersion in which to store the DeepWellPlate
     * @throws BusinessException if something goes wrong
     */
    public void create(DeepWellPlate dwp, WritableVersion wv) throws BusinessException {

        log.debug("In create");

        // Find the Holder for the Screen
        ScreenDAO screenDAO = new ScreenDAO(wv);
        RefHolder screenHolder = screenDAO.getPO(dwp.getScreen());
        if (null == screenHolder) {
            throw new BusinessException("no such screen: " + dwp.getScreen().getName());
        }

        // Find the HolderCategory
        HolderCategory dwpCategory = DAOUtils.findHolderCategory(wv, HOLDER_CATEGORY_NAME);
        if (null == dwpCategory) {
            throw new BusinessException("no \"" + HOLDER_CATEGORY_NAME
                + "\" category - xtalPiMS ref data not installed?");
        }

        // Find the HolderCategory
        SampleCategory screenCategory =
            wv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, SCREEN_CATEGORY_NAME);
        if (null == screenCategory) {
            throw new BusinessException("no \"" + SCREEN_CATEGORY_NAME
                + "\" category - xtalPiMS ref data not installed?");
        }

        Set<SampleCategory> scs = new HashSet<SampleCategory>();
        scs.add(screenCategory);

        try {

            Map<String, Object> hattr = new HashMap<String, Object>();

            // Barcode
            hattr.put(Holder.PROP_NAME, dwp.getBarcode());

            // Dates
            // TODO attr.put(Holder.PROP_STARTDATE, dwp.getCreateDate());
            hattr.put(Holder.PROP_STARTDATE, dwp.getActivationDate());
            hattr.put(Holder.PROP_ENDDATE, dwp.getDestroyDate());

            // HolderCategory
            Set<HolderCategory> cats = new HashSet<HolderCategory>();
            cats.add(dwpCategory);
            hattr.put(Holder.PROP_HOLDERCATEGORIES, cats);

            // Create the Holder
            log.debug("creating Holder");
            AbstractModelObject plateHolder = new Holder(wv, hattr);
            log.debug("created Holder");

            // Link to screen
            Map<String, Object> rhoattr = new HashMap<String, Object>();
            rhoattr.put(RefHolderOffset.PROP_HOLDER, plateHolder);
            rhoattr.put(RefHolderOffset.PROP_REFHOLDER, screenHolder);
            rhoattr.put(RefHolderOffset.PROP_COLOFFSET, 0);
            rhoattr.put(RefHolderOffset.PROP_ROWOFFSET, 0);
            rhoattr.put(RefHolderOffset.PROP_SUBOFFSET, 0);
            new RefHolderOffset(wv, rhoattr);
            log.debug("created RefHolderOffset");

            // make the well contents
            for (RefSamplePosition position : screenHolder.getRefSamplePositions()) {

                log.debug("start of loop");

                WellPosition wp =
                    new WellPosition(position.getRowPosition(), position.getColPosition(), position
                        .getSubPosition());

                Float currentAmount = null;
                String unit = "L";
                ConditionQuantity cq = dwp.getConditions().get(wp);
                if (null != cq) {
                    currentAmount = new Float(cq.getQuantity());
                    unit = cq.getUnit();
                }

                Map<String, Object> attr = new HashMap<String, Object>();
                attr.put(Sample.PROP_COLPOSITION, position.getColPosition());
                attr.put(Sample.PROP_CURRENTAMOUNT, currentAmount);
                attr.put(Sample.PROP_AMOUNTDISPLAYUNIT, "ul");
                attr.put(Sample.PROP_AMOUNTUNIT, unit);
                attr.put(Sample.PROP_HOLDER, plateHolder);
                attr.put(Sample.PROP_NAME, plateHolder.getName() + ":" + wp.toStringNoSubPosition());
                attr.put(Sample.PROP_REFSAMPLE, position.getRefSample());
                attr.put(Sample.PROP_ROWPOSITION, position.getRowPosition());
                attr.put(Sample.PROP_SUBPOSITION, position.getSubPosition());
                attr.put(Sample.PROP_SAMPLECATEGORIES, scs);

                log.debug("creating Sample");
                new Sample(wv, attr);
                log.debug("created Sample");

                log.debug("end of loop");

            }

            log.debug("done create");

        }

        catch (ConstraintException e) {

            throw new BusinessException(e.getMessage(), e);

        }

    }

    /**
     * <p>
     * Find the DeepWellPlate with the specified id.
     * </p>
     * 
     * @param id
     * @param rv
     * @return
     * @throws BusinessException
     */
    public DeepWellPlate findById(Long id, ReadableVersion rv) throws BusinessException {

        /*
         * JMD FIXME Check loss of functionality - DAOUtils.buildQuery is gone
        Query q =
            DAOUtils.buildQuery(rv, "h",
                "org.pimslims.model.holder.Holder as h left join h.holderCategories as hc",
                "hc.namingSystem = :hcns and hc.name = :hcn and h.id = :id", null);
        
        q.setParameter("hcns", DAOUtils.NAMING_SYSTEM);
        q.setParameter("hcn", HOLDER_CATEGORY_NAME);
        q.setParameter("id", new Long(id));

        List<?> holders = q.list();
        if (1 == holders.size()) {
            return populateFromHolder((Holder) holders.get(0));
        }
        return null;
         */

        //HolderCategories
        Map<String, Object> hcCriteria = new HashMap<String, Object>();
        hcCriteria.put(HolderCategory.PROP_NAME, HOLDER_CATEGORY_NAME);
        //holder
        Map<String, Object> holderCriteria = new HashMap<String, Object>();
        holderCriteria.put(Holder.PROP_HOLDERCATEGORIES, hcCriteria);
        holderCriteria.put(Holder.PROP_DBID, id);

        Holder holder = rv.findFirst(Holder.class, holderCriteria);
        if (null != holder) {
            return populateFromHolder(holder);
        }
        return null;

    }

    /**
     * <p>
     * Find the DeepWellPlate with the specified name.
     * </p>
     * 
     * @param name
     * @param rv
     * @return
     * @throws BusinessException
     */
    public DeepWellPlate findByName(String name, ReadableVersion rv) throws BusinessException {

        /*
         * JMD FIXME Check loss of functionality - DAOUtils.buildQuery is gone
        Query q =
            DAOUtils.buildQuery(rv, "h",
                "org.pimslims.model.holder.Holder as h left join h.holderCategories as hc",
                "hc.namingSystem = :hcns and hc.name = :hcn and h.name = :name", null);

        q.setParameter("hcns", DAOUtils.NAMING_SYSTEM);
        q.setParameter("hcn", HOLDER_CATEGORY_NAME);
        q.setParameter("name", name);

        List<?> holders = q.list();
        if (1 == holders.size()) {
            return populateFromHolder((Holder) holders.get(0));
        }
        return null;
         */

        //HolderCategories
        Map<String, Object> hcCriteria = new HashMap<String, Object>();
        hcCriteria.put(HolderCategory.PROP_NAME, HOLDER_CATEGORY_NAME);
        //holder
        Map<String, Object> holderCriteria = new HashMap<String, Object>();
        holderCriteria.put(Holder.PROP_HOLDERCATEGORIES, hcCriteria);
        holderCriteria.put(Holder.PROP_NAME, name);

        Holder holder = rv.findFirst(Holder.class, holderCriteria);
        if (null != holder) {
            return populateFromHolder(holder);
        }
        return null;

    }

    /**
     * <p>
     * Find the Collection of DeepWellPlate that instantiate the specified Screen.
     * </p>
     * 
     * @param screen
     * @param rv
     * @return
     * @throws BusinessException
     */
    public Collection<DeepWellPlate> findByScreen(Screen screen, ReadableVersion rv) throws BusinessException {

        // Find Screen
        ScreenDAO screenDAO = new ScreenDAO(rv);
        RefHolder screenHolder = screenDAO.getPO(screen);
        if (null == screenHolder) {
            throw new BusinessException("no such screen: " + screen.getName());
        }
        //HolderCategories
        Map<String, Object> hcCriteria = new HashMap<String, Object>();
        hcCriteria.put(HolderCategory.PROP_NAME, HOLDER_CATEGORY_NAME);
        //Offset
        Map<String, Object> rhoCriteria = new HashMap<String, Object>();
        rhoCriteria.put(RefHolderOffset.PROP_REFHOLDER, screenHolder);
        //holder
        Map<String, Object> holderCriteria = new HashMap<String, Object>();
        holderCriteria.put(Holder.PROP_HOLDERCATEGORIES, hcCriteria);
        holderCriteria.put(Holder.PROP_REFHOLDEROFFSETS, rhoCriteria);

        Collection<Holder> deepWellHolders = rv.findAll(Holder.class, holderCriteria);

        /*
         * This looks like it is now all obsolete, so I'm commenting
         * it out.
         *
         *
        // Build Query
        Query q =
            DAOUtils
                .buildQuery(
                    rv,
                    "h",
                    "org.pimslims.model.holder.Holder as h join h.hbRefHolderOffsets as rho join h.holderCategories as hc",
                    "rho.hbRefHolder.id = :screenid and hc.namingSystem = :hcns and hc.name = :hcn", null);

        // Apply parameters
        q.setParameter("screenid", screenHolder.getDbId());
        q.setParameter("hcns", DAOUtils.NAMING_SYSTEM);
        q.setParameter("hcn", HOLDER_CATEGORY_NAME);

        // Run query
        // TODO Beware enormous result sets - q.scroll() and q.setCacheMode()
        List<?> plateHolders = q.list();
        
         * 
         * End of apparently obsolete section
         */

        // Convert to DeepWellPlate
        Collection<DeepWellPlate> dwps = new ArrayList<DeepWellPlate>();
        for (Holder h : deepWellHolders) {
            dwps.add(populateFromHolderAndScreen(h, screen));
        }

        // Return the Collection of DeepWellPlate
        return dwps;

    }

    /**
     * <p>
     * Instantiate and populate a DeepWellPlate from the specified Holder.
     * </p>
     * 
     * @TODO Return visibility to protected
     * @param holder
     * @param screen
     * @return
     * @throws BusinessException
     */
    public DeepWellPlate populateFromHolder(Holder holder) throws BusinessException {
        if (holder == null || holder.getRefHolderOffsets() == null
            || holder.getRefHolderOffsets().size() == 0)
            return null;
        RefHolderOffset refHolderOffset = holder.getRefHolderOffsets().iterator().next();
        RefHolder screenHolder = refHolderOffset.getRefHolder();
        ScreenDAO screenDAO = new ScreenDAO(holder.get_Version());
        Screen screen = screenDAO.getFullXO(screenHolder);

        return populateFromHolderAndScreen(holder, screen);

    }

    /**
     * <p>
     * Add the list of holders to the DeepWellPlate HolderCategory.
     * </p>
     * 
     * @param holders
     * @param wv
     * @throws BusinessException
     */
    public void addHoldersToCategory(Set<Holder> holders, WritableVersion wv) throws BusinessException {

        // Switch to manual mode
        //JMD FIXME Check loss of functionality - Bill has reimplemented
        //WritableVersionState state = WritableVersionState.setManualMode((WritableVersionImpl) wv);
        final WritableVersionImpl wvi = (WritableVersionImpl) wv;
        final org.pimslims.dao.FlushMode oldFlushMode = wvi.getFlushMode();
        wvi.setFlushMode(org.pimslims.dao.FlushMode.batchMode());

        // Find the HolderCategory
        HolderCategory dwpCategory = DAOUtils.findHolderCategory(wv, DeepWellPlateDAO.HOLDER_CATEGORY_NAME);
        if (null == dwpCategory) {
            throw new BusinessException("no DeepWellPlate category - xtalPiMS ref data not installed?");
        }

        // Add the holders
        for (Holder h : holders) {
            try {
                h.addHolderCategory(dwpCategory);
                // JMD FIXME Check loss of functionality - reverse assignment not possible
                //dwpCategory.addAbstractHolder(h);
            } catch (ConstraintException e) {
                throw new BusinessException(e.getMessage(), e);
            }
        }

        // Flush
        try {
            wv.flush();
        } catch (ConstraintException e) {
            throw new BusinessException(e.getMessage(), e);
        }

        // Reset mode
        //JMD FIXME Check loss of functionality - Bill as reimplemented
        //state.applyTo((WritableVersionImpl) wv);
        wvi.setFlushMode(oldFlushMode);

    }

    /**
     * <p>
     * Instantiate and populate a DeepWellPlate from the specified Holder and Screen.
     * </p>
     * 
     * @param holder
     * @param screen
     * @return
     */
    protected DeepWellPlate populateFromHolderAndScreen(Holder holder, Screen screen) {

        DeepWellPlate dwp = new DeepWellPlate(holder.getName(), screen);
        // TODO dwp.setCreateDate(createDate);
        dwp.setActivationDate(holder.getStartDate());
        dwp.setDestroyDate(holder.getEndDate());

        return dwp;

    }

}
