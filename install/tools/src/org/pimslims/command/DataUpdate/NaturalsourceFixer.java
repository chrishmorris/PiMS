/**
 * current-pims-web org.pimslims.command.DataUpdate NaturalsourceFixer.java
 * 
 * @author bl67
 * @date 20 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.Organism;
import org.pimslims.search.Searcher;

/**
 * NaturalsourceFixer
 * 
 */
public class NaturalsourceFixer implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        Boolean allCorrect = true;
        final Searcher s = new Searcher(wv);

        final Collection<ModelObject> nss = s.searchAll(wv.getMetaClass(Organism.class), "NCBI_TaxID=");
        for (final ModelObject modelObject : nss) {
            final Organism ns = (Organism) modelObject;
            if (ns.getNcbiTaxonomyId().contains("NCBI_TaxID=")) {
                final String oldValue = ns.getNcbiTaxonomyId();
                ns.setNcbiTaxonomyId(ns.getNcbiTaxonomyId().replace("NCBI_TaxID=", ""));
                System.out.println("ncbitaxonomyid is updated to '" + ns.getNcbiTaxonomyId() + "' from '"
                    + oldValue + "'");
                allCorrect = false;
            }
        }

        return allCorrect;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "remove 'NCBI_Tax_ID=' from ncbitaxonomyid";
    }

}
