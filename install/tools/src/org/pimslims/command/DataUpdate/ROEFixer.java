/**
 * pims-web org.pimslims.command.DataUpdate ROEFixer.java
 * 
 * @author Marc Savitsky
 * @date 12 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.search.Paging;

/**
 * ROEFixer
 * 
 */
public class ROEFixer {

    private static AbstractModel model;

    private final int pageSize = 100;

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public boolean fixData(final WritableVersion version, final int page) throws ConstraintException,
        AccessException {

        System.out.println("ROEFixer page [" + page + "]");
        final Paging pageing = new Paging(page * this.pageSize, this.pageSize);
        final Collection<ResearchObjective> ros = version.getAll(ResearchObjective.class, pageing);
        if (ros.isEmpty()) {
            return false;
        }

        int i = 1;
        for (final ResearchObjective ro : ros) {

            System.out.println("Processing ResearchObjective [" + i + "/" + ros.size() + ":" + ro.getName()
                + "]");
            final ConstructBean cb = ConstructBeanReader.readConstruct(ro);

            for (final ResearchObjectiveElement roe : ro.getBlueprintComponents()) {

                if (roe.getComponentType().equals("OpticConstruct")) {

                    if (null == cb.getExpressedProt() || "null".equals(cb.getExpressedProt())) {
                        // Create and apply the Molecule representing dnaSeq (expressed protein)
                        roe.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, "expressedProt",
                            cb.getProtSeq(), "protein", cb));
                    }

                    if (null == cb.getFinalProt()) {
                        // Create and apply the Molecule representing dnaSeq (final protein)
                        roe.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, "finalProt", cb
                            .getProtSeq(), "protein", cb));
                    }
                }
                i++;
            }

        }
        return true;
        //return false;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Create Expressed Protein and Final Protein sequences for all OpticConstructs";
    }

    public static void main(final String[] args) {

        ROEFixer.model = ModelImpl.getModel();

        final ROEFixer fixer = new ROEFixer();

        boolean hasMore = true;
        int page = 0;

        while (hasMore) {

            final WritableVersion wv =
                ROEFixer.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

            try {
                hasMore = fixer.fixData(wv, page);
                wv.commit();

            } catch (final AbortedException e) {
                e.printStackTrace();
                System.out.println(fixer.getClass().getSimpleName() + " failed due to "
                    + e.getLocalizedMessage());

            } catch (final AccessException e) {
                e.printStackTrace();
                System.out.println(fixer.getClass().getSimpleName() + " failed due to "
                    + e.getLocalizedMessage());

            } catch (final ConstraintException e) {
                e.printStackTrace();
                System.out.println(fixer.getClass().getSimpleName() + " failed due to "
                    + e.getLocalizedMessage());

            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
            page++;
        }

        System.out.println(fixer.getClass().getSimpleName() + " has finished!");
    }

}
