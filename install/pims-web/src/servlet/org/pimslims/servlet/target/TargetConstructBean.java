/*
 * Created on April 2007
 */
package org.pimslims.servlet.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.servlet.utils.ProgressListener;

/**
 * @author Petr Troshin The bean is used by ReportTargetConstruct servlet and corresponding jsp page.
 * 
 */
@Deprecated
// has a copy of a model object
public class TargetConstructBean implements Comparable<TargetConstructBean> {

    // Target target; // Also used in the TargetDecorator
    ConstructBean construct;

    Target target;

    /**
     * @param ModelObject (instanceof org.pimslims.model.target.Target)
     * 
     */
    public TargetConstructBean(final ConstructBean construct, final Target target) {
        this.construct = construct;
        this.target = target;
    }

    public ModelObjectShortBean getConstruct() {
        return this.construct;
    }

    public String getConstructName() {
        return this.construct.getName();
    }

    /**
     * Getters of the properties of interest
     */

    /*
     * This methods not quite correct cause it returns the first DNA from the unsorted collection, but at this
     * moment PIMS does not handle this correctly anyway, so not to worry.
     */
    public String getDna() {
        // Alternatively. Which one you are interested in?
        // return construct.getDnaSeq();

        String sequence = "";
        final Set<Molecule> dnas = this.target.getNucleicAcids();
        if (dnas != null && !dnas.isEmpty()) {
            final Molecule dna = dnas.iterator().next();
            if (dna != null) {
                sequence = dna.getSequence();
            }
        }
        return sequence;
    }

    @Deprecated
    public String getLocalName() {
        return this.target.getName();
    }

    public String getName() {
        return this.target.getName();
    }

    public String getComments() {
        return this.construct.getComments();
    }

    public String getProtein() {
        return this.construct.getFinalProt();
        /*
         * String sequence = ""; Molecule protein = target.getProtein(); if(protein != null) { sequence =
         * protein.getSeqString(); } return sequence;
         */
    }

    public Target getTarget() {
        return this.target;
    }

    public String getOrganismName() {
        final Organism species = this.target.getSpecies();
        if (species != null) {
            return species.getName();
        }
        return "";
    }

    public int compareTo(final TargetConstructBean cb) {
        final String thisname = this.getName();
        final String thatname = cb.getName();
        Integer tname = null;
        Integer name = null;
        try {
            name = Integer.decode(thisname);
            tname = Integer.decode(thatname);
        } catch (final NumberFormatException nex) {
            System.out.println("cannot compare" + nex.getMessage());
            name = new Integer(0);
            tname = name;
        }
        return name.compareTo(tname);
    }

    public static java.util.List<TargetConstructBean> getAll(
        final Collection<ResearchObjectiveElement> blueComponent, final ProgressListener listener)
        throws java.io.IOException {

        final java.util.List<TargetConstructBean> targetcbs =
            Collections.synchronizedList(new ArrayList<TargetConstructBean>());
        int count = 0;
        for (final ResearchObjectiveElement bc : blueComponent) {
            final ResearchObjective expb = bc.getResearchObjective();
            if (expb != null) {
                final ConstructBean cb = ConstructBeanReader.readConstruct(expb);
                final TargetConstructBean tcb = new TargetConstructBean(cb, bc.getTarget());
                if (cb != null) {
                    count = count + 1;
                    if (count % 20 == 0 && listener != null) {
                        listener.setProgress(count);
                    }
                    targetcbs.add(tcb);
                }
            }
        }

        return targetcbs;
    }

}
