/**
 * org.pimslims.presentation.constructconstruct PrimerMaker.java
 * 
 * @author cm65
 * @date 2 Jun 2009
 * 
 *       Protein Information ManagementPrimerMakerersion: 2.2
 * 
 *       Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.construct;

import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PrimerBean;

/**
 * PrimerMaker
 * 
 */
@Deprecated
// obsolete
public interface PrimerDesigner {

    /**
     * PrimerDesigner.calcFwdPrimer Prepends the tag to the primer sequence
     * 
     * @param cb the construct bean - this method has a side effect on the dna sequence
     * @param pb the primer bean
     */
    public abstract void calcFwdPrimer(final ConstructBean cb, final PrimerBean pb);

    public abstract void calcRevPrimer(final ModelObjectShortBean cb, final PrimerBean pb);

}