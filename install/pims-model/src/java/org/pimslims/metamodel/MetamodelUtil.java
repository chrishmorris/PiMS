package org.pimslims.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author cm65 Code written by Peter Troshin for org.pimslims.servlet.utils.ServletUtil Moved to
 *         org.pimslims.metamodel to resolve circular dependency.
 * 
 */
public class MetamodelUtil {

    public static boolean isEmpty(Object o) {
        if (o instanceof Collection) {
            return ((Collection) o).size() == 0 ? true : false;
        }
        if (o instanceof String[]) {
            return ((String[]) o).length == 0 ? true : false;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) != null ? false : true;
        }
        if (o instanceof String) {
            return ((String) o).equalsIgnoreCase("") ? true : false;
        }
        return false;
    }

    /**
     * Method to find all mandatory roles of a Metaclass
     * 
     * @param metaRoles all metaRoles
     * @return mandatory roles names only (keys)
     */
    @SuppressWarnings("unchecked")
    public static ArrayList getMandatoryRoles(Map metaRoles) {
        ArrayList mandRoles = new ArrayList();
        for (Iterator iter = metaRoles.entrySet().iterator(); iter.hasNext();) {
            Map.Entry elem = (Map.Entry) iter.next();
            MetaRole role = (MetaRole) elem.getValue();
            if (role.getLow() >= 1) {
                mandRoles.add(elem.getKey());
            }
        }
        return mandRoles;
    }

    /**
     * Sort attributes into alphabetical order of name. Note that this may not be the right order for the UI,
     * e.g Experiment has startDate and endDate.
     * 
     * @param attributes map of name => MetaAttribute
     * @return a list of the attributes, in name order
     */
    @SuppressWarnings("unchecked")
    public static ArrayList getSortedAttributes(Map attributes) {
        ArrayList sortedAttributes = new ArrayList();
        Map.Entry[] sortedmap = sortMap(attributes);
        for (Entry elem : sortedmap) {
            sortedAttributes.add(elem.getValue());
        }
        return sortedAttributes;
    }

    /**
     * Filter a list of attributes to return the mandatory ones
     * 
     * @param sortedAttributes
     * @return list of mandatory attributes
     */
    public static ArrayList<MetaAttribute> getMandatoryAttr(ArrayList sortedAttributes) {
        ArrayList<MetaAttribute> mandatoryAttributes = new ArrayList<MetaAttribute>();
        for (Iterator iter = sortedAttributes.iterator(); iter.hasNext();) {
            MetaAttribute attribute = (MetaAttribute) iter.next();
            if (attribute.isRequired() && !attribute.isDerived()) {
                mandatoryAttributes.add(attribute);
            } else {
                break;
            }
        }
        return mandatoryAttributes;
    }

    /**
     * This method will use Arrays.sort for sorting Map
     * 
     * @param map
     * @return outputList of Map.Entries
     */
    @SuppressWarnings("unchecked")
    public static Map.Entry[] sortMap(Map map) {
        int count = 0;
        Set set = null;
        Map.Entry[] entries = null;
        // Logic:
        // get a set from Map
        // Build a Map.Entry[] from set
        // Sort the list using Arrays.sort
        // Add the sorted Map.Entries into arrayList and return

        set = map.entrySet();
        Iterator iterator = set.iterator();
        entries = new Map.Entry[set.size()];
        while (iterator.hasNext()) {
            entries[count++] = (Map.Entry) iterator.next();
        }

        // Sort the entries with your own comparator for the values:
        Arrays.sort(entries, new Comparator() {
            public int compareTo(Object lhs, Object rhs) {
                Map.Entry le = (Map.Entry) lhs;
                Map.Entry re = (Map.Entry) rhs;
                MetaAttribute leAttribute = (MetaAttribute) le.getValue();
                MetaAttribute reAttribute = (MetaAttribute) re.getValue();
                boolean leReq = leAttribute.isRequired();
                boolean reReq = reAttribute.isRequired();
                int result;
                if (leReq && reReq || !leReq && !reReq) {
                    result = 0;
                } else if (leReq && !reReq) {
                    result = -1;
                } else {
                    result = 1;
                }
                return result;
            }

            public int compare(Object lhs, Object rhs) {
                return compareTo(lhs, rhs);
            }
        });

        return entries;
    }// End of sortMap

}
