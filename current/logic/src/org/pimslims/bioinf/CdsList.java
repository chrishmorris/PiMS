/**
 * V4_3-web org.pimslims.bioinf CdsList.java
 * 
 * @author cm65
 * @date 13 Oct 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.FeatureHolderUtils;
import org.biojava.bio.symbol.Location;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;

/**
 * CdsList
 * 
 */
public class CdsList {

    private final RichSequence sequence;

    public CdsList(final RichSequence sequence) {
        super();
        this.sequence = sequence;
    }

    /**
     * CdsList.getSequence
     * 
     * @return
     */
    private RichSequence getSequence() {
        return this.sequence;
    }

    /**
     * CdsList.getCDSList
     * 
     * @return
     */
    private FeatureHolder getCDSList() {
        final FeatureHolder fh = this.getSequence().filter(new FeatureFilter.ByType("CDS"), true);
        return fh;
    }

    /**
     * CdsList.hasCDS
     * 
     * @return
     */
    public boolean hasCDS() {
        final RichSequence seq = this.getSequence();
        final FeatureHolder fh = seq.filter(new FeatureFilter.ByType("CDS"), true);
        return fh.countFeatures() > 0;
    }

    /**
     * CdsList.isSingleCDS
     * 
     * @return
     */
    public boolean isSingleCDS() {
        final RichSequence seq = this.getSequence();
        final FeatureHolder cds = seq.filter(new FeatureFilter.ByType("CDS"));
        return cds.countFeatures() <= 1; // This can be either 0 or 1 for single cds record, as some record does not
        // contain CDS declaration, but they are single CDS nonetheless
    }

    /**
     * CdsList.getCDS
     * 
     * // This is fragile! Better to rely on feature properties, but this is // require implementation. For
     * now feature number should be fine, as feature // storage backed up by ArrayList
     * 
     * @param featureNumber
     * @return
     */
    public RichFeature getCDS(final int featureNumber) {
        final FeatureHolder fholder = this.getCDSList();
        //could assert 0 != fholder.countFeatures() : "No features in: " + seq;
        int counter = 0;
        for (final Iterator iterator = fholder.features(); iterator.hasNext();) {
            final RichFeature feature = (RichFeature) iterator.next();
            if (counter == featureNumber) {
                return feature;
            }
            counter++;
        }
        return null;
    }

    /**
     * CdsList.getCDSByLocation
     * 
     * @param start
     * @param end
     * @return
     */
    public RichFeature getCDSByLocation(final int start, final int end) {
        for (final Iterator iterator = this.getCDSList().features(); iterator.hasNext();) {
            final RichFeature rf = (RichFeature) iterator.next();
            //final Annotation annotation = rf.getAnnotation();
            //System.out.println(annotation.getProperty("protein_id"));

            //System.out.println(rf.getNoteSet());

            final Location location = rf.getLocation();
            //System.out.println(location); //TODO remove
            if (location.getMin() == start && location.getMax() == end) {
                return rf;
            }
        }
        return null;
    }

    /**
     * CdsList.getCDSByLocation
     * 
     * @param start
     * @param end
     * @return
     */
    public RichFeature getCDSByProtein(final String proteinName) {
        for (final Iterator iterator = this.getCDSList().features(); iterator.hasNext();) {
            final RichFeature rf = (RichFeature) iterator.next();
            final Annotation annotation = rf.getAnnotation();
            //System.out.println(annotation.getProperty("gene"));
            final String proteinVersion = (String) annotation.getProperty("protein_id");// e.g. "CAA61933.1"
            if (proteinVersion.startsWith(proteinName + ".")) {
                return rf;
            }

        }
        return null;
    }

    /**
     * CdsList.getCDSByName
     * 
     * @param cdsName
     * @return
     */
    public RichFeature getCDSByName(final String cdsName) {
        for (final Iterator iterator = this.getCDSList().features(); iterator.hasNext();) {
            final RichFeature rf = (RichFeature) iterator.next();
            if (rf.getName().equals(cdsName)) {
                return rf;
            }
        }
        return null;
    }

    /**
     * CdsList.getFeatureBeans
     * 
     * @return
     */
    public ArrayList<FeatureBean> getFeatureBeans() {
        final FeatureHolder fholder = this.getCDSList();
        final ArrayList<FeatureBean> fbs = new ArrayList<FeatureBean>(fholder.countFeatures() + 1);
        final Set<RichFeature> fhs = FeatureHolderUtils.featureHolderAsSet(fholder);
        for (final RichFeature rf : fhs) {
            fbs.add(new FeatureBean(rf));
        }
        Collections.sort(fbs);
        return fbs;
    }

}
