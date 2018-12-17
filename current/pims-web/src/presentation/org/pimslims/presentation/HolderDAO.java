/**
 * pims-web org.pimslims.presentation HolderDAO.java
 * 
 * @author Marc Savitsky
 * @date 8 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;

/**
 * HolderDAO
 * 
 */
public class HolderDAO {

    //TODO find first free location in holders

    /**
     * 
     * HolderDAO.findHoldersNotPlates
     * 
     * @param version
     * @param criteria
     * @return
     */
    @Deprecated
    // seems not to be used
    private static Collection<HolderBean> findHoldersNotPlates(final ReadableVersion version,
        final Map<String, Object> criteria) {

        assert criteria != null;
        // get expGroups
        Collection<Holder> holders = Collections.EMPTY_LIST;
        if (criteria.isEmpty()) {
            holders = HolderDAO.getHolders(version);
        } else {
            holders = HolderDAO.getHolders(version, criteria);
        }

        // get beans
        final List<HolderBean> holderbeans = new LinkedList<HolderBean>();
        for (final Holder holder : holders) {
            holderbeans.add(new HolderBean(holder));
        }

        return holderbeans;
    }

    /**
     * 
     * HolderDAO.getHolders
     * 
     * @param version
     * @return
     */
    public static Collection<Holder> getHolders(final ReadableVersion version) {

        final String selectHQL =
            " select distinct holder" + " from Holder as holder " + "left join holder.samples as sample "
                + " left join sample.outputSample as os " + " left join os.experiment as experiment "
                + " where experiment.experimentGroup  is null";
        final PimsQuery query = PimsQuery.getQuery(version, selectHQL, Holder.class);

        return query.list();
    }

    /**
     * 
     * HolderDAO.getHolders
     * 
     * @param version
     * @param criteria
     * @return
     */
    public static Collection<Holder> getHolders(final ReadableVersion version,
        final Map<String, Object> criteria) {

        String selectHQL = null;
        final PimsQuery query;

        if (criteria.containsKey(AbstractHolder.PROP_SUPHOLDER)
            && null != criteria.get(AbstractHolder.PROP_SUPHOLDER)) {

            selectHQL =
                " select distinct holder" + " from Holder as holder " + "left join holder.samples as sample "
                    + " left join sample.outputSample as os " + " left join os.experiment as experiment "
                    + " where holder.parentHolder=:supHolder" + " and experiment.experimentGroup  is null";

            query = PimsQuery.getQuery(version, selectHQL, Holder.class);
            query.setEntity("supHolder", criteria.get(AbstractHolder.PROP_SUPHOLDER));

        } else {

            selectHQL =
                " select distinct holder" + " from Holder as holder " + "left join holder.samples as sample "
                    + " left join sample.outputSample as os " + " left join os.experiment as experiment "
                    + " where holder.parentHolder is null" + " and experiment.experimentGroup is null";

            query = PimsQuery.getQuery(version, selectHQL, Holder.class);
        }

        return query.list();
    }

    /**
     * HolderDAO.findRowWithGaps
     * 
     * @param holder
     * @return row number of first row with empty space (numbering from one) or null if the holder is full
     */
    public static int[] findFirstGap(final Holder holder) {
        final HolderType type = (HolderType) holder.getHolderType();
        if (null == type) {
            return null;
        }
        Integer maxColumn = type.getMaxColumn();
        if (null==maxColumn) {maxColumn = 1;}
        if ( null == type.getMaxRow()) {
            return null;
        }

        final boolean[][] occupied = new boolean[maxColumn][type.getMaxRow()];
        final Set<Sample> samples = holder.getSamples();
        for (final Iterator iterator = samples.iterator(); iterator.hasNext();) {
            final Sample sample = (Sample) iterator.next();
            if (null == sample.getColPosition() || maxColumn < sample.getColPosition()) {
                continue;
            }
            if (null == sample.getRowPosition() || type.getMaxRow() < sample.getRowPosition()) {
                continue;
            }
            occupied[sample.getColPosition() - 1][sample.getRowPosition() - 1] = true;
        }
        for (int row = 1; row <= occupied[0].length; row++) {
            for (int column = 1; column <= occupied.length; column++) {
                if (!occupied[column - 1][row - 1]) {
                    return new int[] { column, row };
                }
            }
        }
        return null;
    }
}
