/*
 * Created on 25-Aug-2004 @author Chris Morris, c.morris@dl.ac.uk PIMS project, www.pims-lims.org Copyright
 * (C) 2006 Daresbury Lab Daresbury, Warrington WA4 4AD, UK
 */

package org.pimslims.lab.sample;

import java.util.HashSet;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;

/**
 * Factory methods for sample components (RefSampleComponent)
 * 
 */
public class SampleComponentFactory {

    /**
     * Create a MolComponent
     * 
     * @param componentCategories list of strings representing categories
     * @param name
     * @return a representation of the new abstract component
     */
    public static Molecule createMolComponent(final WritableVersion wv, final String name,
        final java.util.List componentCategories) {
        final Map attributes = new java.util.HashMap();
        try {
            attributes.put(Substance.PROP_NAME, name);
            attributes.put(Substance.PROP_CATEGORIES, new HashSet(componentCategories));
            return new Molecule(wv, attributes);
        } catch (final ModelException ex) {
            wv.abort();
            throw new RuntimeException(ex);
        }
    }

}
