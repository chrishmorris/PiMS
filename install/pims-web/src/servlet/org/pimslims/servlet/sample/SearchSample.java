/**
 * pims-web org.pimslims.servlet Search.java
 * 
 * @author Marc Savitsky
 * @date 14 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.Conditions;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.SearchFilter;

/**
 * Search
 * 
 */

public class SearchSample extends QuickSearch {

    private static final long serialVersionUID = -2823117367106408486L;

    /**
     * SearchSample.getMetaClassName
     * 
     * @see org.pimslims.servlet.QuickSearch#getMetaClassName(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected String getMetaClassName(final HttpServletRequest request) {
        return Sample.class.getName();
    }

    /**
     * SearchSample.setSpecialBeans
     * 
     * @see org.pimslims.servlet.QuickSearch#setSpecialBeans(javax.servlet.http.HttpServletRequest,
     *      org.pimslims.metamodel.MetaClass, org.pimslims.dao.ReadableVersion)
     */
    @Override
    protected void setSpecialBeans(final HttpServletRequest request, final MetaClass metaClass,
        final ReadableVersion version) throws ServletException {

        final List<String> sampleCategoryList = new ArrayList();
        final Collection<SampleCategory> scs = super.getAll(version, SampleCategory.class);
        for (final Iterator iterator = scs.iterator(); iterator.hasNext();) {
            final SampleCategory sc = (SampleCategory) iterator.next();
            sampleCategoryList.add(sc.getName());
        }
        Collections.sort(sampleCategoryList);
        request.setAttribute("sampleCategoryList", sampleCategoryList);

        if (metaClass.getName().equals(Sample.class.getName())) {
            // request.setAttribute("userPersons",                 PersonUtility.getHookAndName(PersonUtility.getUserPersons(version)));

            final String dbid =
                request.getParameter("org.pimslims.model.sample.Sample:" + Sample.PROP_ASSIGNTO);
            if (null != dbid) { // probably obsolete
                request.setAttribute("personAssignedTo", this.getSelectedPerson(dbid, version));
                // TODO why isn't this just Sample.PROP_ASSIGNTO and a ModeObjectShortBean
            }
        }
    }

    private Map getSelectedPerson(final String userHook, final ReadableVersion version) {
        // Get person selected
        final Map hookPerson = new HashMap();

        final User person = version.get(userHook);
        if (person != null) {
            hookPerson.put("hook", person.get_Hook());
            hookPerson.put("name", person.getName());
        }

        return hookPerson;
    }

    /**
     * SearchSample.doGetCriteria
     * 
     * @see org.pimslims.servlet.QuickSearch#doGetCriteria(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.metamodel.MetaClass, java.util.Map)
     */
    @Override
    protected SearchFilter doGetCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map parameterMap) {
        final SearchFilter criteria = super.doGetCriteria(version, metaClass, parameterMap);
        SearchSample.updateCriteriaForInputsForGroup(version, metaClass, criteria);
        if (parameterMap.containsKey("isInPopup")) {
            criteria.put(AbstractSample.PROP_ISACTIVE, true);
        }
        return criteria;
    }

    /**
     * OldSearch.updateCriteriaForInputsForGroup
     * 
     * @param version
     * @param metaClass
     * @param criteria
     */
    public static void updateCriteriaForInputsForGroup(final ReadableVersion version,
        final MetaClass metaClass, final SearchFilter criteria) {

        // PiMS 1038 exclude samples that are output samples from this experimentGroup
        final String experimentGroupHook = (String) criteria.get("experimentGroup");
        ExperimentGroup group = null;

        // note that even with this complicated condition, Searcher generates a single select
        if (null != experimentGroupHook) {
            group = version.get(experimentGroupHook);

            if (null != group) {
                final Map<String, Object> expCriterials =
                    Conditions.newMap(Experiment.PROP_EXPERIMENTGROUP,
                        Conditions.or(Conditions.isNull(), Conditions.notEquals(group)));

                final Map<String, Object> osCriterials =
                    Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);

                criteria.put(Sample.PROP_OUTPUTSAMPLE, osCriterials);
            }
        }

    }
}
