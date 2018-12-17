package org.pimslims.presentation.leeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderCategory;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         HolderManager
 * 
 */
public class HolderManager {
    Holder holder = null;

    public HolderManager(final Holder holder) {
        this.holder = holder;
    }

    public String getName() {
        return this.holder.getName();
    }

    public Holder getHolder() {
        return this.holder;
    }

    public String getType() {
        final AbstractHolderType ht = this.holder.getHolderType();
        if (ht != null) {
            return ht.getName();
        }
        return null;
    }

    public String getCategory() {
        final Collection<HolderCategory> hts = this.holder.getHolderCategories();
        if (hts != null && !hts.isEmpty()) {
            return hts.iterator().next().getName();
        }
        return null;
    }

    public void setBarcode(final String barcode) throws ConstraintException {
        if (!Util.isEmpty(barcode)) {
            this.holder.setDetails(barcode);
        }
    }

    public String getBarcode() {
        return this.holder.getDetails();
    }

    public static Collection<HolderManager> getHolders(final ReadableVersion rv) {
        final Map prop = Util.getNewMap();
        final ArrayList cats = new ArrayList();
        cats.add("box");
        cats.add("card");
        // TODO does not seem to work as return plates as well - check!
        prop.put(AbstractHolder.PROP_HOLDERCATEGORIES, cats);
        final Collection<Holder> boxes = rv.findAll(Holder.class, prop);

        final ArrayList<HolderManager> holderM = new ArrayList<HolderManager>();
        for (final Holder h : boxes) {
            holderM.add(new HolderManager(h));
        }
        Collections.sort(holderM, new Comparator<HolderManager>() {
            public int compare(final HolderManager o1, final HolderManager o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return holderM;
    }

    public boolean getMayUpdate() {
        return this.holder.get_MayUpdate();
    }

}
