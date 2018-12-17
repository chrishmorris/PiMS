package org.pimslims.presentation.construct;

import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PrimerBean;

/**
 * This class annotates SPOT constructs It has a constructor that accepts a SPOT construct By Johan?
 */
@Deprecated
// obsolete
public class PimsPrimerDesigner implements PrimerDesigner {

    /**
     * @param cb org.pimslims.presentation.construct.ConstructBean
     * 
     * @Deprecated // not used public static void designPrimers(final ConstructBean cb) {
     * 
     *             final PrimerDesigner pd = PropertyGetter.getInstance("Primer.Designer",
     *             PimsPrimerDesigner.class); //Not for DNA Target constructs if (null == cb.getDnaTarget() ||
     *             "".equals(cb.getDnaTarget())) {
     * 
     *             if (cb.getTargetProtStart() == null || cb.getTargetProtEnd() == null) { throw new
     *             IllegalStateException( "Please only run designPrimers() on the constructBean after values
     *             have been given for start and stop positions"); } ConstructAnnotator.calcExpressedProt(cb);
     *             ConstructAnnotator.calcFinalProt(cb); } for (final Iterator iterator =
     *             cb.getPrimers().iterator(); iterator.hasNext();) { final PrimerBean pb = (PrimerBean)
     *             iterator.next(); if ("forward".equals(pb.getDirection())) { pd.calcFwdPrimer(cb, pb); }
     *             else { pd.calcRevPrimer(cb, pb); } } //calcExpressedProt(cb); //calcFinalProt(cb); }
     */

    /**
     * PrimerDesigner.calcFwdPrimer Prepends the tag to the primer sequence
     * 
     * @param cb the construct bean - this method has a side effect on the dna sequence
     * @param pb the primer bean
     */
    public void calcFwdPrimer(final ConstructBean cb, final PrimerBean pb) {
        if ("A".equals(pb.getSequence())) {
            cb.setDnaSeq("A" + cb.getDnaSeq().substring(1));
        }
        pb.setSequence(pb.getTag() + pb.getSequence());
        throw new AssertionError("beleived obsolete");
    }

    public void calcRevPrimer(final ModelObjectShortBean cb, final PrimerBean pb) {
        /* was final String compstring =
            PrimerDesigner.reverseComplement(cb.getDnaSeq().substring(
                cb.getDnaSeq().length() - cb.getRevOverlapLen().intValue(), cb.getDnaSeq().length())); */
        pb.setSequence(pb.getTag() + pb.getSequence());
        throw new AssertionError("beleived obsolete");
    }

}
