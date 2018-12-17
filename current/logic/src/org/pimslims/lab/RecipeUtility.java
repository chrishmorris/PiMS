/**
 * pims-web org.pimslims.lab RecipeUtility.java
 * 
 * @author Marc Savitsky
 * @date 15 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

/**
 * RecipeUtility
 * 
 */
public class RecipeUtility {

    public static RefSample duplicate(final RefSample recipe, final WritableVersion rw)
        throws ConstraintException, AccessException {

        // This name will be reset later
        final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(RefSample.PROP_NAME, "aa" + System.currentTimeMillis());
        attributes.put(LabBookEntry.PROP_ACCESS, rw.getFirstProject());
        final RefSample dupl = new RefSample(rw, attributes);
        assert dupl != null;

        final Map<String, Object> duplParam = Utils.deleteNullValues(recipe.get_Values());
        // Protocol name mast be unique
        // Name is character varing must be < 80 characters
        //final Collection recipes = rw.getAll(org.pimslims.model.sample.RefSample.class);
		duplParam.put(RefSample.PROP_NAME,
				rw.getUniqueName(RefSample.class, recipe.get_Name()));
        dupl.set_Values(duplParam);

        //recipe.getConformings();

        dupl.setSampleComponents(new HashSet(RecipeUtility.duplicateSampleComponents(rw,
            recipe.getSampleComponents(), dupl)));
        dupl.setSampleCategories(recipe.getSampleCategories());
        dupl.setHazardPhrases(recipe.getHazardPhrases());
        dupl.setRefSampleSources(recipe.getRefSampleSources());

        return dupl;
    }

    /**
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static Collection<SampleComponent> duplicateSampleComponents(final WritableVersion wv,
        final Set<SampleComponent> objects, final RefSample dupl) throws AccessException, ConstraintException {

        final Collection<SampleComponent> components = new ArrayList<SampleComponent>(objects.size());
        for (final SampleComponent mobj : objects) {
            final Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put(SampleComponent.PROP_REFCOMPONENT,
					duplicate(mobj.getRefComponent()));
            attributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, mobj.getAbstractSample());
            attributes.put(LabBookEntry.PROP_ACCESS, wv.getFirstProject());
            final SampleComponent scomponent = new SampleComponent(wv, attributes);

            scomponent.set_Values(Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
            components.add(scomponent);
        }

        return components;
    }

	private static Object duplicate(Substance refComponent)
			throws AccessException, ConstraintException {
		return shallowCopy(refComponent);
	}

	public static ModelObject shallowCopy(LabBookEntry object)
			throws AccessException, ConstraintException {
		WritableVersion version = (WritableVersion) object.get_Version();
		Map<String, Object> values = object.get_Values();
		if (values.containsKey("name")) {
			values.put(
					"name",
					version.getUniqueName(object.getClass(),
							(String) values.get("name")));
		}
		return version.create(object.getClass(), values);
	}

}
