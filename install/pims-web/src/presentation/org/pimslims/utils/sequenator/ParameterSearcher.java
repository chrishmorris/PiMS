/**
 * current-pims-web org.pimslims.utils.sequenator ParameterSearcher.java
 * 
 * @author pvt43 aka Petr Troshin
 * @date 27 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.search.Paging;
import org.pimslims.search.Searcher;
import org.pimslims.utils.experiment.Utils;

/**
 * ParameterSearcher
 * 
 * Abstraction which look at the Parameter values and produce SequencingOrder lists what has those parameters
 */
public class ParameterSearcher {

    private final ReadableVersion rv;

    private final MetaClass paramc;

    private final Searcher searcher;

    private final Map<String, Object> criteria;

    private final Map<String, Object> commonLimits;

    private final Map<String, Object> expCriteria;

    /**
     * These are parameters name from Sequencing Order protocol
     */
    private static String userEmail = "User Email";

    private static String user = "User";

    private static String pi = "Principal Investigator";

    static String accountNum = "Account Number";

    static String department = "Department";

    /**
     * Constructor for ParameterSearcher
     */
    @Deprecated
    // not supported
    public ParameterSearcher(final ReadableVersion rv) {
        this.rv = rv;
        this.searcher = new Searcher(rv);
        this.paramc = ServletUtil.getMetaClass("org.pimslims.model.experiment.Parameter");
        this.criteria = new HashMap<String, Object>();
        this.commonLimits = new HashMap<String, Object>();
        final Protocol protocol = Utils.getSOProtocol(this.rv);
        assert protocol != null : "Sequencing Order Protocol is not found! Please update your reference data!";

        final Map<String, Object> pdCriteria = new HashMap<String, Object>();
        pdCriteria.put(ParameterDefinition.PROP_PROTOCOL, protocol);
        this.criteria.put(Parameter.PROP_PARAMETERDEFINITION, pdCriteria);
        this.criteria.put(Parameter.PROP_NAME, "Order ID");

        this.expCriteria = new HashMap<String, Object>();

        this.commonLimits.putAll(this.criteria);
    }

    public ParameterSearcher(final ReadableVersion rv, final boolean recentOnly) {
        this(rv);
        final Calendar monthBeforeToday = Calendar.getInstance();
        monthBeforeToday.roll(Calendar.MONTH, false);
        this.criteria.put(LabBookEntry.PROP_CREATIONDATE,
            org.pimslims.search.Conditions.greaterOrEquals(monthBeforeToday));
        this.commonLimits.putAll(this.criteria);
    }

    public ParameterSearcher(final ReadableVersion rv, final Calendar from, final Calendar to) {
        this(rv);
        // Due to the fact that dates in between are excluded from the search dateTo+1 day is used. 
        // This is done to avoid confusion as users expected to find the experiments they created today if
        // dateTo is today. 
        to.roll(Calendar.DATE, true);

        this.criteria.put(LabBookEntry.PROP_CREATIONDATE, org.pimslims.search.Conditions.between(from, to));
        this.commonLimits.putAll(this.criteria);
    }

    private List<Experiment> convertToExperiments(final Collection<ModelObject> expParams) {
        final List<Experiment> exps = new ArrayList<Experiment>();
        for (final ModelObject o : expParams) {
            exps.add(((Parameter) o).getExperiment());
        }
        return exps;
    }

    /**
     * Please make sure to call this if different searches has to be independent
     */
    public void reset() {
        this.criteria.clear();
        this.expCriteria.clear();
        this.criteria.putAll(this.commonLimits);
    }

    public void setSearchByAccountNum(final String accountNumber) {
        this.setSearchParameter(ParameterSearcher.accountNum, accountNumber);
    }

    public void setSearchByPiName(final String piName) {
        this.setSearchParameter(ParameterSearcher.pi, piName);
    }

    public void setSearchParameter(final String paramName, final String searchValue) {
        // clear all previous criteria
        if (!Util.isEmpty(searchValue)) {
            this.reset();

            final Map<String, Object> paramCriteria = new HashMap<String, Object>();
            paramCriteria.put(Parameter.PROP_NAME, org.pimslims.search.Conditions.eq(paramName));
            paramCriteria.put(Parameter.PROP_VALUE, org.pimslims.search.Conditions.contains(searchValue));
            this.expCriteria.put(Experiment.PROP_PARAMETERS, paramCriteria);
            this.criteria.put(Parameter.PROP_EXPERIMENT, this.expCriteria);
        }
    }

    public void setSearchByUserName(final String userName) {
        this.setSearchParameter(ParameterSearcher.user, userName);
    }

    public void setSearchBySoId(final String soid) {
        this.setSearchParameter(SequencingInputDataParser.orderParamName, soid);
    }

    public void setSearchByEmail(final String email) {
        this.setSearchParameter(ParameterSearcher.userEmail, email);
    }

    public void setSearchByPrimerName(final String primerName) {
        this.setSearchParameter(SequencingOrder.primerName, primerName);
    }

    public void setSearchByTemplateName(final String templateName) {
        this.setSearchParameter(SequencingOrder.TemplateName, templateName);
    }

    public void setSearchByDepartment(final String depName) {
        this.setSearchParameter(ParameterSearcher.department, depName);
    }

    public List<Experiment> getResults(final Paging paging) {
        final ArrayList<ModelObject> mObjs = this.searcher.search(this.criteria, this.paramc, paging);
        return this.convertToExperiments(mObjs);
    }

    public int getCount(final String searchStr) {
        assert !Util.isEmpty(searchStr);
        assert this.paramc != null;
        //final Experiment e;
        // TODO should be this.searcher.count(searchStr, this.paramc);
        // but this method has a bug in it
        return this.searcher.count(this.paramc, new HashMap<String, Object>(), searchStr);
    }

    /**
     * Remove not relevant Parameters Searcher.filter
     * 
     * @param parametersList
     * @return
     */
    private static List<ModelObject> filter(final List<ModelObject> parametersList) {
        final List<ModelObject> pars = new ArrayList<ModelObject>();
        for (final ModelObject op : parametersList) {
            final Parameter p = ((Parameter) op);
            final ParameterDefinition pd = p.getParameterDefinition();
            if (pd != null && pd.getProtocol().getName().equalsIgnoreCase(SequencingOrder.soProtocolName)) {
                pars.add(p);
            }
        }
        return pars;
    }

    public List<Experiment> searchAll(final String value, final Paging paging) {
        assert !Util.isEmpty(value);
        // Note that paging can be null
        final ArrayList<ModelObject> mObjs = this.searcher.searchAll(this.paramc, value, paging);
        return this.convertToExperiments(ParameterSearcher.filter(mObjs));
    }

    public int getCount() {
        return this.searcher.count(this.criteria, this.paramc);
    }

    public static List<SequencingOrder> convertToOrders(final Set<Experiment> experiments) {
        final SOrdersManager som = new SOrdersManager(experiments);
        return som.getSorders();
    }

    public void printCriteria() {
        System.out.println(this.criteria);
    }

    /*    
     public void searchByRunNumber() {

    }
    */
}
