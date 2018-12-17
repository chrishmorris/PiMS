package org.pimslims.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.reference.SampleCategory;

/**
 * Represents a sample category in a list on a JSP page.
 * 
 * @see org.pimslims.model.reference.SampleCategory
 * @author Ed Daniel
 * 
 */
public class SampleCategoryBean implements Serializable, Comparable {

    private String hook;

    private String name;

    private static final int MAX_CATEGORIES = 200;

    public SampleCategoryBean(final String name, final String hook) {
        this.hook = hook;
        this.name = name;
    }

    /**
     * @return Returns the hook of teh related RefSample TODO simply return the hook of the RefSample after
     *         the DM change
     */
    public String getHook() {
        return this.hook;
    }

    public void setHook(final String hook) {
        this.hook = hook;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int compareTo(final Object otherSC) {
        if (!(otherSC instanceof SampleCategoryBean)) {
            throw new ClassCastException("A SampleCategoryBean object expected.");
        }
        final String otherSCName = ((SampleCategoryBean) otherSC).getName();
        return this.getName().compareTo(otherSCName);
    }

    //LATER maybe this should list only "head" locations, those whose PROP_LOCATION is null
    public static Collection<ModelObjectBean> getAllSampleCategories(final ReadableVersion version) {
        final Collection<SampleCategory> sampleCategories =
            version.getAll(SampleCategory.class, 0, SampleCategoryBean.MAX_CATEGORIES);
        if (SampleCategoryBean.MAX_CATEGORIES == sampleCategories.size()) {
            throw new IllegalStateException("You have too many sampleCategories");
        }
        final List<ModelObjectBean> ret = new ArrayList<ModelObjectBean>(sampleCategories.size());
        for (final SampleCategory sampleCategory : sampleCategories) {
            ret.add(new ModelObjectBean(sampleCategory));
        }
        Collections.sort(ret);
        return ret;
    }
}
