package org.pimslims.presentation.mru;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pimslims.presentation.ModelObjectShortBean;

public class MRUList {

    // note that Vector is synchronized
    private final Vector<MRU> vList = new Vector<MRU>();

    public MRUList() {
        super();

    }

    /**
     * add a mru into list
     * 
     * @param mru
     */
    public void add(final MRU mru) {
        if (mru == null) {
            return;
        }
        this.removeOldMRU(mru);
        this.vList.add(0, mru);
        this.removeOversizeMRU(mru);
    }

    /**
     * remove a mru by hook
     */
    public void removeMRUByHook(final String hook) {
        for (final ModelObjectShortBean mru : new Vector<MRU>(this.vList)) {
            if (mru.getHook().equals(hook)) {
                this.vList.remove(mru);
                break;
            }

        }
    }

    /**
     * remove the old mru which is oversize in totoal or by class' limit
     */
    private void removeOversizeMRU(final ModelObjectShortBean mru) {
        // over class' limit
        final List<MRU> mruWithSameClassName = this.getMRUs(mru.getClassName());
        if (mruWithSameClassName.size() > MRUController.getMaxSizeOfEachClass()) {
            this.removeOldMRU(mruWithSameClassName.get(mruWithSameClassName.size() - 1));
        }
        // oversize in totoal
        if (this.vList.size() > MRUController.getMaxSize()) {
            this.vList.setSize(MRUController.getMaxSize());
        }
    }

    /**
     * remove the old mru which has same hook
     * 
     * @param mru
     */
    private void removeOldMRU(final ModelObjectShortBean mru) {
        for (final ModelObjectShortBean oldMRU : new ArrayList<MRU>(this.vList)) {
            if (oldMRU.getHook().equals(mru.getHook())) {
                this.vList.remove(oldMRU);
            }
        }

    }

    public List<MRU> getMRUs() {
        return new ArrayList<MRU>(this.vList);
    }

    public List<MRU> getMRUs(final String classFullName) {
        final ArrayList<MRU> mruByClassName = new ArrayList<MRU>();
        for (final MRU mru : this.vList) {
            if (mru.getClassName().equalsIgnoreCase(classFullName)) {
                mruByClassName.add(mru);
            }
        }
        return mruByClassName;
    }

}
