/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.crystallization.model.Crystal;
import org.pimslims.business.crystallization.model.PlateExperimentInfo;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.CrystalService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Jon Diprose
 */
public class CrystalServiceImpl extends BaseServiceImpl implements CrystalService {

    private static final Logger log = LoggerFactory.getLogger(CrystalServiceImpl.class);

    public CrystalServiceImpl(DataStorage baseStorage) {
        super(baseStorage);
    }

    /**
     * CrystalServiceImpl.create - Create a crystal by creating an experiment from the SelectCrystal protocol
     * and linking to the sample that represents the drop.
     * 
     * @see org.pimslims.business.crystallization.service.CrystalService#create(org.pimslims.business.crystallization.model.Crystal)
     */
    @Override
    public void create(Crystal crystal) throws BusinessException {

        WritableVersion wv = this.getWritableVersion();

        try {

            Person operator = new Person();
            operator.setUsername(this.getDataStorage().getUsername());

            // Find out what we are operating on
            SQLQuery q = wv.getSession().createSQLQuery(getSQLQuery1());
            q.setParameter("barcode", crystal.getBarcode());
            q.setParameter("row", crystal.getWellPosition().getRow());
            q.setParameter("col", crystal.getWellPosition().getColumn());
            q.setParameter("sub", crystal.getWellPosition().getSubPosition());
            Object[] res = (Object[]) q.uniqueResult();

            if (null == res) {
                throw new BusinessException("No such trial drop: barcode=" + crystal.getBarcode()
                    + ";wellPosition=" + crystal.getWellPosition().toString());
            }

            Long sampleId = longFromObject(res[0]);
            String name = (String) res[1];
            String expBlueprintName = (String) res[2];
            Long accessId = longFromObject(res[3]);

            // Set default owner
            wv.setDefaultOwner(wv.findFirst(LabNotebook.class, LabNotebook.PROP_DBID, accessId));

            // Find the previous max crystal number
            q = wv.getSession().createSQLQuery(getSQLQuery2());
            q.setParameter("sampleid", sampleId);
            @SuppressWarnings("unchecked")
            List<Object> results = q.list(); // Again, returned as Object rather than Object[]
            int maxCrystalNum = 0;
            for (Object row : results) {
                if ((null != row)) {
                    try {
                        int i = Integer.parseInt((String) row);
                        if (i > maxCrystalNum) {
                            maxCrystalNum = i;
                        }
                    } catch (NumberFormatException e) {
                        // Swallow it
                    }
                }
            }

            // Increment and apply
            crystal.setNum(new Integer(maxCrystalNum + 1));

            // Append uniqifier to name
            name += " Crystal " + crystal.getNum().toString();

            // Build PlateExperimentInfo
            PlateExperimentInfo ei = new PlateExperimentInfo();
            ei.setExpBlueprintName(expBlueprintName);
            ei.setName(name);
            ei.setOperator(operator);
            // ei.setParameter("Conditions", crystal.getX().toString()); // TODO Copy down the conditions
            ei.setParameter("x", crystal.getX().toString());
            ei.setParameter("y", crystal.getY().toString());
            ei.setParameter("r", crystal.getR().toString());
            ei.setParameter("Crystal number", crystal.getNum().toString());
            ei.setProtocolName("Select Crystal");
            ei.setRunAt(Calendar.getInstance());

            // Create the experiment and parameters
            Map<String, Object> eattr = TrialServiceUtils.createExperimentAttributeMap(wv, null, ei);
            Experiment expt = TrialServiceUtils.createExperiment(wv, ei.getName(), eattr, ei);

            // Copy the experiment id back into the crystal
            crystal.setId(expt.getDbId());

            // Link the input sample to the experiment
            Sample inputSample = wv.findFirst(Sample.class, Sample.PROP_DBID, sampleId);
            // Shouldn't fail here
            if (null == inputSample) {
                throw new BusinessException("Trial drop disappeared during create - this shouldn't happen!");
            }
            TrialServiceUtils.createInputSample(wv, expt, "Drop containing crystal", inputSample, new Float(
                0f));

            // Create the output sample
            Map<String, Object> sattr = new HashMap<String, Object>();
            sattr.put(Sample.PROP_AMOUNTDISPLAYUNIT, "number");
            sattr.put(Sample.PROP_AMOUNTUNIT, "number");
            sattr.put(Sample.PROP_CURRENTAMOUNT, new Float(1f));
            sattr.put(Sample.PROP_INITIALAMOUNT, new Float(1f));
            //sattr.put(Sample.PROP_REFSAMPLE, refSample); // TODO For screen condition?
            sattr.put(Sample.PROP_NAME, name);
            Sample outputSample = wv.create(Sample.class, sattr);

            // Link the output sample to the experiment
            TrialServiceUtils.createOutputSample(wv, expt, "Crystal", outputSample, new Float(1f), "number");

        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessException(t);
        } finally {
            // TODO Check normal pims code arrangement
        }

    }

    /**
     * CrystalServiceImpl.delete - Delete a crystal by marking the relevant experiment as failed.
     * 
     * @see org.pimslims.business.crystallization.service.CrystalService#delete(org.pimslims.business.crystallization.model.Crystal)
     */
    @Override
    public void delete(Crystal crystal) throws BusinessException {
        WritableVersion wv = this.getWritableVersion();

        try {

            Person operator = new Person();
            operator.setUsername(this.getDataStorage().getUsername());

            // I'd like to trust crystal.getId() to be the experimentId but don't really feel that I can.

            // Find out what we are operating on
            SQLQuery q = wv.getSession().createSQLQuery(getSQLQuery3());
            q.setParameter("barcode", crystal.getBarcode());
            q.setParameter("row", crystal.getWellPosition().getRow());
            q.setParameter("col", crystal.getWellPosition().getColumn());
            q.setParameter("sub", crystal.getWellPosition().getSubPosition());
            q.setParameter("num", crystal.getNum().toString());
            //Object[] res = (Object[]) q.uniqueResult();// Cos its a single column it gets returned directly?
            Object res = q.uniqueResult();

            //if ((null != res) && (null != res[0])) {
            if (null != res) {
                //Long experimentId = (Long) res[0];
                Long experimentId = longFromObject(res);
                Experiment expt = wv.findFirst(Experiment.class, Experiment.PROP_DBID, experimentId);
                if (null != expt) {
                    expt.setStatus("Failed"); // TODO Is this a constant somewhere?
                    expt.setEndDate(Calendar.getInstance());
                }
            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessException(t);
        } finally {
            // TODO Check normal pims code arrangement
        }

    }

    /**
     * CrystalServiceImpl.findByBarcodeAndWell - find all the crystals on the specified barcode and
     * wellPosition by finding all the experiments with the SelectCrystal protocol on the relevant sample.
     * 
     * @see org.pimslims.business.crystallization.service.CrystalService#findByBarcodeAndWell(java.lang.String,
     *      org.pimslims.business.crystallization.model.WellPosition)
     */
    @Override
    public Collection<Crystal> findByBarcodeAndWell(String barcode, WellPosition wellPosition)
        throws BusinessException {

        Collection<Crystal> crystals = new ArrayList<Crystal>();

        try {

            Person operator = new Person();
            operator.setUsername(this.getDataStorage().getUsername());

            // Find out what we are operating on
            SQLQuery q = this.version.getSession().createSQLQuery(getSQLQuery4());
            q.setParameter("barcode", barcode);
            q.setParameter("row", wellPosition.getRow());
            q.setParameter("col", wellPosition.getColumn());
            q.setParameter("sub", wellPosition.getSubPosition());
            @SuppressWarnings("unchecked")
            List<Object[]> results = q.list();

            Map<Object, Crystal> tmpCrystals = new HashMap<Object, Crystal>();
            if (null != results) {
                for (Object[] row : results) {
                    if (null != row[0]) {
                        Long id = longFromObject(row[0]);
                        Crystal c = tmpCrystals.get(id);
                        if (null == c) {
                            c = new Crystal();
                            c.setId(id);
                            c.setBarcode(barcode);
                            c.setWellPosition(wellPosition);
                            tmpCrystals.put(id, c);
                            crystals.add(c);
                        }

                        if ("x".equals(row[1])) {
                            c.setX(new Integer(Integer.parseInt((String) row[2])));
                        } else if ("y".equals(row[1])) {
                            c.setY(new Integer(Integer.parseInt((String) row[2])));
                        } else if ("r".equals(row[1])) {
                            c.setR(new Integer(Integer.parseInt((String) row[2])));
                        } else if ("Crystal number".equals(row[1])) {
                            c.setNum(new Integer(Integer.parseInt((String) row[2])));
                        }
                    }

                }
            }

        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessException(t);
        } finally {
            // TODO Check normal pims code arrangement
        }

        return crystals;

    }

    /**
     * CrystalServiceImpl.getSQLQuery1 - an SQL query to get the sampleid and name, researchobjective name and
     * accessobjectid for the trial drop. Query takes four params: barcode (String); row (Integer); col
     * (Integer); and sub (Integer).
     * 
     * TODO This will need tuning/expanding if you don't always have the format: 'Protein production summary'
     * experiment with research objective and accessobject correctly set -> Sample -> experiment representing
     * the trial setup -> Sample representing the trial drop at the correct position in a plate -> Plate with
     * the correct barcode.
     * 
     * It could certainly be simplified if one didn't have to go up two rungs to find the research objective
     * and access object.
     * 
     * @return
     */
    protected String getSQLQuery1() {
        return "select " + "  td.abstractsampleid, " + "  sas.name, " + "  tro.commonname, "
            + "  clbe.accessid " + "from " + "  sam_sample td "
            + "  join sam_abstractsample sas on (td.abstractsampleid = sas.labbookentryid) "
            + "  join hold_abstractholder hah on (td.holderid = hah.labbookentryid) "
            + "  join expe_outputsample eos1 on (td.abstractsampleid = eos1.sampleid) "
            + "  join expe_inputsample eis1 on (eos1.experimentid = eis1.experimentid) "
            + "  join expe_outputsample eos2 on (eis1.sampleid = eos2.sampleid) "
            + "  join expe_experiment ee on (eos2.experimentid = ee.labbookentryid) "
            + "  join core_labbookentry clbe on (ee.labbookentryid = clbe.dbid) "
            + "  join ref_experimenttype ret on (ee.experimenttypeid = ret.publicentryid) "
            + "  left join targ_researchobjective tro on (ee.researchobjectiveid = tro.labbookentryid) "
            + "where " + "  hah.name = :barcode " + "  and td.rowposition = :row "
            + "  and td.colposition = :col " + "  and td.subposition = :sub "
            + "  and ret.name = 'Protein production summary'";
    }

    /**
     * CrystalServiceImpl.getSQLQuery2 - an SQL query to get all the previously recorded values of Crystal
     * Number for a particular trial drop (sample). Query takes one param: sampleid (Long).
     * 
     * @return The query string
     */
    protected String getSQLQuery2() {
        return "select ep.value from expe_parameter ep "
            + "  join expe_inputsample eis on (ep.experimentid = eis.experimentid) "
            + "  join prot_refinputsample pri on (eis.refinputsampleid = pri.labbookentryid) "
            + "  join prot_protocol pp on (pri.protocolid = pp.labbookentryid) where "
            + "  eis.sampleid = :sampleid and ep.name = 'Crystal number' "
            + "  and pp.name = 'Select Crystal'";

    }

    /**
     * CrystalServiceImpl.getSQLQuery3 - an SQL query to get the id of the experiment recording the location
     * of a particular crystal. Query takes five params: barcode (String); row (Integer); col (Integer); sub
     * (Integer); and num (String).
     * 
     * @return The query string
     */
    protected String getSQLQuery3() {
        return "select " + "  ep.experimentid " + "from " + "  sam_sample td "
            + "  join hold_abstractholder hah on (td.holderid = hah.labbookentryid) "
            + "  join expe_inputsample eis on (td.abstractsampleid = eis.sampleid) "
            + "  join expe_parameter ep on (eis.experimentid = ep.experimentid) "
            + "  join prot_refinputsample pri on (eis.refinputsampleid = pri.labbookentryid) "
            + "  join prot_protocol pp on (pri.protocolid = pp.labbookentryid) " + "where "
            + "  hah.name = :barcode " + "  and td.rowposition = :row " + "  and td.colposition = :col "
            + "  and td.subposition = :sub " + "  and ep.value = :num " + "  and pp.name = 'Select Crystal'";

    }

    /**
     * CrystalServiceImpl.getSQLQuery4 - an SQL query to get all the recorded crystals by barcode and well.
     * Query takes four params: barcode (String); row (Integer); col (Integer); and sub (Integer).
     * 
     * @return The query string
     */
    protected String getSQLQuery4() {
        return "select ep.experimentid, ep.name, ep.value from sam_sample td "
            + "  join hold_abstractholder hah on (td.holderid = hah.labbookentryid) "
            + "  join expe_inputsample eis on (td.abstractsampleid = eis.sampleid) "
            + "  join expe_experiment ee on (eis.experimentid = ee.labbookentryid) "
            + "  join expe_parameter ep on (eis.experimentid = ep.experimentid) "
            + "  join prot_refinputsample pri on (eis.refinputsampleid = pri.labbookentryid) "
            + "  join prot_protocol pp on (pri.protocolid = pp.labbookentryid) where "
            + "  hah.name = :barcode and td.rowposition = :row and td.colposition = :col "
            + "  and td.subposition = :sub and pp.name = 'Select Crystal' "
            + "  and not ee.status = 'Failed' order by ep.experimentid, ep.name";

    }

    /**
     * CrystalServiceImpl.longFromObject - Convert an Object that might really be a BigInteger or might be a
     * Long to a Long.
     * 
     * @param longObj - the Object containing the long value
     * @return The Long equivalent
     */
    protected Long longFromObject(Object longObj) {
        if (longObj instanceof BigInteger) {
            return new Long(((BigInteger) longObj).longValue());
        }
        return (Long) longObj;
    }
}
