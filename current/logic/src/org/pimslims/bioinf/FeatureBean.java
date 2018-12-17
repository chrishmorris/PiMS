/**
 * V2_0-pims-web org.pimslims.presentation.bioinf FeatureBean.java
 * 
 * @author Petr aka pvt43
 * @date 8 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.bioinf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.biojava.bio.Annotation;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.ontology.SimpleComparableTerm;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;

/**
 * FeatureBean
 * 
 */
public class FeatureBean implements Comparable<FeatureBean> {

    private final RichFeature feature;

    /**
     * 
     */
    public FeatureBean(final RichFeature feature) {
        this.feature = feature;
    }

    public static ArrayList<FeatureBean> getFeatureBeans(final CdsList bio) {

        return bio.getFeatureBeans();
    }

    public String getLocation() {
        return this.format(this.feature.getLocation().toString());
    }

    /*
     * Remove biojavax: and add spaces, so that line can be wrapped 
     * biojavax:join:[<31347..31646,32133..32410,32730..32840,32883..33222,33831..34016] 
     */
    private String format(String location) {
        if (location.startsWith("biojavax:")) {
            location = location.substring("biojavax:".length());
            String newloc = "";
            final String[] loc = location.split(",");
            for (int i = 0; i < loc.length; i++) {
                newloc += loc[i] + ", ";
            }
            if (!Util.isEmpty(newloc)) {
                location = newloc;
            }
        }
        return location;
    }

    public String getName() {
        return this.feature.getName();
    }

    public TreeMap<String, String> getAnnotation() {
        //return new TreeMap(feature.getAnnotation().asMap());
        final TreeMap<String, String> tm = new TreeMap();
        final Annotation an = this.feature.getAnnotation();
        for (final Iterator iterator = an.keys().iterator(); iterator.hasNext();) {
            final SimpleComparableTerm entry = (SimpleComparableTerm) iterator.next();
            final String key = entry.getName();
            String val = (String) an.getProperty(key);
            if (key.equalsIgnoreCase("translation") && val.length() > 60) {
                val = Utils.StringFormatter.getFormatedLine(val);
            }
            tm.put(key, val);
        }
        return tm;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final FeatureBean o) {
        return new Integer(this.feature.getLocation().getMin()).compareTo(o.feature.getLocation().getMin());
    }

}
