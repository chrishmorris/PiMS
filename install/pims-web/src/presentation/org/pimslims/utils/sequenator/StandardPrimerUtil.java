/**
 * current-pims-web org.pimslims.utils.sequenator StandardPrimerUtil.java
 * 
 * @author pvt43
 * @date 21 Apr 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeSet;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;

/**
 * StandardPrimerUtil
 * 
 */
public class StandardPrimerUtil {

    /**
     * @web function StandardPrimerUtil.getPrimerList
     * @param rv
     * @return
     */
    public static TreeSet<PrimerBean> getPrimerList(final ReadableVersion rv) {
        final Map<String, Object> prop = Util.getNewMap();
        final SampleCategory sc =
            rv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Standard Sequencing Primer");
        if (sc == null) {
            throw new RuntimeException(
                "Please run loadSampleCategories ant task to create 'Standard Sequencing Primer' sample category required");
        }
        prop.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sc));
        final Collection<Sample> standardPrimers = rv.findAll(Sample.class, prop);
        final TreeSet<PrimerBean> names = new TreeSet<PrimerBean>();

        for (final Sample sample : standardPrimers) {
            final PrimerBean pb = PrimerBeanReader.readPrimer(sample);
            //pb.getName()
            //pb.getSequence()
            names.add(pb);
        }
        assert !names.isEmpty() : "No standard primers found! Please make sure to run loadStandardPrimers ant task from build.xml";
        return names;
    }

    /**
     * @web function StandardPrimerUtil.getPrimerListforJSArray
     * @param rv
     * @return
     */
    public static String getPrimerListforJSArray(final ReadableVersion rv) {
        final Map<String, Object> prop = Util.getNewMap();
        final SampleCategory sc =
            rv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Standard Sequencing Primer");
        prop.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sc));
        final Collection<Sample> standardPrimers = rv.findAll(Sample.class, prop);
        String primers = "";
        for (final Sample sample : standardPrimers) {
            primers += "'" + sample.getName() + "'" + ",";
        }
        if (0 == primers.length()) {
            return "";
        }
        return primers.substring(0, primers.length() - 1);
    }

    /**
     * 
     * StandardPrimerUtil.isStandardPrimer
     * 
     * @param rv
     * @param primerName
     * @return true if primer is a standard primer, false otherwise
     */
    public static boolean isStandardPrimer(final ReadableVersion rv, final String primerName) {
        assert !Util.isEmpty(primerName);
        final Primer primer = rv.findFirst(Primer.class, Substance.PROP_NAME, primerName.trim());
        return primer != null;
    }
}
