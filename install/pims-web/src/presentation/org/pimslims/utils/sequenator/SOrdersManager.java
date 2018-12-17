/**
 * V2_2-pims-web org.pimslims.utils.sequenator SequencingOrder.java
 * 
 * @author pvt43
 * @date 21 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Creator;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.Note;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.Conditions;
import org.pimslims.utils.experiment.Utils;
import org.pimslims.utils.sequenator.SequencingOrder.CompletionState;

/**
 * SequencingOrderManager
 * 
 * org.pimslims.utils.sequenator.SOrdersManager
 */
public class SOrdersManager {

    public static enum ExpStatus {
        OK, In_process, Failed, To_be_run, Unknown
    }

    //List must remain initialized in the each of the constructors
    private final ArrayList<SequencingOrder> sOrders;

    public SOrdersManager(final SequencingOrder sOrder) {
        assert sOrder != null : "Sequencing order must not be NULL";
        this.sOrders = this.getList();
        this.sOrders.add(sOrder);
    }

    public SOrdersManager(final ExperimentGroup plate) {
        final HashSet<String> orderIds = SOrdersManager.getOrdersIds(plate.getExperiments());
        final ArrayList<SequencingOrder> sos = new ArrayList<SequencingOrder>();
        final ReadableVersion rv = plate.get_Version();
        for (final String orderId : orderIds) {
            if (Util.isHookValid(orderId)) {
                // Controls needs to be retrieved in a special way
                final Experiment controlExp = rv.get(orderId);
                sos.add(new SequencingOrder(Collections.singletonList(controlExp)));
                continue;
            }
            final SequencingOrder so = new SequencingOrder(orderId, rv);
            sos.add(so);
        }
        this.sOrders = sos;
    }

    public SOrdersManager(final Collection<Experiment> diffSOrdersExps) {
        assert diffSOrdersExps != null && !diffSOrdersExps.isEmpty();
        final HashSet<String> orderIds = SOrdersManager.getOrdersIds(diffSOrdersExps);
        final ArrayList<SequencingOrder> sos = new ArrayList<SequencingOrder>();
        final ReadableVersion rv = diffSOrdersExps.iterator().next().get_Version();
        for (final String orderId : orderIds) {
            if (Util.isHookValid(orderId)) {
                // Controls needs to be retrieved in a special way
                final Experiment controlExp = rv.get(orderId);
                sos.add(new SequencingOrder(Collections.singletonList(controlExp)));
                continue;
            }

            final SequencingOrder so = new SequencingOrder(orderId, rv);
            sos.add(so);
        }
        this.sOrders = sos;
    }

    // Version has to be fresh  
    public SOrdersManager(final Collection<Experiment> diffSOrdersExps, final ReadableVersion rv) {
        assert diffSOrdersExps != null && !diffSOrdersExps.isEmpty();
        final HashSet<String> orderIds = SOrdersManager.getOrdersIds(diffSOrdersExps);
        final ArrayList<SequencingOrder> sos = new ArrayList<SequencingOrder>();

        for (final String orderId : orderIds) {
            if (Util.isHookValid(orderId)) {
                // Controls needs to be retrieved in a special way
                final Experiment controlExp = rv.get(orderId);
                sos.add(new SequencingOrder(Collections.singletonList(controlExp)));
                continue;
            }

            final SequencingOrder so = new SequencingOrder(orderId, rv);
            sos.add(so);
        }
        this.sOrders = sos;
    }

    public SOrdersManager(final List<SequencingOrder> sOrders) {
        assert sOrders != null && !sOrders.isEmpty() : "Sequencing orders must not be NULL";
        this.sOrders = this.getList();
        this.sOrders.addAll(sOrders);
    }

    private ArrayList<SequencingOrder> getList() {
        if (this.sOrders == null) {
            return new ArrayList<SequencingOrder>();
        }
        return this.sOrders;
    }

    public SOrdersManager(final String sOrderId, final ReadableVersion rv) {
        this.sOrders = this.getList();
        this.addOrder(sOrderId, rv);
    }

    /*
    private static List<SequencingOrder> getAllControlExp(final ExperimentGroup run) {
        final SequencingOrder rcontrol = SOrdersManager.getControlExp(run, true);
        final SequencingOrder icontrol = SOrdersManager.getControlExp(run, false);
        final ArrayList<SequencingOrder> csos = new ArrayList<SequencingOrder>();
        if (rcontrol != null) {
            csos.add(rcontrol);
        }
        if (icontrol != null) {
            csos.add(icontrol);
        }
        return csos;
    }
    */

    /**
     * 
     * SOrdersManager.getControl
     * 
     * @param run
     * @param reaction
     * @web function
     * @return
     */
    public static SequencingOrder getControlExp(final ExperimentGroup run, final boolean reaction) {
        final SOrdersManager som = new SOrdersManager(run);
        final Experiment cexp = som.getControl(reaction);
        if (cexp != null) {
            return new SequencingOrder(Collections.singletonList(cexp));
        }
        return null;
    }

    public Experiment getControl(final boolean reaction) {
        String ctype = SequencingOrder.reactionControl;
        if (!reaction) {
            ctype = SequencingOrder.instrumentControl;
        }
        for (final SequencingOrder so : this.sOrders) {
            if (so.getId().equals(ctype)) {
                return so.getOneExperiment();
            }
        }
        return null;
    }

    public int getControlCount() {

        int ccount = 0;
        for (final SequencingOrder so : this.sOrders) {
            final String soId = so.getId();
            if (soId.equals(SequencingOrder.instrumentControl)
                || soId.equals(SequencingOrder.reactionControl)) {
                ccount++;
            }
        }
        return ccount;
    }

    public void addOrder(final String sOrderId, final ReadableVersion rv) {
        assert !Util.isEmpty(sOrderId) : "Sequencing orderId must not be NULL";
        /*
        assert sOrderId.trim().startsWith(Utils.ORDERNAMEPREFIX) : "Order Id must start with "
            + Utils.ORDERNAMEPREFIX + " which follow the number";
            */
        final SequencingOrder so = new SequencingOrder(sOrderId, rv);
        if (!this.sOrders.contains(so)) {
            this.sOrders.add(so);
        }
    }

    public List<Experiment> getExperiments() {
        final ArrayList<Experiment> allExps = new ArrayList<Experiment>();
        for (final SequencingOrder so : this.sOrders) {
            allExps.addAll(so.getExperiments());
        }
        return allExps;
    }

    public static Comparator<SequencingOrder> SOrderName = new Comparator<SequencingOrder>() {

        public int compare(final SequencingOrder o1, final SequencingOrder o2) {
            return o2.getIntId().compareTo(o1.getIntId()); // ascending list
        }

    };

    public static Comparator<SequencingOrder> wellName = new Comparator<SequencingOrder>() {
        // This benefits of the fact that experiments are named sequentially, 
        // thus this is equivalent to well sort
        public int compare(final SequencingOrder o1, final SequencingOrder o2) {
            return o1.getOneExperiment().getName().compareTo(o2.getOneExperiment().getName());
        }

    };

    public List<SequencingOrder> getSorders() {
        final ArrayList<SequencingOrder> orderIdSortedSOrders = new ArrayList<SequencingOrder>(this.sOrders);
        Collections.sort(orderIdSortedSOrders, SOrdersManager.SOrderName);
        return orderIdSortedSOrders;
    }

    public LinkedList<SequencingOrder> getSOrderByDate() {
        final LinkedList<SequencingOrder> sOrdersOldFirst = new LinkedList<SequencingOrder>(this.sOrders);
        Collections.sort(sOrdersOldFirst, SOrdersManager.dateComp);
        return sOrdersOldFirst;
    }

    public SequencingOrder getOrder(final String orderId) {
        assert !Util.isEmpty(orderId) : "Order Id must be provided! ";

        for (final SequencingOrder so : this.sOrders) {
            if (so.orderId.equalsIgnoreCase(orderId)) {
                return so;
            }
        }
        return null;
    }

    public List<SequencingOrder> getTraficLightsSortedOrders() {
        final ArrayList<SequencingOrder> resultSorted = new ArrayList<SequencingOrder>(this.sOrders);
        Collections.sort(resultSorted, SOrdersManager.wellName);
        /*
        if (this.getIsPlateCompleted() == CompletionState.Completed) {
            // If plate is completed sort by name
            Collections.sort(resultSorted, SOrdersManager.wellName);
        } else {
            // If plate is NOT completed sort by completion status
            Collections.sort(resultSorted, SequencingOrder.traficLightComparator);
        } */
        return resultSorted;
    }

    /*
    @obsolite
    public String getPlateName() {   }
    */

    static boolean isControlId(final String id) {
        assert !Util.isEmpty(id);
        if (id.startsWith(SequencingOrder.reactionControl)
            || id.startsWith(SequencingOrder.instrumentControl)) {
            return true;
        }
        return false;
    }

    /*
    public String getAllOrdersNames() {
        String name = "SO_";
        for (final SequencingOrder so : this.sOrders) {
            name += so.getId().substring(Utils.ORDERNAMEPREFIX.length()) + "/";
        }
        return name.substring(0, name.length() - 1);
    } */

    public int getSize() {
        int counter = 0;
        for (final SequencingOrder so : this.sOrders) {
            counter += so.getSize();
        }
        return counter;
    }

    /**
     * @web function
     * @see pims.tld SOrdersManager.getNewOrdersTotalSampleNumber
     * @param userName
     * @return
     */
    public static int getNewOrdersTotalSampleNumber(final String userName) {
        assert !Util.isEmpty(userName);

        final AbstractModel model = ModelImpl.getModel();
        final ReadableVersion rv = model.getReadableVersion(userName);
        final List<SequencingOrder> soders = SOrdersManager.getNewSOrders(rv);
        if (soders == null || soders.isEmpty()) {
            return 0;
        }
        final SOrdersManager so = new SOrdersManager(soders);
        final int sampleNumber = so.getExperiments().size();
        try {
            rv.commit();
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        return sampleNumber;
    }

    public SequencingOrder getOldestOrder() {
        Calendar memc = null;
        SequencingOrder oldestSo = null;
        for (final SequencingOrder so : this.sOrders) {
            // Skip controls as they do not matter
            if (so.isControl()) {
                continue;
            }
            final Calendar cur = so.getDate();
            if (memc == null) {
                memc = cur;
                oldestSo = so;
            } else {
                if (memc.before(cur)) {
                    continue;
                } else {
                    memc = cur;
                    oldestSo = so;
                }
            }
        }
        return oldestSo;
    }

    // Returns day value difference  
    public int getOldestOrderAge() {
        final Long difference =
            System.currentTimeMillis() - this.getOldestOrder().getDate().getTimeInMillis();
        final Long days = (difference / (1000 * 60 * 60 * 24));
        return days.intValue();
    }

    /**
     * Propose an optimal sample distribution so that - for larger then a plate orders
     * 
     * 1) The amount of unused wells is close to 0
     * 
     * 2) The oldest samples goes first
     * 
     * 3) Orders from different users grouped together - for smaller then a plate orders
     * 
     * 1)use complete capillaries
     * 
     * 2) & 3) from above
     * 
     * SOrdersManager.getOptimalLayout
     * 
     * @return
     */

    SequencingOrder getOrderbySize(final List<SequencingOrder> orders, final int size) {
        assert size > 0 : "Order size must be greater than 0!";
        boolean seek = true;
        int nextSize = size;
        while (seek) {
            for (final SequencingOrder so : orders) {
                if (so.getSize() == nextSize) {
                    return so;
                }
            }
            if (nextSize > 1) {
                nextSize--;
            } else {
                seek = false;
                break;
            }
        }

        return null;
    }

    static final Comparator<SequencingOrder> dateComp = new Comparator<SequencingOrder>() {
        public int compare(final SequencingOrder o1, final SequencingOrder o2) {
            assert o1 != null && o2 != null : "Cannot compare NULL orders!";
            return o1.getDate().compareTo(o2.getDate());
        }

    };

    static String getOrderId(final Set<Experiment> experiments) {
        assert experiments != null && !experiments.isEmpty() : "No experiments found. Cannot determine the orderId";
        final Experiment exp = experiments.iterator().next();
        final String orderId = Utils.getParameterValue(exp, SequencingInputDataParser.orderParamName);
        assert !Util.isEmpty(orderId) : "Cannot get orderId from experiment '" + exp
            + "'. Is this a Sequencing Order?";

        return orderId;
    }

    private static HashSet<String> getOrdersIds(final Collection<Experiment> experiments) {
        final HashSet<String> orderIds = new HashSet<String>();
        for (final Experiment exp : experiments) {
            if (exp == null) {
                continue;
            }
            String orderId = Utils.getParameterValue(exp, SequencingInputDataParser.orderParamName);
            assert !Util.isEmpty(orderId) : "Cannot get orderId from experiment '" + exp
                + "'. Is this a Sequencing Order?";
            if (SequencingOrder.isControl(orderId)) {
                // Controls must be handled differently 
                orderId = exp.get_Hook();
            }
            orderIds.add(orderId);
        }
        return orderIds;
    }

    /**
     * This only returns reference controls, not the controls used in other orders!
     * SOrdersManager.getControlParams
     * 
     * @param rv
     * @param reaction
     * @return
     */
    private static Collection<Parameter> getControlParams(final ReadableVersion rv, final boolean reaction) {
        final HashMap<String, Object> expprop = new HashMap<String, Object>();
        // Only the Reference controls have this status
        expprop.put(Experiment.PROP_STATUS, "Unknown");

        final HashMap<String, Object> prop = new HashMap<String, Object>();
        prop.put(Parameter.PROP_NAME, SequencingInputDataParser.orderParamName);
        prop.put(Parameter.PROP_EXPERIMENT, expprop);
        if (reaction) {
            prop.put(Parameter.PROP_VALUE, SequencingOrder.reactionControl);
        } else {
            prop.put(Parameter.PROP_VALUE, SequencingOrder.instrumentControl);
        }
        final Collection<Parameter> prms = rv.findAll(Parameter.class, prop);
        //final Searcher s = new Searcher(rv);
        //final Collection<ModelObject> prms =
        //    s.search(prop, ServletUtil.getMetaClass("org.pimslims.model.experiment.Parameter"));
        return prms;
    }

    /*
     * Set the status of sequencing experiment depending on the uploaded results. 
     * Make sure do not rate the experiments for which no results have yet been uploaded. 
     * 
     * should to complete new expperiments are not rated as so.isCompleted retur
     */
    public void rateResults() throws ConstraintException {
        for (final SequencingOrder so : this.sOrders) {
            // Do not re-rate completed orders 
            //if (so.getIsCompleted() != SequencingOrder.CompletionState.Completed) {
            so.rateResults();
            //}
        }
    }

    public CompletionState getIsPlateCompleted() {
        final List<SequencingOrder> orders = this.getSorders();
        int completed = 0;
        int notCompleted = 0;
        for (final SequencingOrder sorder : orders) {
            switch (sorder.getIsCompleted()) {
                case Completed:
                    completed++;
                    break;
                case PartlyCompleted:
                    // Do nothing its fine
                    break;
                case NotCompleted:
                    notCompleted++;
                    break;
            }
        }
        if (completed == orders.size()) {
            return CompletionState.Completed;
        }
        if (notCompleted == orders.size()) {
            return CompletionState.NotCompleted;
        }

        return CompletionState.PartlyCompleted;

    }

    public static boolean isPlateCompletionConfirmed(final ExperimentGroup eg) {

        final PlateNoteManager pnm = new PlateNoteManager(eg);
        return pnm.isPlateCompletionConfirmed();
    }

    public static List<SequencingOrder> getNewSOrders(final ReadableVersion rv) {

        final Protocol protocol = Utils.getSOProtocol(rv);
        final Map<String, Object> prop = Util.getNewMap();

        prop.put(Experiment.PROP_PROTOCOL, protocol);
        prop.put(Experiment.PROP_STATUS, SOrdersManager.ExpStatus.To_be_run.toString());
        prop.put(Experiment.PROP_EXPERIMENTGROUP, Conditions.isNull());
        final Collection<Experiment> exps = rv.findAll(Experiment.class, prop);

        if (exps.isEmpty()) {
            return new ArrayList<SequencingOrder>();
        }

        final SOrdersManager som = new SOrdersManager(exps);
        return som.getSorders();
    }

    public static List<SequencingOrder> getLastMonthSOrders(final ReadableVersion rv) {
        final Calendar monthAgo = Calendar.getInstance();
        monthAgo.add(Calendar.MONTH, -1);
        final Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 1);
        return SOrdersManager.getSOrders(rv, monthAgo, today);
    }

    public static List<SequencingOrder> getLastWeekSOrders(final ReadableVersion rv) {
        final Calendar weekAgo = Calendar.getInstance();
        weekAgo.add(Calendar.DATE, -7);
        final Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 1);
        return SOrdersManager.getSOrders(rv, weekAgo, today);
    }

    public static List<SequencingOrder> getSOrders(final ReadableVersion rv, final Calendar from,
        final Calendar to) {

        final Protocol protocol = Utils.getSOProtocol(rv);
        final Map<String, Object> prop = Util.getNewMap();

        prop.put(Experiment.PROP_PROTOCOL, protocol);
        // This will filter out controls
        final Map<String, Object> parCrit = Conditions.newMap(Parameter.PROP_NAME, "Order ID");
        parCrit.put(Parameter.PROP_VALUE, Conditions.startWith("SO_"));
        prop.put(Experiment.PROP_PARAMETERS, Conditions.andMap(parCrit));
        prop.put(Experiment.PROP_STARTDATE, Conditions.between(from, to));

        final Collection<Experiment> exps = rv.findAll(Experiment.class, prop);

        if (exps.isEmpty()) {
            return new ArrayList<SequencingOrder>();
        }

        final SOrdersManager som = new SOrdersManager(exps);
        return som.getSorders();
    }

    /*
    public static List<SequencingOrder> getPlannedSOrders(final ReadableVersion rv) {

        final Collection<Experiment> exps =
            SOrdersManager.getExpforSOrders(rv, SOrdersManager.ExpStatus.In_process);

        if (exps.isEmpty()) {
            return new ArrayList<SequencingOrder>();
        }

        final SOrdersManager som = new SOrdersManager(exps);

        final List<SequencingOrder> layoutDoneOrders = new ArrayList<SequencingOrder>();
        for (final SequencingOrder so : som.getSorders()) {
            //(Please note that ALL orders on one plate will have reached the same processing step)
            // Filter out controls
            if (so.isControl()) {
                continue;
            }
            // get orders with at a particular processing step  
            if (so.isLayoutComplete()) {
                layoutDoneOrders.add(so);
            }
        }

        return layoutDoneOrders;
    }
    */
    public static List<ExperimentGroup> getPlannedRuns(final ReadableVersion rv) {
        return SOrdersManager.getPlates(SOrdersManager.getNotesForPlannedRuns(rv));
    }

    /*
    public static List<ExperimentGroup> getPlannedRuns(final ReadableVersion rv) {
        return SOrdersManager.groupByPlate(SOrdersManager.getPlannedSOrders(rv));
    }
    */
/*
    public static List<SequencingOrder> getSeqSetupDoneSOrders(final ReadableVersion rv) {

        final Collection<Experiment> exps =
            SOrdersManager.getExpforSOrders(rv, SOrdersManager.ExpStatus.In_process);

        if (exps.isEmpty()) {
            return new ArrayList<SequencingOrder>();
        }

        final SOrdersManager som = new SOrdersManager(exps);
        final List<SequencingOrder> seqSetupOrders = new ArrayList<SequencingOrder>();

        for (final SequencingOrder so : som.getSorders()) {
            if (so.isSeqSetupCompleted()) {
                seqSetupOrders.add(so);
            }
        }
        return seqSetupOrders;
    }

*/
    public static List<ExperimentGroup> getSSetupDoneSOrders(final ReadableVersion rv) {
        return SOrdersManager.getPlates(SOrdersManager.getNotesForAwaitingResultsRuns(rv));
    }

    /*
    public static List<ExperimentGroup> getSSetupDoneSOrders(final ReadableVersion rv) {
        return SOrdersManager.groupByPlateAndRemove(SOrdersManager.getSeqSetupDoneSOrders(rv));
    }
     */
    /*
    public static List<SequencingOrder> getLastMonthCompletedOrders(final ReadableVersion rv) {
        final Collection<Experiment> exps = SOrdersManager.getExpforSOrders(rv, SOrdersManager.ExpStatus.OK); // return completed orders from the last month
        if (exps.isEmpty()) {
            return new ArrayList<SequencingOrder>();
        }
        final SOrdersManager som = new SOrdersManager(exps);
        return som.getSorders();
    }

    
    public static List<ExperimentGroup> getLastMonthCompletedRuns(final ReadableVersion rv) {
        return SOrdersManager.groupByPlate(SOrdersManager.getLastMonthCompletedOrders(rv));
    }

    
    public static List<ExperimentGroup> getMonthCompletedRuns(final ReadableVersion rv, final Calendar date) {
        final Collection<Experiment> exps = SOrdersManager.getExpforCompletedSOrders(rv, date);
        if (exps.isEmpty()) {
            return new ArrayList<ExperimentGroup>();
        }
        final SOrdersManager som = new SOrdersManager(exps);
        return SOrdersManager.groupByPlate(som.getSorders());
    }

    
    static Collection<Experiment> getExpforCompletedSOrders(final ReadableVersion rv, final Calendar date) {
        assert date != null;

        final Protocol protocol = Utils.getSOProtocol(rv);
        final Map<String, Object> prop = Util.getNewMap();
        prop.put(Experiment.PROP_PROTOCOL, protocol);
        prop.put(Experiment.PROP_STATUS, Conditions.or(Conditions.eq(SOrdersManager.ExpStatus.OK.toString()),
            Conditions.eq(SOrdersManager.ExpStatus.Failed.toString())));
        final Calendar monthLater = (Calendar) date.clone();
        monthLater.roll(Calendar.MONTH, true);
        prop.put(LabBookEntry.PROP_CREATIONDATE, org.pimslims.search.Conditions.between(date, monthLater));

        final Collection<Experiment> exps = rv.findAll(Experiment.class, prop);

        return exps;
    }

    public static List<ExperimentGroup> getLastMonthCompletedRuns(final ReadableVersion rv) {
        return SOrdersManager.getLastMonthCompletedRuns(rv);
    }
    */

    public static List<ExperimentGroup> getLastMonthCompletedRuns(final ReadableVersion rv) {
        final Calendar monthAgo = Calendar.getInstance();
        monthAgo.roll(Calendar.MONTH, false);
        return SOrdersManager.getMonthCompletedRuns(rv, monthAgo);
    }

    public static List<ExperimentGroup> getMonthCompletedRuns(final ReadableVersion rv, final Calendar date) {
        final Collection<Note> notes = SOrdersManager.getNotesforCompletedRuns(rv, date);
        return SOrdersManager.getPlates(notes);
    }

    static List<ExperimentGroup> getPlates(final Collection<Note> notes) {
        if (notes.isEmpty()) {
            return new ArrayList<ExperimentGroup>();
        }
        final ArrayList<ExperimentGroup> plates = new ArrayList<ExperimentGroup>(notes.size());
        // Filter not relevant notes
        for (final Note note : notes) {
            final LabBookEntry plate = note.getParentEntry();
            if (!(plate instanceof ExperimentGroup)) {
                continue;
            }
            final ExperimentGroup run = (ExperimentGroup) plate;
            plates.add(run);
            //TODO look at the performance impact may be better to omit
            /*
             final Set<Experiment> exps = run.getExperiments();
             if (!exps.isEmpty()
                 && exps.iterator().next().getProtocol().getName().equalsIgnoreCase(
                     SequencingOrder.soProtocolName)) {
                 plates.add(run);
             }
             */
        }
        return plates;
    }

    static Collection<Note> getNotesforCompletedRuns(final ReadableVersion rv, final Calendar date) {
        assert date != null;

        final Map<String, Object> prop = Util.getNewMap();
        prop.put(Note.PROP_NAME, Conditions.eq(PlateNoteManager.TagType.CompletedRunTag.toString()));
        // Roll one day back to include the current date in selection as between operator excludes it
        final Calendar yesterday = (Calendar) date.clone();
        yesterday.roll(Calendar.DATE, false);
        final Calendar monthLater = (Calendar) date.clone();
        monthLater.roll(Calendar.MONTH, true);
        prop.put(Attachment.PROP_DATE, org.pimslims.search.Conditions.between(yesterday, monthLater));

        final Collection<Note> notes = rv.findAll(Note.class, prop);

        return notes;
    }

    static Collection<Note> getNotesForPlannedRuns(final ReadableVersion rv) {
        return SOrdersManager.getNotes(rv, PlateNoteManager.TagType.PlannedRunsTag);
    }

    static Collection<Note> getNotesForAwaitingResultsRuns(final ReadableVersion rv) {
        return SOrdersManager.getNotes(rv, PlateNoteManager.TagType.AwaitingResultsRunTag);
    }

    static Collection<Note> getNotes(final ReadableVersion rv, final PlateNoteManager.TagType noteType) {
        final Map<String, Object> prop = Util.getNewMap();
        prop.put(Note.PROP_NAME, Conditions.eq(noteType.toString()));
        final Collection<Note> notes = rv.findAll(Note.class, prop);
        return notes;
    }

    static Collection<Note> getNotesforLastMonthCompletedRuns(final ReadableVersion rv) {
        return SOrdersManager.getNotesforCompletedRuns(rv, Calendar.getInstance());
    }

    /*
    static Collection<Experiment> getExpforSOrders(final ReadableVersion rv,
        final SOrdersManager.ExpStatus status) {

        final Protocol protocol = Utils.getSOProtocol(rv);
        final Map<String, Object> prop = Util.getNewMap();

        prop.put(Experiment.PROP_PROTOCOL, protocol);

        switch (status) {
            // Completed orders
            case Failed:
                // This is leaking deliberately 
            case OK:
                throw new AssertionError(
                    "Please use getMonthCompletedRuns() method to retrieve completed Orders!");
                // Planned & awaiting results runs
            case In_process:
                // This takes agas to complete have tp abandon
                /*
                final Collection<Condition> cond = new ArrayList<Condition>();
                cond.add(Conditions.eq(SOrdersManager.ExpStatus.OK.toString()));
                cond.add(Conditions.eq(SOrdersManager.ExpStatus.Failed.toString()));
                cond.add(Conditions.eq(SOrdersManager.ExpStatus.In_process.toString()));
                prop.put(Experiment.PROP_STATUS, Conditions.or(cond));

                final Map<String, Object> expGroupCrit = new HashMap<String, Object>();
                expGroupCrit.put(BaseClass.PROP_DETAILS, NoteManager.awaitingResultsRunTag);
                //TODO it would be better to search for experimentGroups that do not have 
                // CompletedRunTag note but the search below does not work 

                //final Map<String, Object> expGroupCrit = new HashMap<String, Object>();
                //final Map<String, Object> noteCrit = new HashMap<String, Object>();
                //noteCrit.put(Note.PROP_NAME, NoteManager.completedTag);
                //expGroupCrit.put(LabBookEntry.PROP_ATTACHMENTS, Conditions.notExistsMap(noteCrit));
                prop.put(Experiment.PROP_EXPERIMENTGROUP, Conditions.andMap(expGroupCrit));
                //
                break;
            case To_be_run:
                // new orders
                prop.put(Experiment.PROP_STATUS, status.toString());
                prop.put(Experiment.PROP_EXPERIMENTGROUP, Conditions.isNull());
                break;
            case Unknown:
                prop.put(Experiment.PROP_STATUS, status.toString());
                break;
        }

        final Collection<Experiment> exps = rv.findAll(Experiment.class, prop);

        return exps;
    }
    */

    public static SequencingOrder getRefControls(final ReadableVersion rv, final boolean reaction) {
        final Collection<Parameter> params = SOrdersManager.getControlParams(rv, reaction);
        final List<Experiment> controls = new ArrayList<Experiment>();
        for (final ModelObject p : params) {
            controls.add(((Parameter) p).getExperiment());
        }

        if (!controls.isEmpty()) {
            return new SequencingOrder(controls);
        }
        return null;
    }

    public static List<ExperimentGroup> groupByPlate(final List<SequencingOrder> sorders) {
        /* TODO improve this
         * There is no efficient way to select certain groups at the moment 
         * this need a DM change
         */
        final List<ExperimentGroup> groups = new ArrayList<ExperimentGroup>();
        for (final SequencingOrder so : sorders) {
            final ExperimentGroup eg = so.getPlate();
            groups.add(eg);

        }
        return groups;
    }

    /*
    public static List<ExperimentGroup> groupByPlateAndRemove(final List<SequencingOrder> sorders) {
        /* TODO improve this
         * There is no efficient way to select certain groups at the moment 
         * this need a DM change
         *
        final List<ExperimentGroup> groups = new ArrayList<ExperimentGroup>();
        for (final SequencingOrder so : sorders) {
            final ExperimentGroup eg = this.so.getPlate();
            final List<Note> notes = eg.getNotes();
            if (notes.isEmpty()) {
                this.groups.add(eg);
            }
        }
        return groups;
    }
    */

    public static List<SequencingOrder> getNewOrders(final List<SequencingOrder> orders) {
        final List<SequencingOrder> newOrders = new ArrayList<SequencingOrder>();

        for (final SequencingOrder order : orders) {
            if (order.isNewOrder() && !order.isControl()) {
                newOrders.add(order);
            }
        }
        Collections.sort(newOrders);
        return newOrders;
    }

    /**
     * Orders which was recorded by the users but no further operation was completed
     * 
     * Underlaying experiments are at the status to_be_run
     * 
     * Next stage is layoutCompleted
     * 
     * Required to proceed to the next stage - sequencing machine administrator to plane orders/sample to the
     * plate.
     * 
     * SOrdersManager.getAllNotDoneSOrders
     * 
     * @param rv
     * @return public static List<SequencingOrder> getAllNotDoneSOrders(final List<SequencingOrder> sorders) {
     *         return SOrdersManager.getNewOrders(sorders); }
     * @throws ConstraintException
     * @throws AccessException
     */

    public ExperimentGroup setLayoutCompletedStep(final List<Experiment> experiments,
        final WritableVersion rw, final String runNumber) throws ConstraintException, AccessException {
        this.setProcessingStep(SequencingOrder.OrderProcessingStatus.LayoutDone);
        final ExperimentGroup eg = new ExperimentGroup(rw, runNumber, "Sequencing orders plate");
        eg.setCreationDate(Calendar.getInstance());

        SOrdersManager.setExperimentPositionAndRefNumber(experiments, rw, runNumber);
        eg.setExperiments(experiments);
        final PlateNoteManager nm = new PlateNoteManager(eg);
        nm.addPlannedRunTag(rw);
        return eg;
    }

    public void abandonPlannedRun() throws ConstraintException, AccessException {
        for (final SequencingOrder so : this.sOrders) {
            if (so.isLayoutComplete()) {
                so.revertPlannedRunToNewOrder();
            }
        }
    }

    /* There is no update for the initial sequencing order to be done
    // The next step is achieved via recording SeqSetupPlate experiment and linking 
    // their samples to the OutputSample of SequencingOrders
    // Therefore the method is static
    public static ExperimentGroup setPlateSetupCompletedStep(final ExperimentGroup sOrderEg,
        final List<SeqSetupExperiment> seqSetupExps, final String expGroupName) throws ConstraintException,
        AccessException {

        assert seqSetupExps != null && !seqSetupExps.isEmpty() : "No SeqSetupExperiments to record!";
        final WritableVersion rw = (WritableVersion) sOrderEg.get_Version();

        // The unique RUN number given by runnumber sequence is used in the sequencing setup experiment 
        // group name. It is generated and recorded in sequecing setup template. 
        // If the old template has been used then, this may happen  
        // Apparently there is no uniquness constraint on the experiment group name!
        assert expGroupName.startsWith("A");
        final String ssetupGName = "SS" + expGroupName.substring("A".length());
        final ExperimentGroup oldExpGroup =
            rw.findFirst(ExperimentGroup.class, ExperimentGroup.PROP_NAME, ssetupGName);
        if (oldExpGroup != null) {
            throw new AssertionError(
                "The Sequencing Setup Experiment group name is not unique! The name is : "
                    + expGroupName
                    + "\n"
                    + "Please check "
                    + " the RUN name in the sequencing setup spreadsheet and make sure you are using the right spreadsheet. ");
        }
        final ExperimentGroup sseEg = new ExperimentGroup(rw, ssetupGName, "Sequencing setup plate");
        sseEg.setCreationDate(Calendar.getInstance());

        // Plate to position Sequencing Setup Experiments
        final String holderName = "Sequencing Setup Plate for run " + ssetupGName;

        final HolderType holderType =
            rw.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well");
        assert null != holderType : "No '96 deep well' holder type! ";

        final HolderCategory holderCat =
            rw.findFirst(HolderCategory.class, HolderCategory.PROP_NAME, "96 deep well plate");
        assert null != holderCat : "No '96 deep well' holder category! ";

        final Holder holderForSSetupExps = Creator.recordHolder(rw, "96 deep well plate", holderName, null);
        holderForSSetupExps.setHolderType(holderType);
        holderForSSetupExps.setHolderCategories(Collections.singletonList(holderCat));

        // Here is the performance bottle-neck for this servlet
        final List<Experiment> exps = SeqSetupExperiment.record(seqSetupExps, rw);
        // Set OutputSample from Sequencing Order as InputSample of Sequencing Prep experiment
        // Watch the order!  
        Utils.linkExperiments(sOrderEg, exps, holderForSSetupExps);

        sseEg.setExperiments(exps);

        final PlateNoteManager nm = new PlateNoteManager(sOrderEg);
        nm.addAwaitingResultsRunTag(rw);

        return sseEg;
    }
    */
    public void setCompletedStep(final WritableVersion rw) throws ConstraintException, AccessException {
        this.setProcessingStep(SequencingOrder.OrderProcessingStatus.Completed);
    }

    private void setProcessingStep(final SequencingOrder.OrderProcessingStatus step)
        throws ConstraintException {
        for (final SequencingOrder so : this.sOrders) {
            // Update the step only the order has not reached it yet
            // TODO fix this ! getProcessingStep need to look at all exps 
            //if (so.getProcessingStep() != step) {
            so.updateProcessingStep(step);
            //}
        }
    }

    public Enum getStatus() {
        final Enum status = this.sOrders.iterator().next().getStatus();

        for (final SequencingOrder so : this.sOrders) {
            assert status == so.getStatus() : "Sequencing Orders with different statuses are not expected!";
        }

        return status;
    }

    /**
     * 
     * Util.isUnique
     * 
     * @param rv
     * @param property
     * @param propertyValue
     * @param javaClass
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public static boolean isNameUnique(final ReadableVersion rv, final String name, final Class javaClass) {

        ModelObject result;
        try {
            result = rv.findFirst(javaClass, (String) javaClass.getField("PROP_NAME").get(javaClass), name);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(
                "isNameUnique only works for PIMS classes! Check the name of the javaclass and make sure it contains PROP_NAME field!"
                    + e);
        } catch (final SecurityException e) {
            throw new RuntimeException(
                "isNameUnique only works for PIMS classes! Check the name of the javaclass and make sure it contains PROP_NAME field!"
                    + e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(
                "isNameUnique only works for PIMS classes! Check the name of the javaclass and make sure it contains PROP_NAME field!"
                    + e);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(
                "isNameUnique only works for PIMS classes! Check the name of the javaclass and make sure it contains PROP_NAME field!"
                    + e);
        }
        if (result != null) {
            return false;
        }
        return true;
    }

    /*
     * This method assumes that the List of experiments is ordered so that the first element position at the 
     * start of a plate e.g. A1
     */
    public static void setExperimentPositionAndRefNumber(final List<Experiment> exps,
        final WritableVersion rw, final String plateName) throws ConstraintException, AccessException {
        /* 
          This can only be done once per plate not per order!
        */
        assert exps != null && exps.size() <= 96 : "The number of experiments is zero or greater then a plate size (96). Cannot proceeed!";

        final HolderType holderType =
            rw.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well");
        assert null != holderType : "No '96 deep well' holder type! ";

        final HolderCategory holderCat =
            rw.findFirst(HolderCategory.class, HolderCategory.PROP_NAME, "96 deep well plate");
        assert null != holderCat : "No '96 deep well' holder category! ";

        // Plate to position Sequencing Setup Experiments
        String holderName = "Sequencing Order Plate " + plateName;

        //TODO better to find the last name and then iterate
        while (!SOrdersManager.isNameUnique(rw, holderName, Holder.class)) {
            holderName = Util.nextNumericName(holderName);
        }

        final Holder holder = Creator.recordHolder(rw, "96 deep well plate", holderName, null);
        holder.setHolderType(holderType);
        holder.setHolderCategories(Collections.singletonList(holderCat));

        int counter = -1;
        for (final Experiment exp : exps) {
            counter++;
            if (exp == null) {
                continue;
            }
            final String well = PlatePlanner.positions.get(counter);
            // Assign the reference number for a reaction
            exp.setName(new Long(Utils.getSeqRefNumber(rw)).toString());
            final Set<OutputSample> outs = exp.getOutputSamples();
            //System.out.println("pos:" + position);
            assert outs != null && !outs.isEmpty() : "No output is defined for experiment! Cannot put it into plate!";
            assert outs.size() == 1 : "More than one output is defined for experiment - cannot put it into plate!";

            final OutputSample outSample = outs.iterator().next();
            final Sample sample = outSample.getSample();
            assert sample != null : "Sample for Output sample is not defined. Cannot defined holder for this sample!";
            //sample.setName();
            sample.setRowPosition(HolderFactory.getRow(well) + 1);// the actual data in sample is indexed from 1
            sample.setColPosition(HolderFactory.getColumn(well) + 1);
            sample.setHolder(holder);
        }

    }

}
