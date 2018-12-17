package uk.ac.sspf.spot.pims;

import java.util.Calendar;
import java.util.Comparator;

import org.pimslims.presentation.worklist.ConstructProgressBean;

/**
 * Comparator for sorting ConstructProgressBeans on the reverse dateOfExperimentDate natural order
 * 
 * TODO Should this be the natural sort order for ConstructProgressBeans? If so, make ConstructProgressBean
 * implement Comparable and reimplement this code in ConstructProgressBean's compareTo method
 * 
 * @author Jon Diprose
 */
@Deprecated
public class ProgressDateComparator implements Comparator {

    public int compare(final Object a, final Object b) {

        // If a is a ConstructProgressBean
        if (a instanceof ConstructProgressBean) {

            // If b is a ConstructProgressBean
            final int i = -1;
            if (b instanceof ConstructProgressBean) {

                // Return the results of comparing the two
                return this.compare((ConstructProgressBean) a, (ConstructProgressBean) b);

            }
            return i;

        }

        // Else if b is a ConstructProgressBean return more than
        else if (b instanceof ConstructProgressBean) {
            return 1;
        } else {
            return 0;
        }

    }

    private int compare(final ConstructProgressBean a, final ConstructProgressBean b) {

        // None of these should be true, but best to check
        if ((a == null) && (b == null)) {
            return 0;
        }
        if ((a == null) && (b != null)) {
            return -1;
        }
        if ((a != null) && (b == null)) {
            return 1;
        }

        // Parse dates back out
        final Calendar ad = a.getDateOfExperimentDate();
        final Calendar bd = b.getDateOfExperimentDate();

        // Check what we got back
        if ((ad == null) && (bd == null)) {
            return 0;
        }
        if ((ad == null) && (bd != null)) {
            return -1;
        }
        if ((ad != null) && (bd == null)) {
            return 1;
        }

        // Return the result of comparing the dates
        return bd.compareTo(ad);

    }

}
