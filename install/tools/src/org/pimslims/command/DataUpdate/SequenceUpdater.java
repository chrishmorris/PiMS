/**
 * current-pims-web
 * 
 * @author Petr Troshin
 * @date July 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.molecule.Molecule;

/**
 * PersonFixer
 * 
 * Remove persons created as citations authors to de-clutter database
 * 
 */
public class SequenceUpdater implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        Boolean allCorrect = true;

        final Collection<Molecule> mols =
            wv.findAll(Molecule.class, Molecule.PROP_MOLTYPE, "DNA");
        for (final Molecule mo : mols) {
            String seq = mo.getSeqString();
            if (seq.length() == 0) {
                continue;
            }
            final String updatedSeq = this.getUpdatedSequence(seq);
            if (!seq.equals(updatedSeq)) {
                allCorrect = false;
                mo.setSeqString(updatedSeq);
                System.out.println(mo + "'s DNA sequence is updated from " + seq);
                System.out.println("To " + updatedSeq + "\n");
                seq = updatedSeq;
            }

        }

        return allCorrect;
    }

    private String getUpdatedSequence(final String seq) {
        String result = "";
        final char[] chars = seq.toUpperCase().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            if (c == 'A' || c == 'C' || c == 'G' || c == 'T') {
                result += c;
            }

        }
        return result;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Check DNA sequence of all Molcomponent(type=DNA) ";
    }

}
