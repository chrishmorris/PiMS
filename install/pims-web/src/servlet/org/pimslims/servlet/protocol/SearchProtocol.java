/**
 * 
 */
package org.pimslims.servlet.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.search.Condition;
import org.pimslims.search.Conditions;
import org.pimslims.search.conditions.OrCondition;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.SearchFilter;
import org.pimslims.servlet.experiment.CreateExperiment;

/**
 * @author cm65
 * 
 */

public class SearchProtocol extends QuickSearch {

    /*
     * (non-Javadoc)
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show custom list of protocols";
    }

    /**
     * SearchProtocol.getMetaClassName
     * 
     * @see org.pimslims.servlet.QuickSearch#getMetaClassName(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected String getMetaClassName(final HttpServletRequest request) {
        return Protocol.class.getName();
    }

    /**
     * SearchProtocol.setSpecialBeans
     * 
     * @see org.pimslims.servlet.QuickSearch#setSpecialBeans(javax.servlet.http.HttpServletRequest,
     *      org.pimslims.metamodel.MetaClass, org.pimslims.dao.ReadableVersion)
     */
    @Override
    protected void setSpecialBeans(final HttpServletRequest request, final MetaClass metaClass,
        final ReadableVersion version) throws ServletException {

        SearchProtocol.exportSampleCategoryList(request, version);

        request.setAttribute("experimentTypes",
            ModelObjectShortBean.getModelObjectShortBeans(CreateExperiment.activeExperimentTypes(version)));
        request.setAttribute("instrumentTypes", ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet
            .getAll(version, org.pimslims.model.reference.InstrumentType.class)));
        super.setSpecialBeans(request, metaClass, version);
    }

    /**
     * SearchProtocol.exportSampleCategoryList
     * 
     * @param request
     * @param version
     * @throws ServletException
     */
    public static void exportSampleCategoryList(final HttpServletRequest request,
        final ReadableVersion version) throws ServletException {
        final List<String> sampleCategoryList = new ArrayList();
        final Collection<SampleCategory> scs = PIMSServlet.getAll(version, SampleCategory.class);
        for (final Iterator iterator = scs.iterator(); iterator.hasNext();) {
            final SampleCategory sc = (SampleCategory) iterator.next();
            sampleCategoryList.add(sc.getName());
        }
        Collections.sort(sampleCategoryList);
        request.setAttribute("sampleCategoryList", sampleCategoryList);
    }

    // TODO generalise this to support other conditions on joined tables
    public static final String PRODUCT = "_join:refoutputSamples.sampleCategory.name";

    /**
     * SearchProtocol.doGetCriteria
     * 
     * @see org.pimslims.servlet.QuickSearch#doGetCriteria(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.metamodel.MetaClass, java.util.Map)
     */
    @Override
    protected SearchFilter doGetCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map parameterMap) {
        final SearchFilter ret = super.doGetCriteria(version, metaClass, parameterMap);
        if (parameterMap.containsKey(SearchProtocol.PRODUCT)) {
            final String[] categories = (String[]) parameterMap.get(SearchProtocol.PRODUCT);
            final Collection<Condition> names = new HashSet(categories.length);
            for (int i = 0; i < categories.length; i++) {
                names.add(Conditions.eq(categories[i]));
            }
            final Map<String, Object> categoryCriteria = new HashMap();
            categoryCriteria.put(SampleCategory.PROP_NAME, new OrCondition(names));
            final Map<String, Object> rosCriteria = new HashMap();
            rosCriteria.put(RefOutputSample.PROP_SAMPLECATEGORY, categoryCriteria);
            ret.put(Protocol.PROP_REFOUTPUTSAMPLES, rosCriteria);
        }

        return ret;
    }
}
