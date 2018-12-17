/**
 * current-pims-web org.pimslims.command.DataUpdate CharacterFixer.java
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
import org.pimslims.model.reference.Organism;
import org.pimslims.search.Conditions;

/**
 * CharacterFixer
 * 
 */
public class OrganismsFixer implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        //search in organisms' taxonomy id
        final Collection<Organism> orgs =
            wv.findAll(Organism.class, Organism.PROP_NCBITAXONOMYID, Conditions.contains(";"));
        for (final Organism org : orgs) {
            org.setNcbiTaxonomyId(org.getNcbiTaxonomyId().replace(";", ""));
        }
        if (orgs.size() != 0) {
            System.out.println(orgs.size() + " organisms have been fixed ");
        }
        return orgs.size() == 0;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {

        return "remove ';' in the taxonomy id of organisms";
    }

}
