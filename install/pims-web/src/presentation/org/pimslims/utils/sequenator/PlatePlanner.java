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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.utils.experiment.Utils;

/**
 * PlatePlanner
 * 
 * org.pimslims.utils.sequenator.PlatePlanner
 */
public class PlatePlanner {

    public static final int CAPILLARY_SIZE = 16;

    public static final int INIT_CONTROL_NUM = 2;

    public static final int PLATE_SIZE = 96;

    public int startWellIdx;

    private final String[] rcontrolWells = new String[] { "A01", "A03", "A05", "A07", "A09", "A11" };

    //    String[] icontrolWells = new String[] { "H02", "H04", "H06", "H08", "H10", "H12" };

    int[] icontrolWellNums = new int[] { 15, 31, 47, 63, 79, 95 };

    String initIcontrolPos = "H12";

    public static final List<String> positions = HolderFactory.POSITIONS_BY_COLUMN_96;

    // Sequencing orders
    LinkedList<SequencingOrder> layedOrders;

    // Sequencing orders
    LinkedList<SequencingOrder> notLayedOrders;

    // HashMap linking orders to a certain style to be displayed on a plate
    HashMap<String, String> orderStyles;

    Experiment rcontrol;

    Experiment icontrol;

    SOrdersManager som;

    // This object leaves longer then a version, therefore it cannot have it as instance
    public PlatePlanner(final String startingWell, final SOrdersManager som, final ReadableVersion rv) {
        assert !Util.isEmpty(startingWell) && startingWell.trim().toUpperCase().startsWith("A")
            && startingWell.trim().length() == 3;
        this.startWellIdx = PlatePlanner.positions.indexOf(startingWell);
        if (this.startWellIdx == -1) {
            this.startWellIdx = 0;
        }
        this.som = som;
        // Get reference controls
        //TODO Make sure that at least one reaction and one instrument control 
        // is defined in the database

        // Special case when one order is 95 or 96 samples 
        if (som.getSorders().size() == 1 && som.getSize() >= 95) {
            // Add reaction control only
            if (som.getSize() == 95) {
                this.addReactionControl(startingWell, rv);
            }
            // Do not add controls if order have 96 samples 
        } else {
            this.addReactionControl(startingWell, rv);
            this.addInstrumentControl(rv);
        }
        this.notLayedOrders = som.getSOrderByDate();
        this.layOrders(rv);
        this.calculateOrdersStyles();
    }

    public void addReactionControl(final String startingWell, final ReadableVersion rv) {
        final SequencingOrder rcso = SOrdersManager.getRefControls(rv, true);
        assert rcso.getSize() > 0 : "No Reaction controls found";
        // Make sure starting well is valid for r.controls
        assert Arrays.asList(this.rcontrolWells).contains(startingWell);
        // Propose one r.control by default
        this.rcontrol = rcso.getOneExperiment();
    }

    public SOrdersManager getSOManager() {
        return this.som;
    }

    void addInstrumentControl(final ReadableVersion rv) {
        final SequencingOrder icso = SOrdersManager.getRefControls(rv, false);
        assert icso.getSize() > 0 : "No Instrument controls found";
        // Propose one i.control by default
        final Experiment icExp = icso.getOneExperiment();
        assert icExp != null;
        this.icontrol = icExp;
    }

    public int getNumPositionsAvail() {
        return PlatePlanner.PLATE_SIZE - this.startWellIdx - this.getNumControls();
    }

    public int getTotalNumPositionsAvail() {
        return PlatePlanner.PLATE_SIZE - this.startWellIdx;
    }

    public int getNumFreePositions() {
        return PlatePlanner.PLATE_SIZE - this.startWellIdx - this.getTotalSampleNum();
    }

    public int getNumControls() {
        return this.getIControlNum() + this.getRControlNum();
    }

    public Experiment getRcontrol() {
        return this.rcontrol;
    }

    public void setRControl(final String expHook, final ReadableVersion rv) {
        assert Util.isHookValid(expHook);
        final Experiment experiment = rv.get(expHook);
        this.rcontrol = experiment;
    }

    public void removeRControl() {
        this.rcontrol = null;
    }

    public void setIControl(final String expHook, final ReadableVersion rv) {
        assert Util.isHookValid(expHook);
        final Experiment control = rv.get(expHook);
        this.icontrol = control;
    }

    public Experiment getIcontrol() {
        return this.icontrol;
    }

    public void removeIControl() {
        this.icontrol = null;
    }

    // Return index in the list before which all elements are included 
    private void layOrders(final ReadableVersion rv) {

        int positionsEngaged = 0;
        final LinkedList<SequencingOrder> olist = new LinkedList<SequencingOrder>();
        for (int i = 0; i < this.notLayedOrders.size(); i++) {
            final SequencingOrder so = this.notLayedOrders.get(i);
            final int size = so.getSize();
            positionsEngaged += size;
            if (this.getNumPositionsAvail() - positionsEngaged >= 0) {
                olist.add(so);
            } else {
                // subtract the size of the order which cannot be fit into the plate
                positionsEngaged -= size;
                continue;
            }
        }
        // Put layed orders in the beginning of the list
        this.layedOrders = olist;
        this.diffLists(olist);
        //this.adjustIControlPosition(rv);
    }

    // This method is only called once at the first layout 
    public String getIcontrolPosition() {
        // -1  minus one instrument control 
        final int snum = this.startWellIdx + this.getTotalSampleNum() - 1;
        //System.out.println("SNUm " + snum);
        final String icPos = this.getNearestIControlPosition(snum);
        // System.out.println("POS " + icPos);

        return icPos;
    }

    public String getStartingWell() {
        return PlatePlanner.positions.get(this.startWellIdx);
    }

    public boolean getCanFitIntoPlate() {
        // Number of wells in a plate from starting well  >= Number of samples + controls
        return this.getTotalNumPositionsAvail() >= this.getTotalSampleNum();
    }

    public int getRControlNum() {
        return this.rcontrol != null ? 1 : 0;
    }

    public int getIControlNum() {
        return this.icontrol != null ? 1 : 0;
    }

    public ArrayList<Experiment> getLayedSampleList() {

        final ArrayList<Experiment> samples = new ArrayList<Experiment>(96);
        assert this.getCanFitIntoPlate() : "Samples do not fit into the Plate! Please remove some orders!";

        // Make sure the capacity of the vestor is what of a plate
        // Add empty samples if any at the start of a plate
        for (int i = 0; i < this.startWellIdx; i++) {
            samples.add(null);
        }
        assert samples.size() == this.startWellIdx;
        // Add samples from the orders
        int scounter = this.startWellIdx;
        for (final SequencingOrder so : this.getLayedOrders()) {

            final List<Experiment> exps = so.getExperiments();
            scounter += exps.size();
            samples.addAll(exps);
        }
        assert samples.size() == scounter;

        // Add reaction controls
        if (this.rcontrol != null) {
            samples.add(this.startWellIdx, this.rcontrol);
        }
        assert samples.size() == scounter + this.getRControlNum();

        // Add instrument controls
        if (this.icontrol != null) {
            final int icIdx = PlatePlanner.positions.indexOf(this.getIcontrolPosition());
            if (icIdx > samples.size()) {
                this.addEmptyWells(samples, icIdx);
            }
            samples.add(icIdx, this.icontrol);
        }
        // Make sure the capacity of the vector is what of a plate
        samples.trimToSize();
        assert samples.size() <= 96 : "Number of samples is " + samples.size() + " that is more than 96! ";

        return samples;
    }

    private void addEmptyWells(final ArrayList<Experiment> samples, final int endIdx) {
        for (int i = samples.size(); i < endIdx; i++) {
            samples.add(null);
        }
    }

    private String getNearestIControlPosition(final int sampleNumber) {
        for (int i = 0; i < this.icontrolWellNums.length; i++) {
            if (this.icontrolWellNums[i] >= sampleNumber) {
                return PlatePlanner.positions.get(this.icontrolWellNums[i]);
            }
        }
        return null;
    }

    public List<SequencingOrder> getLayedOrders() {
        return this.layedOrders;
    }

    public List<SequencingOrder> getNotLayedOrders() {
        return this.notLayedOrders;
    }

    public List<SequencingOrder> getAllOrders() {
        final LinkedList<SequencingOrder> allOrders = new LinkedList<SequencingOrder>(this.layedOrders);
        allOrders.addAll(this.notLayedOrders);
        return allOrders;
    }

    private void diffLists(final LinkedList<SequencingOrder> layedOrders) {
        for (final SequencingOrder so : new LinkedList<SequencingOrder>(this.notLayedOrders)) {
            if (layedOrders.contains(so)) {
                this.notLayedOrders.remove(so);
            }
        }
    }

    public void addOrder(final String orderId) {
        final SequencingOrder so = this.getOrderById(orderId, false);
        assert so != null;
        this.notLayedOrders.remove(so);
        this.layedOrders.add(so);
    }

    private SequencingOrder getOrderById(final String orderId, final boolean fromLayedOrdersList) {
        List<SequencingOrder> listOrders = this.notLayedOrders;
        if (fromLayedOrdersList) {
            listOrders = this.layedOrders;
        }
        for (final SequencingOrder so : listOrders) {
            if (so.getId().equalsIgnoreCase(orderId)) {
                return so;
            }
        }
        return null;
    }

    public void removeOrder(final String orderId) {
        final SequencingOrder so = this.getOrderById(orderId, true);
        assert so != null;

        this.layedOrders.remove(so);
        this.notLayedOrders.add(so);
    }

    public void moveOneOrderUp(final String orderId) {
        final SequencingOrder so = this.getOrderById(orderId, true);
        assert so != null;
        final int initialIdx = this.layedOrders.indexOf(so);
        assert initialIdx != -1;
        if (initialIdx > 0) {
            final SequencingOrder oneup = this.layedOrders.get(initialIdx - 1);
            this.layedOrders.set(initialIdx, oneup);
            this.layedOrders.set(initialIdx - 1, so);
        }
    }

    public void moveOneOrderDown(final String orderId) {
        final SequencingOrder so = this.getOrderById(orderId, true);
        assert so != null;

        final int initialIdx = this.layedOrders.indexOf(so);
        if (initialIdx < this.layedOrders.size()) {
            final SequencingOrder oneDown = this.layedOrders.get(initialIdx + 1);
            this.layedOrders.set(initialIdx, oneDown);
            this.layedOrders.set(initialIdx + 1, so);
        }

    }

    public void makeOrderFirst(final String orderId) {
        final SequencingOrder so = this.getOrderById(orderId, true);
        assert so != null;
        final int initialIdx = this.layedOrders.indexOf(so);
        final SequencingOrder first = this.layedOrders.peek();
        this.layedOrders.set(0, so);
        this.layedOrders.set(initialIdx, first);
    }

    public void makeOrderLast(final String orderId) {
        final SequencingOrder so = this.getOrderById(orderId, true);
        assert so != null;
        this.layedOrders.remove(so);
        this.layedOrders.add(so);
    }

    public int getTotalSampleNum() {
        int count = this.getNumControls();
        for (final SequencingOrder so : this.layedOrders) {
            count += so.getSize();
        }
        return count;
    }

    public HashMap<String, String> getOrdersStyles() {
        return this.orderStyles;
    }

    private void calculateOrdersStyles() {
        final String[] orderStyles =
            new String[] { "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "s12", "s13",
                "s14" };

        // Map OrderID->display class 
        final HashMap<String, String> orderIds = new HashMap<String, String>();
        for (final SequencingOrder so : this.getAllOrders()) {
            for (final Experiment experiment : so.getExperiments()) {
                assert experiment.getProtocol().getName().equalsIgnoreCase(SequencingOrder.soProtocolName);
                final String orderId = Utils.getParameterValue(experiment, "Order ID");
                orderIds.put(orderId, null);
            }
        }

        int counter = -1;
        for (final Map.Entry<String, String> ent : orderIds.entrySet()) {

            counter = counter + 1;
            if (counter >= orderStyles.length) {
                counter = 0;
            }
            final String nClass = orderStyles[counter];

            ent.setValue(nClass);
        }
        this.orderStyles = orderIds;
    }
}
