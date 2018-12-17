/**
 * 
 */
package uk.ac.sspf.spot.beans;

import org.pimslims.model.target.Target;
import org.pimslims.presentation.worklist.ConstructProgressBean;

/**
 * Bean to back the filter form on the Construct Progress page
 * 
 * @author Jon Diprose
 */
@Deprecated
public class ConstructFilterBean {

    public static final String RESULT_PARAMETER = "result";

    /**
     * The parameter name to use for milestone
     */
    public static final String MILESTONE_PARAMETER = "milestone";

    /**
     * The parameter name to use for milestone
     */
    public static final String ORGANISM_PARAMETER = "organism";

    /**
     * The parameter name to use for milestone
     */

    @Deprecated
    // target.project is no longer used
    public static final String PROJECT_PARAMETER = "project";

    /**
     * The parameter name to use for milestone
     */
    public static final String PROTEIN_NAME_PARAMETER = "proteinName";

    // constants for result filter
    public static final String RESULT_ANY = "any";

    public static final String RESULT_OK = "OK";

    public static final String RESULT_FAILED = "Failed";

    public static final String DAYS_AGO_PARAMETER = "daysAgo";

    /**
     * The string to use to filter by milestone
     */
    private String milestone = "";

    /**
     * The string to use to filter by organism
     */
    private String organism = "";

    /**
     * The string to use to filter by project
     */
    private String project = "";

    /**
     * The string to use to filter by proteinName
     */
    private String proteinName = "";

    private String result = ConstructFilterBean.RESULT_ANY;

    private Integer daysAgo;

    /**
     * Zero-arg Constructor
     */
    public ConstructFilterBean() {
        /* empty */
    }

    /**
     * @return Returns the milestone.
     */
    public String getMilestone() {
        return this.milestone;
    }

    /**
     * @param milestone The milestone to set.
     */
    public void setMilestone(String milestone) {
        if (milestone == null) {
            milestone = "";
        } else if (milestone.equalsIgnoreCase("any")) {
            milestone = "";
        }
        this.milestone = milestone;
    }

    /**
     * @return Returns the organism.
     */
    public String getOrganism() {
        return this.organism;
    }

    /**
     * @param organism The organism to set.
     */
    public void setOrganism(String organism) {
        if (organism == null) {
            organism = "";
        }
        this.organism = organism;
    }

    /**
     * @return Returns the project.
     */
    @Deprecated
    // target.project is no longer used
    public String getProject() {
        return this.project;
    }

    /**
     * @param project The project to set.
     */
    @Deprecated
    // target.project is no longer used
    public void setProject(String project) {
        if (project == null) {
            project = "";
        }
        this.project = project;
    }

    /**
     * @return Returns the proteinName.
     */
    public String getProteinName() {
        return this.proteinName;
    }

    /**
     * @param proteinName The proteinName to set.
     */
    public void setProteinName(String proteinName) {
        if (proteinName == null) {
            proteinName = "";
        }
        this.proteinName = proteinName;
    }

    /**
     * Getter for MILESTONE_PARAMETER for JSPs
     */
    public final String getMILESTONE_PARAMETER() {
        return ConstructFilterBean.MILESTONE_PARAMETER;
    }

    /**
     * Getter for ORGANISM_PARAMETER for JSPs
     */
    public final String getORGANISM_PARAMETER() {
        return ConstructFilterBean.ORGANISM_PARAMETER;
    }

    /**
     * Getter for PROJECT_PARAMETER for JSPs
     */
    @Deprecated
    // target.project is no longer used
    public final String getPROJECT_PARAMETER() {
        return ConstructFilterBean.PROJECT_PARAMETER;
    }

    /**
     * Getter for PROTEIN_NAME_PARAMETER for JSPs
     */
    public final String getPROTEIN_NAME_PARAMETER() {
        return ConstructFilterBean.PROTEIN_NAME_PARAMETER;
    }

    /**
     * Getter for RESULT_PARAMETER for JSPs
     */
    public final String getRESULT_PARAMETER() {
        return ConstructFilterBean.RESULT_PARAMETER;
    }

    /**
     * Getter for DAYS_AGO_PARAMETER for JSPs
     */
    public final String getDAYS_AGO_PARAMETER() {
        return ConstructFilterBean.DAYS_AGO_PARAMETER;
    }

    /**
     * Simple filter on organism
     * 
     * @param spotTarget
     * @param organism
     * 
     * @return True if the filter term is found in the organism
     * @see ConstructFasta#containsIgnoreCase(String, String)
     */
    private boolean organismFilter(final String organism) {

        // Apply our filter
        return ConstructFilterBean.containsIgnoreCase(organism, this.getOrganism());

    }

    /**
     * Simple filter on protein name
     * 
     * @param proteinName
     * @param spotTarget
     * 
     * @return True if the filter term is found in the protein name
     * @see ConstructFasta#containsIgnoreCase(String, String)
     */
    private boolean proteinNameFilter(final String proteinName) {

        // Apply our filter
        return ConstructFilterBean.containsIgnoreCase(proteinName, this.getProteinName());

    }

    /**
     * Implementation of a case-insensitive string contains string function. Returns true if searchFor is null
     * or empty. Returns false if toSearch is null.
     * 
     * @param toSearch - the String to search
     * @param searchFor - the String to search for
     * @return true if searchFor is in toSearch (or toSearch is null or empty), otherwise false
     */
    public static boolean containsIgnoreCase(final String toSearch, final String searchFor) {

        // True if we have no searchFor
        if (searchFor == null) {
            return true;
        }

        // True if searchFor is empty
        if ("".equals(searchFor)) {
            return true;
        }

        // False if toSearch is null
        if (toSearch == null) {
            return false;
        }

        // True if we can find searchFor in toSeach, case-insensitive
        if (toSearch.toUpperCase().indexOf(searchFor.toUpperCase()) > -1) {
            return true;
        }

        // False if we get here
        return false;

    }

    /**
     * Do the filtering
     * 
     * @param target
     * @return true if the target should be in the report
     */
    public boolean isWanted(final Target target) {
        final String organism = ConstructFilterBean.getOrganism(target);

        //final String project = ConstructFilterBean.getProject(target);

        // Filter here on protein name, organism and project
        final boolean isWanted =
            this.proteinNameFilter(target.getProtein().getName()) && this.organismFilter(organism)
        /*&& this.projectFilter(project) */;
        return isWanted;
    }

    // target.Project is no longer used
    public static String getOldProject(final Target target) {
        final String project = "";
        /*if (0 < target.getProjects().size()) {
            project = target.getProjects().iterator().next().getShortName();
        } */
        return project;
    }

    public static String getOrganism(final Target target) {
        String organism = "";
        if (null != target.getSpecies()) {
            organism = target.getSpecies().getName();
        }
        return organism;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    /**
     * @return Returns the result.
     */
    public String getResult() {
        return this.result;
    }

    public boolean isWanted(final ConstructProgressBean progress) {
        if (null != this.daysAgo && this.daysAgo > progress.getDaysAgo()) {
            return false;
        }
        if (!ConstructFilterBean.RESULT_ANY.equals(this.result) && !this.result.equals(progress.getResult())) {
            return false;
        }
        if (this.milestone != null && this.milestone.trim().length() > 0
            && !this.milestone.equalsIgnoreCase(progress.getMilestone())) {
            return false;
        }
        return true;
    }

    public void setDaysAgo(final Integer i) {
        this.daysAgo = i;

    }

    /**
     * @return Returns the daysAgo.
     */
    public Integer getDaysAgo() {
        return this.daysAgo;
    }

}
