package org.pimslims.utils.orders;

import java.io.IOException;
import java.util.Collection;

import org.pimslims.presentation.PrimerBean;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.util.File;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.dao.WritableVersion;

public interface PrimerOrderForm {

    /**
     * Add a primer to the order form
     * 
     * @param primer
     */
    public abstract void addPrimer(PrimerBean primer);

    /**
     * Remove a primer from the order form
     * 
     * @param primer
     */
    public abstract void removePrimer(PrimerBean primer);

    /**
     * Add a collection of primers to the order form
     * 
     * @param primers
     */
    public abstract void addAll(Collection<PrimerBean> primers);

    /**
     * Remove all the primers from the order form
     */
    public abstract void removeAll();

    /**
     * Generate and save the order form, attaching it to the specified experiment.
     * 
     * @param expt - the Experiment to which to attach the generated order form
     * @param wv - the WritableVersion to which to write the order form.
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    public abstract File saveOrderForm(ModelObject object, WritableVersion wv) throws AccessException,
        ConstraintException, IOException;

}
