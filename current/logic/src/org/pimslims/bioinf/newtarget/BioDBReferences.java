/**
 * current-pims-web org.pimslims.command.newtarget BioDBReference.java
 * 
 * @author Petr Troshin
 * @date 29 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.biojavax.CrossRef;
import org.biojavax.RankedCrossRef;
import org.biojavax.SimpleRankedCrossRef;

/**
 * BioDBReferences Class to handle BioJava cross database references
 */
public class BioDBReferences {

    public HashSet<BioDBReference> dbreferences;

    public BioDBReferences(final Set<RankedCrossRef> crossrefs) {
        assert crossrefs != null && !crossrefs.isEmpty();
        this.dbreferences = new HashSet<BioDBReference>(crossrefs.size());
        for (final Iterator iterator = crossrefs.iterator(); iterator.hasNext();) {
            final SimpleRankedCrossRef xrref = (SimpleRankedCrossRef) iterator.next();
            this.addReference(xrref.getCrossRef());
        }
    }

    public BioDBReferences(final BioDBReference ref) {
        this.dbreferences = new HashSet<BioDBReference>();
        this.dbreferences.add(ref);
    }

    @Override
    public String toString() {
        String str = "";
        for (final BioDBReference biorf : this.dbreferences) {
            str += biorf.toString();
        }
        return str;
    }

    void addReference(final CrossRef xref) {
        this.addReference(xref.getDbname(), xref.getAccession());
    }

    void addReference(final String database, final String accession) {
        if (this.dbreferences == null) {
            this.dbreferences = new HashSet<BioDBReference>();
        } else {
            //TODO do not allow 2 references with the same key!
            //dbreferences.contains(new BioDBReference(database, accession));
        }
        this.dbreferences.add(new BioDBReference(database, accession));
    }

    BioDBReference getXReference(final String dbname) {
        for (final BioDBReference bioref : this.dbreferences) {
            if (bioref.getXReference(dbname) != null) {
                return bioref.getXReference(dbname);
            }
        }
        return null;
    }

    String getAccession(final String dbname) {
        final BioDBReference bioxref = this.getXReference(dbname);
        if (bioxref != null) {
            return bioxref.accession;
        }

        return null;
    }

    public static class BioDBReference {
        public String databaseName;

        public String accession;

        public BioDBReference(final String databaseName, final String accession) {
            this.databaseName = databaseName;
            this.accession = accession;
        }

        BioDBReference getXReference(final String databaseName) {
            if (this.databaseName.equals(databaseName)) {
                return this;
            }
            return null;
        }

        @Override
        public String toString() {
            return "Database " + this.databaseName + " Accession " + this.accession + "\n";
        }

    }// end of enclosed class 

}
