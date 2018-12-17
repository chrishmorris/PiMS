package org.pimslims.presentation.servlet.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;

public class RecordsFilter {

    /**
     * Filter search results by criteria e.g. Result = Collection of MolComponents filter criteria = Role
     * Category has attribute name equals "Protein"
     * 
     * @param results - Collection<ModelObject>
     * @param criteria - Map<MetaAttribute, String> where String is a value of metaAttribute
     * @param role MetaRole - The metaRole
     * @return Subset of objects match the criteria
     */
    public static Collection<ModelObject> filter(final Collection<ModelObject> results,
        final Map<MetaAttribute, String> criteria, final MetaRole role) {
        final ArrayList<ModelObject> filteredResults = new ArrayList<ModelObject>();
        for (final ModelObject mObj : results) {
            final MetaClass metaClass = mObj.get_MetaClass();
            assert metaClass.getMetaRoles().containsKey((role.getName())) : "No role " + role.getName()
                + " is defined for MetaClass " + metaClass.getMetaClassName();
            final Collection<ModelObject> roleObjs = mObj.get(role.getName());
            if (roleObjs == null) {
                continue;
            }
            if (RecordsFilter.match(roleObjs, criteria)) {
                // Keep this object;
                filteredResults.add(mObj);
            }
        }
        return filteredResults;
    }

    private static boolean match(final Collection<ModelObject> roleObjs,
        final Map<MetaAttribute, String> criteria) {
        for (final ModelObject mObj : roleObjs) {
            // Only one object is sufficient to meet criteria (!)
            if (RecordsFilter.containValue(mObj, criteria)) {
                return true;
            }
        }
        return false;
    }

    /*
     * This will only work correctly if criteria contain only one criteria (!)
     */
    private static boolean containValue(final ModelObject mObj, final Map<MetaAttribute, String> criteria) {
        for (final Map.Entry<MetaAttribute, String> entry : criteria.entrySet()) {
            final Object value = mObj.get_Value(entry.getKey().getName());
            if (value == null) {
                return false;
            }
            if (value instanceof Collection) {
                for (final Object single_value : (Collection) value) {
                    // If the value is a collection
                    // if one value from the collection is equal to the criteria
                    // this is sufficient
                    if (RecordsFilter.equal((String) single_value, entry.getValue())) {
                        return true;
                    }
                }
            } else {
                System.out.println(entry.getValue() + "  " + value);
                if (RecordsFilter.equal((String) value, entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean equal(final String value, final String criteria) {
        if (value.equals(criteria)) {
            return true;
        }
        return false;
    }

    /**
     * Filter search results by criteria e.g. Result = Collection of MolComponents filter criteria = attribute
     * MolType equals "Protein"
     * 
     * @param results - Collection<ModelObject>
     * @param criteria - Map<MetaAttribute, String> where String is a value of metaAttribute
     * @return Subset of objects match the criteria
     */
    public static Collection<ModelObject> filter(final Collection<ModelObject> results,
        final Map<MetaAttribute, String> criteria) {
        return null;
    }

    /**
     * Filter search results by criteria e.g. Result = Collection of MolComponents filter criteria = Role
     * Category has attribute name equals "Protein"
     * 
     * @param results - Collection<ModelObject>
     * @param criteria - Map<MetaAttribute, String> where String[] a collection of values of the metaAttribute
     * @return Subset of objects match the criteria
     */
    public static Collection<ModelObject> filterMultiple(final Collection<ModelObject> results,
        final Map<MetaAttribute, String[]> criteria) {
        return null;
    }

    /**
     * Filter search results by criteria e.g. Result = Collection of MolComponents filter criteria = Role
     * Category has attribute name equals "Protein"
     * 
     * @param results - Collection<ModelObject>
     * @param criteria - Map<MetaAttribute, String[]> where String[] a collection of values of the
     *            metaAttribute
     * @param role MetaRole
     * @return Subset of objects match the criteria
     */
    public static Collection<ModelObject> filterMultiple(final Collection<ModelObject> results,
        final Map<MetaAttribute, String[]> criteria, final MetaRole role) {
        return null;
    }

}
