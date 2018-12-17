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
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Citation;
import org.pimslims.model.people.Person;

/**
 * PersonFixer
 * 
 * Remove persons created as citations authors to de-clutter database
 * 
 */
public class PersonAndCitationRemover implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        Boolean allCorrect = true;

        final Collection<Citation> cits = wv.getAll(Citation.class, 0, Integer.MAX_VALUE);
        for (final Citation cit : cits) {
            System.out.println("Citation " + cit.get_Hook() + " title " + cit.getTitle() + " is deleted ");
            cit.delete();
            allCorrect = false;
        }

        return allCorrect;
    }

    public void deletePerson(final Set<Person> authors) throws AccessException, ConstraintException {
        for (final Person author : authors) {
            if (author.getUsers() != null || !author.getUsers().isEmpty()) {
                continue; // make sure that not to delete a user person, however should not be any
            }
            System.out.println("Citation author " + author.get_Hook() + " Surname " + author.getFamilyName()
                + " is deleted");
            author.delete();
        }
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Remove Persons who associated with Citation (citation authors) and citations themselves";
    }

}
