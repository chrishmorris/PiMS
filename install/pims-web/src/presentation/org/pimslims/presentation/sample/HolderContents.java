/**
 * pims-web org.pimslims.presentation.sample HolderContents.java
 * 
 * @author cm65
 * @date 28 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;

/**
 * HolderContents
 * 
 */
public class HolderContents {

    /**
     * HolderContents.getHolderContents TODO the finds may be slow, it might be better to all the holders and
     * samples first
     * 
     * @param holder a 3D holder
     * @return a list of lists of lists of beans, representing the contents
     */
    public static List<List<List<ModelObjectBean>>> getHolderContents(final Holder holder) {
        final HolderType type = (HolderType) holder.getHolderType();
        if (null == type) {
            throw new IllegalStateException("Unknown type for holder: " + holder.getName());
        }
        final Integer rows = type.getMaxRow();
        final List<List<List<ModelObjectBean>>> ret = new ArrayList<List<List<ModelObjectBean>>>(rows);
        for (int row = 1; row <= rows; row++) {
            final List<List<ModelObjectBean>> rowContents = new ArrayList<List<ModelObjectBean>>();
            final Integer columns = type.getMaxColumn();
            for (int column = 1; column <= columns; column++) {
                final List columnContents = new ArrayList<List>();
                final Integer positions = type.getMaxSubPosition();
                for (int position = 1; position <= positions; position++) {
                    final Map criteria = new HashMap(3);
                    criteria.put(AbstractHolder.PROP_ROWPOSITION, row);
                    criteria.put(AbstractHolder.PROP_COLPOSITION, column);
                    criteria.put(AbstractHolder.PROP_SUBPOSITION, position);
                    ModelObject found = holder.findFirst(AbstractHolder.PROP_SUBHOLDERS, criteria);
                    if (null == found) {
                        criteria.put(Sample.PROP_ROWPOSITION, row);
                        criteria.put(Sample.PROP_COLPOSITION, column);
                        criteria.put(Sample.PROP_SUBPOSITION, position);
                        found = holder.findFirst(Holder.PROP_SAMPLES, criteria);
                        if (null == found) {
                            columnContents.add(null);
                            continue;
                        }
                    }
                    columnContents.add(BeanFactory.newBean(found));
                }
                rowContents.add(columnContents);
            }
            ret.add(rowContents);
        }
        return ret;
    }

    /**
     * HolderContents.getDimensions
     * 
     * @param holder
     * @return
     */
    public static int getDimensions(final Holder holder) {
        final AbstractHolderType type = holder.getHolderType();
        if (null == type) {
            return 0;
        }
        return HolderContents.getDimensions(type);
    }

    /**
     * HolderContents.getDimensions
     * 
     * @param type
     * @return 0,1,2, or 3
     */
    public static int getDimensions(final AbstractHolderType type) {
        final HolderType t = (HolderType) type;
        if (null == t.getMaxRow()) {
            return 0;
        }
        final int ret =
            HolderContents.isExtended(t.getMaxRow()) + HolderContents.isExtended(t.getMaxColumn())
                + HolderContents.isExtended(t.getMaxSubPosition());
        return (0 == ret) ? 1 : ret;
    }

    private static int isExtended(final Integer max) {
        return (null == max || 0 == max || 1 == max) ? 0 : 1;
    }

}
