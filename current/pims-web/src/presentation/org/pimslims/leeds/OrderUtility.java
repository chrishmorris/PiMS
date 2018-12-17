package org.pimslims.leeds;

/**
 * @author Bill
 * 
 */
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.holder.Holder;

public class OrderUtility {

    /* public static boolean isPlateOrder(final Holder holder) {
         if (HolderFactory.getExperimentType(holder) == null) {
             return false;
         }
         if (OrderUtility.isLeedsPrimerOrder(holder)) {
             return true;
         }
         * Don't link to Leeds pages for non-Leeds orders
          * TODO unify the model. At present, non-Leeds orders have a virtual output sample.
          * if (HolderFactory.getExperimentType(holder).getName().equalsIgnoreCase("Order")
             && HolderFactory.getProtocol(holder).getName().contains("Order")) {
             return true;
         } *
         // else
         return false;

     }*/

    public static boolean isLeedsPrimerOrder(final Holder holder) {
        if (HolderFactory.getExperimentType(holder) == null) {
            return false;
        }
        if (HolderFactory.getExperimentType(holder).getName().equalsIgnoreCase(FormFieldsNames.primersDesign)
            && HolderFactory.getExperimentGroup(holder) != null) {
            return true;
        }
        // else
        return false;

    }
}
