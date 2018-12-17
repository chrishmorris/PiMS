/**
 * this class is collection of function which will try to fix the data in DB org.pimslims.upgrader
 * DataFixer.java
 * 
 * @date 21-Dec-2006 10:32:13
 * 
 * @author Bill
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.upgrader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;

public abstract class DataFixer {
    private static final AbstractModel model = ModelImpl.getModel();

    /**
     * this founction correct Milestone Date for "Select" As "Select" should be happened before any other
     * action (milestone) if found it is later than earlest date, it should be correct as before earlest.
     * 
     * @return true id succeeded
     */
    @Deprecated
    // can exhaust memory
    public static boolean CheckAndfixMileStoneDate() {
        boolean everythingIsCorrect = true;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Collection<Target> ts = wv.getAll(Target.class, 0, Integer.MAX_VALUE);
            TargetStatus defaultTs = wv.findFirst(TargetStatus.class, TargetStatus.PROP_NAME, "Selected");
            for (Target t : ts) {
                Collection<Milestone> milestones = t.getMilestones();
                if (milestones != null && milestones.size() > 1) {
                    // find the earliest date
                    Date firstDate = getFirstDate(milestones);
                    for (Milestone ms : milestones)
                        if (ms.getStatus().getName().equalsIgnoreCase("Selected")) {
                            // check the date of "Select"
                            if (ms.getDate().after(firstDate)) {
                                // fix as (earliest date -1)
                                Calendar newCalDate = Calendar.getInstance();
                                newCalDate.setTime(new Date(firstDate.getTime() - 1000));
                                ms.setDate(newCalDate);
                                assert !ms.getDate().after(firstDate);
                                everythingIsCorrect = false;
                            }
                        }
                } else if (defaultTs != null && milestones.size() == 0) {
                    new Milestone(wv, Calendar.getInstance(), defaultTs, t);
                    everythingIsCorrect = false;
                }

            }
            wv.commit();
        } catch (ModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return everythingIsCorrect;
    }

    /**
     * @param ms
     * @return
     */
    private static Date getFirstDate(Collection<Milestone> milestones) {
        Date firstDate = new Date();// which is today
        for (Milestone ms : milestones)
        // if(!ms.getCode().getName().equalsIgnoreCase("Selected"))
        {
            if (firstDate.after(ms.getDate().getTime()))
                firstDate = ms.getDate().getTime();
        }
        return firstDate;
    }

    /**
     * 
     */
    @Deprecated
    // this has done its job
    public static boolean CheckAndFixExperiment() {
        boolean everythingIsCorrect = true;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // fix the following problem
            /*
             * (3) In Spot, there are 2 ways to get expblueprints from experiment: (as it is two way, we can
             * say "There are 2 ways to get experiments from expblueprint" as well) (i)
             * experiment.getExpblueprints () (ii) experiment.getBlueprintStatus().getExpblueprints ()
             * Currently, (i) and (ii) not include each other. However, I think (i) should includes (ii).
             * BlueprintStatus are replaced by milestone. But above problem is still there. Should put (ii)
             * into (i)
             */
            Collection<ResearchObjective> expbs = wv.getAll(ResearchObjective.class, 0, Integer.MAX_VALUE);
            for (ResearchObjective expb : expbs) {
                Collection<Experiment> oldExps = expb.getExperiments();
                Collection<Experiment> addExps = new HashSet<Experiment>();
                for (Milestone milestone : expb.getMilestones()) {
                    if (milestone.getExperiment() != null)
                        addExps.add(milestone.getExperiment());
                }
                Set<Experiment> newExps = new HashSet<Experiment>(oldExps);
                for (Experiment addexp : addExps)
                    if (!oldExps.contains(addexp))
                        newExps.add(addexp);

                assert newExps.size() >= oldExps.size();
                if (newExps.size() > oldExps.size()) {
                    expb.setExperiments(newExps);
                }
            }
            // fix the following problem
            /*
             * (1) The results of experiment is stored in experiment.details(SPOT) and experiment.Status(MPSI)
             * the details should transfered and moved to status
             */
            Collection<Experiment> exps = wv.getAll(Experiment.class, 0, Integer.MAX_VALUE);
            for (Experiment exp : exps) {
                String details = exp.getDetails();
                exp.setDetails(null);
                String status = exp.getStatus();
                if (details == null || details.trim().length() == 0) {
                    if (status != null && !status.equals("OK") && !status.equals("Failed")
                        && !status.equals("To_be_run") && !status.equals("In_process"))
                        exp.setStatus(null);
                } else if (details.trim().equalsIgnoreCase("Yes") && (status == null || !status.equals("OK")))
                    exp.setStatus("OK");
                else if (details.trim().equalsIgnoreCase("No")
                    && (status == null || !status.equals("Failed")))
                    exp.setStatus("Failed");
                else if (details.trim().equalsIgnoreCase("0") && (status == null || !status.equals("Failed")))
                    exp.setStatus("Failed");
                else if (details.trim().equalsIgnoreCase("1") && (status == null || !status.equals("OK")))
                    exp.setStatus("OK");
                else if (details.trim().equalsIgnoreCase("1I")
                    && (status == null || !status.equals("Failed")))
                    exp.setStatus("Failed");
                else if (details.trim().equalsIgnoreCase("1S") && (status == null || !status.equals("OK")))
                    exp.setStatus("OK");
                else if (details.trim().equalsIgnoreCase("2I")
                    && (status == null || !status.equals("Failed")))
                    exp.setStatus("Failed");
                else if (details.trim().equalsIgnoreCase("2S") && (status == null || !status.equals("OK")))
                    exp.setStatus("OK");
                else if (details.trim().equalsIgnoreCase("2") && (status == null || !status.equals("OK")))
                    exp.setStatus("OK");
                else if (details.trim().equalsIgnoreCase("3S") && (status == null || !status.equals("OK")))
                    exp.setStatus("OK");
                else {
                    exp.setStatus("To_be_run");
                    exp.setDetails(details);
                }
            }

            everythingIsCorrect = !wv.getSession().isDirty();
            wv.commit();

        } catch (ModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return everythingIsCorrect;

    }

    /**
     * 
     */
    @Deprecated
    // can exhaust memory
    public static boolean CheckAndFixExperimentParameters() {
        boolean everythingIsCorrect = true;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Collection<Parameter> params = wv.getAll(Parameter.class, 0, Integer.MAX_VALUE);
            for (Parameter param : params) {
                // fix the name
                if (param.getName() == null || param.getName().trim().length() == 0)
                    if (param.getParameterDefinition() != null) {
                        param.setName(param.getParameterDefinition().getName());
                        everythingIsCorrect = false;
                    }
                // remove duplication
                Collection<Parameter> otherParams = param.getExperiment().getParameters();
                otherParams.remove(param);
                for (Parameter otherParam : otherParams) {
                    if (otherParam.getParameterDefinition() == param.getParameterDefinition())
                        if (otherParam.getValue() != null && param.getValue() == null) {
                            param.delete();
                            everythingIsCorrect = false;
                            break;
                        }
                }
            }

            wv.commit();

        } catch (ModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return everythingIsCorrect;

    }

    static boolean checkAndFixMolTypeForSequence() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        boolean everythingIsCorrect;
        try {
            Collection<Molecule> ts = wv.getAll(Molecule.class, 0, Integer.MAX_VALUE);
            for (Molecule mc : ts) {

                String seq = mc.getSequence();
                String molType = null;
                if (seq == null)
                    molType = "other";
                else if (Update_v24.isValidDNA(seq))
                    molType = "DNA";
                else if (Update_v24.isValidProtien(seq))
                    molType = "protein";
                else
                    molType = "other";
                if (seq != null && seq.contains("1")) {
                    System.err.println("following sequence contains '1' which may be a mistake:");
                    System.err.println("Other<" + mc.get_Hook() + ">: " + seq);
                }
                if (!molType.equalsIgnoreCase(mc.getMolType())) {

                    System.err.println("<" + mc.get_Hook() + "> change molType from " + mc.getMolType()
                        + " to " + molType);
                    System.err.println(seq);
                    mc.setMolType(molType);
                }

            }
            everythingIsCorrect = !wv.getSession().isDirty();
            if (!everythingIsCorrect)
                wv.commit();
        } catch (ModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return everythingIsCorrect;
    }

    @Deprecated
    // can exhaust memory
    static boolean RemoveDuplicatedSelectMilestone() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        boolean everythingIsCorrect;
        try {
            Collection<Target> ts = wv.getAll(Target.class, 0, Integer.MAX_VALUE);
            for (Target t : ts) {
                Collection<Milestone> milestones = t.getMilestones();
                if (milestones != null && milestones.size() > 1) {// find the
                    // earliest
                    // date
                    Date firstDate = getFirstDate(milestones);
                    // System.out.println("First date: "+firstDate);
                    for (Milestone ms : milestones)
                        if (ms.getStatus().getName().equalsIgnoreCase("Selected")) {
                            // System.out.println("Current date:
                            // "+ms.getDate());
                            assert !ms.getDate().before(firstDate);
                            // delete this wrong milestone
                            if (ms.getDate().after(firstDate)) {
                                ms.delete();
                            }
                        }
                }

            }
            everythingIsCorrect = !wv.getSession().isDirty();
            if (!everythingIsCorrect)
                wv.commit();
        } catch (ModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return everythingIsCorrect;
    }

    static boolean RemoveDuplicatedWorkflowItem() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        boolean everythingIsCorrect = true;
        try {
            Collection<TargetStatus> statuss = wv.getAll(TargetStatus.class, 0, Integer.MAX_VALUE);
            Collection<ExperimentType> expTypes = wv.getAll(ExperimentType.class, 0, Integer.MAX_VALUE);
            for (TargetStatus status : statuss)
                for (ExperimentType expType : expTypes) {
                    Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
                    attrMap.put(WorkflowItem.PROP_EXPERIMENTTYPE, expType);
                    attrMap.put(WorkflowItem.PROP_STATUS, status);
                    Collection<WorkflowItem> workflowItems = wv.findAll(WorkflowItem.class, attrMap);
                    if (workflowItems.size() <= 1)
                        continue;
                    System.out.println("Found duplication!");
                    int _number = 0;
                    for (WorkflowItem workflowItem : workflowItems) {
                        if (_number > 0)
                            workflowItem.delete();
                        _number++;
                    }

                }
            everythingIsCorrect = !wv.getSession().isDirty();
            wv.commit();

        } catch (ModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return everythingIsCorrect;
    }

    static void RemoveNullExpName(ModelUpdateVersionImpl muv) throws SQLException {
        DatabaseUpdater dbu = muv.getDbUpdater();
        ResultSet rs =
            dbu.sqlExecuteQuery("select memopsbaseclassid,name from expe_experiment where name is NULL");
        while (rs.next()) {
            Long id = rs.getLong("memopsbaseclassid");
            String newName = "Experiment" + id;
            rs.updateString("name", newName);
            System.out.println("Experiment:" + id + " 's name is NULL, changed to " + newName);
            rs.updateRow();
        }
        rs.close();

    }

    /**
     * namingSystem has been removed
     */
    /*static void RemoveSPOTConstructDesignNamingsystem(WritableVersion wv) throws AccessException,
        ConstraintException, SQLException {
        final String spotConstructName = "SPOT Construct Design";
        Collection<ExperimentType> expTypes =
            wv.findAll(ExperimentType.class, ExperimentType.PROP_NAME, spotConstructName);
        ExperimentType defaultExpType =
            wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "SPOTConstructDesign");
        assert defaultExpType != null;
        assert defaultExpType.getNamingSystem().equalsIgnoreCase("default");
        if (expTypes.size() >= 1) {
            System.out
                .println("There are more than ExperimentType which name is 'SPOT Construct Design' or 'SPOTConstructDesign' but different nameingSystem");
            // get the default ExperimentType

            for (ExperimentType expType : expTypes) {
                if (expType.getNamingSystem().equalsIgnoreCase("default")) {
                    defaultExpType = expType;
                    break;
                }
            }

            for (ExperimentType expType : expTypes)
                if (expType != defaultExpType) {
                    // transfer the experiments to default one
                    Collection<Experiment> allExpTypeExp = defaultExpType.getExperiments();
                    allExpTypeExp.addAll(expType.getExperiments());
                    for (Experiment exp : allExpTypeExp) {
                        updateExpType(wv, exp, defaultExpType);
                    }
                    System.out.println("Experiments of ExperimentType (" + expType.getName() + ","
                        + expType.getNamingSystem() + ") are transfered to (" + defaultExpType.getName()
                        + "," + defaultExpType.getNamingSystem() + ")");

                    // transfer experiment of portocol to default one
                    Collection<Protocol> protocols = expType.getProtocols();
                    for (Protocol protocol : protocols) {
                        Collection<Protocol> defaultProtocols =
                            defaultExpType.findAll(ExperimentType.PROP_PROTOCOLS, Protocol.PROP_NAME,
                                protocol.getName());
                        assert defaultProtocols.size() == 1;
                        Protocol defaultProtocol = defaultProtocols.iterator().next();

                        Set<Experiment> allProtocolExp = defaultProtocol.getExperiments();
                        allProtocolExp.addAll(protocol.getExperiments());
                        defaultProtocol.setExperiments(allProtocolExp);
                        System.out.println("Experiments of Protocol (" + protocol.getName() + ","
                            + protocol.getExperimentType().getNamingSystem() + ") are transfered to ("
                            + defaultProtocol.getName() + ","
                            + defaultProtocol.getExperimentType().getNamingSystem() + ")");
                        // remove protocol
                        expType.doRemove(ExperimentType.PROP_PROTOCOLS, protocol);
                        protocol.delete();
                        wv.flush();
                    }

                    // remove ExperimentType
                    expType.delete();
                    wv.flush();
                }
        }

    }*/

    /**
     * Experiment.name is now the unique key for an experiment, so it is unchangeable The setName() method
     * does not exits.
     * 
     * @throws ConstraintException
     */
    /*
     * public static boolean CheckAndFixExperimentName() { boolean everythingIsCorrect=true; WritableVersion
     * wv = model .getWritableVersion(AbstractModel.SUPERUSER); try{ Collection<Experiment>
     * exps=wv.getAll(Experiment.class, 0, Integer.MAX_VALUE); for(Experiment exp:exps) { //fix the name
     * if(exp.getName()==null||exp.getName().trim().length()==0) if(exp.getExperimentType()!=null) {
     * exp.setName(exp.getExperimentType().getName()+exp.getDbId()); } }
     * everythingIsCorrect=!wv.getSession().isDirty(); wv.commit(); } catch (ModelException e) {
     * e.printStackTrace(); throw new RuntimeException(e); }finally { if (!wv.isCompleted()) { wv.abort(); } }
     * return everythingIsCorrect; }
     */
    @SuppressWarnings("deprecation")
    public static void updateExpType(WritableVersion wv, Experiment exp, ExperimentType newET)
        throws SQLException, ConstraintException {
        wv.flush();
        DatabaseUpdater dbu = new DatabaseUpdater(wv.getSession().connection());
        ResultSet rs =
            dbu.sqlExecuteQuery("select memopsbaseclassid, experimenttypeid from expe_experiment where memopsbaseclassid="
                + exp.getDbId());
        while (rs.next()) {
            rs.updateLong("experimenttypeid", newET.getDbId());
            rs.updateRow();
        }
        rs.close();
        wv.getSession().refresh(exp);
    }
}
