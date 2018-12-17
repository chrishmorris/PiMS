/*
 * Created on 31.08.2005
 */
package org.pimslims.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;

/**
 * @author Petr Troshin
 * 
 *         Make beans based on some information from Target, Status, MileStone(TargetStatus) model objects The
 *         bean is used by BrowseTarget servlet and corresponding jsp page.
 */
public class TargetBeanForLists extends ModelObjectShortBean implements Comparable<Object>, Serializable {

    //TODO don't save this, get attributes from super.getValues
    Target target; // Also used in the TargetDecorator

    private Calendar statusDate;

    private String statusName;

    private String tgrShortName;

    /**
     * MAX_STATUSES int
     */
    public static final int MAX_STATUSES = 100;

    /**
     * Used for sorting statuses into date order
     */
    public static final Comparator MOST_RECENT_STATUS = new Comparator() {
    
        public int compare(final Object arg0, final Object arg1) {
            final Milestone status0 = (Milestone) arg0;
            final Milestone status1 = (Milestone) arg1;
            if (status1.getDate().equals(status0.getDate())) {
                return status1.getDbId().compareTo(status0.getDbId());
            }
            return status1.getDate().compareTo(status0.getDate());
        }
    
    };

    //private Collection<ModelObject> targetGroups;

    /**
     * @param ModelObject (instanceof org.pimslims.model.target.Target)
     * 
     */
    public TargetBeanForLists(final Target target) {
        super(target);
        this.target = target;
        Milestone milestone = null;
        TargetStatus status = null;

        milestone = TargetBeanForLists.getCurrentTargetMilestone(target);
        if (milestone != null) {
            status = milestone.getStatus();
        }
        this.setStatusDate(milestone);
        this.setStatus(status);

        //final Project project = target.findFirst(Target.PROP_PROJECTS);
        //this.setProject(project);

        final Object tgroup = ServletUtil.getModelObject(Target.PROP_TARGETGROUPS, this.target);
        if (null == tgroup || tgroup instanceof ModelObject) {
            this.setTargetGroup((ModelObject) tgroup);
        } else {
            final ArrayList tgroups = new ArrayList((Collection) tgroup);
            this.setTargetGroup((ModelObject) tgroups.get(0));
        }
    }

    /**
     * Getters for the Target, Status, MileStone(TargetStatus) properties
     * 
     * @Deprecated public String getLocalName() { return this.getCommonName(); }
     */

    @Override
    public String getHook() {
        return this.target.get_Hook();
    }

    public String getDetails() {
        return (String) this.target.get_Value(LabBookEntry.PROP_DETAILS);
    }

    public String getWhyChosen() {
        return (String) this.target.get_Value(Target.PROP_WHYCHOSEN);
    }

    public String getFunctionDescription() {
        return (String) this.target.get_Value(Target.PROP_FUNCTIONDESCRIPTION);
    }

    public String getProteinName() {
        if (null == this.target.getProtein()) {
            return "";
        }
        return this.target.getProtein().getName();
    }

    public String getAliases() {
        final Set<Alias> aliases = this.target.getAliases();
        String ret = "";
        for (final Alias alias : aliases) {
            ret += alias.getName() + "; ";
        }
        return ret;
    }

    /* target.project is no longer used
    @Deprecated
    public String getProject() {
        return this.prjShortName;
    } */

    public String getProjectList() {
        if (null == this.target) {
            return "";
        }
        if (null == this.target.getAccess()) {
            return "";
        }
        return this.target.getAccess().get_Name();
    }

    void setTargetGroup(final ModelObject targetGroup) {
        if (null == targetGroup) {
            this.tgrShortName = "";
        } else {
            this.tgrShortName = (String) targetGroup.get_Value(TargetGroup.PROP_NAME);
        }
    }

    public String getTargetGroup() {
        return this.tgrShortName;
    }

    public String getTargetGroupList() {
        if (null == this.target) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        int i = 0;
        for (final TargetGroup group : this.target.getTargetGroups()) {
            if (i > 0) {
                sb.append("; ");
            }
            sb.append((String) group.get_Value(TargetGroup.PROP_NAME));
            i++;
        }
        return sb.toString();
    }

    private void setStatus(final ModelObject mileStone) {
        String status = null;
        if (mileStone != null) {
            status = (String) mileStone.get_Value(TargetStatus.PROP_NAME);
        }
        this.statusName = (status == null ? "" : status);
    }

    public String getStatus() {
        return this.statusName;
    }

    void setStatusDate(final ModelObject status) {
        if (status != null) {
            this.statusDate = (Calendar) status.get_Value(Milestone.PROP_DATE);
        } else {
            this.statusDate = java.util.Calendar.getInstance();
        }
    }

    public Calendar getStatusDate() {
        return this.statusDate;
    }

    public String getCommonName() {
        return (String) this.target.get_Value(Target.PROP_NAME);
    }

    /**
     * Method is used to sort target by they common name
     */
    @Override
    public int compareTo(final Object obj) {
        if (!(obj instanceof TargetBeanForLists)) {
            throw new ClassCastException("obj1 is not a TargetBean! ");
        }
        String curCommonName = ((TargetBeanForLists) obj).getCommonName();
        String objCommonName = this.getCommonName();
        // Instance of Leeds commonName
        if (curCommonName.indexOf("UL") >= 0 && objCommonName.indexOf("UL") >= 0) {
            curCommonName =
                curCommonName.substring(curCommonName.indexOf("UL") + 2, curCommonName.indexOf("UL") + 6);
            objCommonName =
                objCommonName.substring(objCommonName.indexOf("UL") + 2, objCommonName.indexOf("UL") + 6);
            Integer curInt = null;
            Integer objInt = null;
            try {
                curInt = Integer.valueOf(curCommonName);
                objInt = Integer.valueOf(objCommonName);
            } catch (final NumberFormatException e) {
                // Than assume they are equal
                curInt = 0;
                objInt = 0;
            }
            return objInt.compareTo(curInt);
        }
        return curCommonName.compareTo(objCommonName);
    }

    /**
     * @param target
     * @return its current status code
     * 
     */
    @SuppressWarnings("unchecked")
    public static Milestone getCurrentTargetMilestone(final Target target) {
        final List statuses = new LinkedList(target.getMilestones());
        if (0 == statuses.size()) {
            return null;
        }
        Collections.sort(statuses, TargetBeanForLists.MOST_RECENT_STATUS);
        final Milestone current = (Milestone) statuses.get(0);
        return current;
    }

}
