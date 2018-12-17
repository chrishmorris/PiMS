/**
 * V2_0-pims-web org.pimslims.command.DataUpdate LeedsPrimerDataFixer.java
 * 
 * @author Marc Savitsky
 * @date 10 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;

/**
 * LeedsPrimerDataFixer
 * 
 */
public class LeedsDataUpdater implements IDataFixer {

    public static void FixPrimerData(final WritableVersion wv, final Sample sample) throws AccessException,
        ConstraintException, AbortedException {

        final Set<SampleComponent> sampleComps = sample.getSampleComponents();
        if (sampleComps.size() != 1) {
            //continue;
            return;
        }
        final SampleComponent scomp = sampleComps.iterator().next();

        final Molecule molcomponent = (Molecule) wv.get(scomp.getRefComponent().get_Hook());

        if (molcomponent instanceof Primer) {
            System.out.println("Processing Primer [" + sample.getName() + "]");
            final PrimerBean bean = PrimerBeanReader.readPrimer(sample);
            org.pimslims.presentation.PrimerBeanWriter.update(wv, bean);

            // check
            final PrimerBean beanb = org.pimslims.presentation.PrimerBeanReader.readPrimer(sample);
            assert (beanb.getLengthOnGeneString().equals(bean.getLengthOnGeneString()));
            assert (beanb.getParticularity().equals(bean.getParticularity()));
            assert (beanb.getRestrictionSite().equals(bean.getRestrictionSite()));
            assert (beanb.getTmseller().equals(bean.getTmseller()));
            assert (beanb.getOD().equals(bean.getOD()));
            assert (beanb.getGCGene().equals(bean.getGCGene()));
        }

    }

    @SuppressWarnings("deprecation")
    public static void FixExpParameterData(final WritableVersion wv) throws AccessException,
        ConstraintException {

        final Collection<Parameter> parameters = wv.getAll(Parameter.class);
/* obsolete
        for (final Parameter parameter : parameters) {

            if (parameter.getName().equals(FormFieldsNames.plasmid)) {
                final String value = parameter.getValue();
                if (value.startsWith("ccp.api.Experiment")) {
                    final String newValue = "org.pimslims.model.experiment" + value.substring(18);
                    System.out.println("Update Parameter [" + parameter.getExperiment().getName() + ":"
                        + parameter.getName() + " with " + newValue + "]");
                    parameter.setValue(newValue);
                }
            }
        } */
    }

    public static void main(final String[] args) throws AccessException, ConstraintException,
        AbortedException {

        final AbstractModel model = ModelImpl.getModel();

        final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        System.out.println("Fix Leeds Primer Data");

        final Collection<Sample> samples = wv.getAll(Sample.class, 0, Integer.MAX_VALUE);
        final Queue<Sample> queue = new LinkedList<Sample>(samples);

        while (!queue.isEmpty()) {

            final WritableVersion version =
                model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                LeedsDataUpdater.FixPrimerData(version, queue.remove());
                version.commit();

            } finally {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }
        }

        System.out.println("Fix Leeds Experiment Parameters");
        try {
            LeedsDataUpdater.FixExpParameterData(wv);
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        System.out.println("Leeds Primer Data Updater has finished!");
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        final Collection<Sample> samples = wv.getAll(Sample.class, 0, Integer.MAX_VALUE);
        final Queue<Sample> queue = new LinkedList<Sample>(samples);
        try {
            while (!queue.isEmpty()) {
                LeedsDataUpdater.FixPrimerData(wv, queue.remove());
            }
        } catch (final AbortedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        LeedsDataUpdater.FixExpParameterData(wv);
        return null;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Update old OPPF/MPSI primer data";
    }

}
