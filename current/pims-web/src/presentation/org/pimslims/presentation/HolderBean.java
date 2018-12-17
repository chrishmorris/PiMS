package org.pimslims.presentation;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.holder.Holder;

public class HolderBean extends ModelObjectBean {

    private Integer freeRow = null;

    private Integer freeColumn = null;

    // see link.tag
    private final boolean isPlate;

    public HolderBean(final Holder holder) {
        super(holder);
        this.isPlate = HolderFactory.isPlate(holder.getHolderType());
        final int[] gap = HolderDAO.findFirstGap(holder);
        if (null != gap) {
            this.freeColumn = gap[0];
            this.freeRow = gap[1];
        }
    }

    /**
     * HolderBean.compareTo
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object arg0) {
        final HolderBean holder = (HolderBean) arg0;

        if (!this.getValues().get("colPosition").equals(holder.getValues().get("colPosition"))) {
            final String s0 = (String) this.getValues().get("colPosition");
            final String s1 = (String) holder.getValues().get("colPosition");
            return s0.compareToIgnoreCase(s1);
        }
        if (!this.getValues().get("rowPosition").equals(holder.getValues().get("rowPosition"))) {
            final String s0 = (String) this.getValues().get("rowPosition");
            final String s1 = (String) holder.getValues().get("rowPosition");
            return s0.compareToIgnoreCase(s1);
        }

        final String s0 = (String) this.getValues().get("subPosition");
        final String s1 = (String) holder.getValues().get("subPosition");
        return s0.compareToIgnoreCase(s1);
    }

    /**
     * HolderBean.getIsPlate
     * 
     * @see org.pimslims.presentation.ModelObjectShortBean#getIsPlate()
     */
    public boolean getIsPlate() {
        return this.isPlate;
    }

    public Integer getFreeRow() {
        return this.freeRow;
    }

    public Integer getFreeColumn() {
        return this.freeColumn;
    }

}
