package org.pimslims.dao;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;


/**
 * For use by wizards. The opening page of the wizard will: session.put(wizardName, getLongWritableVersion());
 * lwv.startRead(); //do this before attempting to read and maybe something like Protocol protocol =
 * lwv.create(Protocol.class, attributes); session.put("protocol", protocol); protocol.add("procedures",
 * procedure2)
 * 
 * The middle pages of the wizard will do something like lwv = session.get(wizardName); Protocol protocol =
 * session.get("protocol"); Collection procedures = protocol.get("procedures"); lwv.startRead(); // processing
 * lwv.commitRead();
 * 
 * The last page of the wizard normally: lwv.save(); // now can get hooks from new objects lwv.commit(); or:
 * lwv.abort() and then: session.put(wizardName, null);
 * 
 * 
 * @author cm65
 * 
 */
@Deprecated
public interface LongWritableVersion extends WritableVersion {

    /**
     * Must be called before calling read methods. The application must call commitRead() before any long
     * delay, e.g. waiting for user input
     * 
     * 
     * 
     * @throws AbortedException if the database is in use The caller can retry this step.
     */
    public void startRead() throws AbortedException;

    /**
     * Drops the current read access to database.
     * 
     * @throws AbortedException if a conflicting update has been detected
     */
    public void commitRead() throws AbortedException;

    /**
     * Writes the changes to the database. This must be followed by a call to commit() or abort(), before the
     * next long delay, e.g. before waiting for user interaction.
     * 
     * @throws ConstraintException
     * @throws AbortedException
     */
    public void save() throws AccessException, ConstraintException, AbortedException;

    /**
     * Get an existing object. If called before save(), this method will not return newly created objects.
     * 
     * @param hook of the object
     * @see org.pimslims.dao.ReadableVersion#get(java.lang.String)
     */
    public ModelObject get(String hook);

}
