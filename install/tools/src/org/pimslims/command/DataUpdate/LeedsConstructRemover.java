/**
 * V2_2-pims-web org.pimslims.command.DataUpdate LeedsConstructRemover.java
 * 
 * @author Petr Troshin
 * @date August 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2007 Petr Troshin * *
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;
import java.util.LinkedList;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.sample.Sample;

/**
 * This is specific update for MPSI database therefore should not be in the DataFixerList
 * LeedsConstructRemover
 * 
 */
public class LeedsConstructRemover {

    /*
     * List of deleted constructs with barcodes 
     */
    static final String[] constructs =
        new String[] { "pMPSIL0288D\r\n", "1004202108\r\n", "pMPSIL0345D\r\n", "1004202123\r\n",
            "pMPSIL0289D\r\n", "1004202132\r\n", "pMPSIL0341C\r\n", "1004202159\r\n", "pMPSIL0341D1\r\n",
            "1004202168\r\n", "pMPSIL0294C\r\n", "1004202095\r\n", "pMPSIL0294D1\r\n", "1004202112\r\n",
            "pMPSIL0038C\r\n", "1004202143\r\n", "pMPSIL0038D1\r\n", "1004202160\r\n", "pMPSIL0150C\r\n",
            "1004202089\r\n", "pMPSIL0150D1\r\n", "1004202094\r\n", "pMPSIL0279C\r\n", "1004202118\r\n",
            "pMPSIL0279D1\r\n", "1004202137\r\n", "pMPSIL0040C\r\n", "1004202161\r\n", "pMPSIL0040D1\r\n",
            "1004202166\r\n", "pMPSIL0044C\r\n", "1004202093\r\n", "pMPSIL0044D1\r\n", "1004201299\r\n",
            "pMPSIL0281C\r\n", "1004202138\r\n", "pMPSIL0281D1\r\n", "1004202141\r\n", "pMPSIL0224C\r\n",
            "1004202165\r\n", "pMPSIL0224D1\r\n", "1004202091\r\n", "pMPSIL0067C\r\n", "1004202115\r\n",
            "pMPSIL0067D1\r\n", "1004202116\r\n", "pMPSIL0119C\r\n", "1004202140\r\n", "pMPSIL0119D1\r\n",
            "1004202163\r\n", "pMPSIL0001D1\r\n", "1004201304\r\n", "pMPSIL0039D1\r\n", "1004202343\r\n",
            "pMPSIL0282C\r\n", "1004202367\r\n", "pMPSIL0300C\r\n", "1004202297\r\n", "pMPSIL0284D1\r\n",
            "1004202318\r\n", "pMPSIL0338D1\r\n", "1004201279\r\n", "pMPSIL0118C\r\n", "1004202274\r\n",
            "pMPSIL0278D1\r\n", "1004202317\r\n", "pMPSIL0346C\r\n", "1004202323\r\n", "pMPSIL0347C\r\n",
            "1004202340\r\n", "pMPSIL0348C\r\n", "1004202347\r\n", "pMPSIL0349C\r\n", "1004201282\r\n",
            "pMPSIL0350C\r\n", "1004202276\r\n", "pMPSIL0351C\r\n", "1004202291\r\n", "pMPSIL0352C\r\n",
            "1004201283\r\n", "pMPSIL0353C\r\n", "1004202315\r\n", "pMPSIL0346D1\r\n", "1004202325\r\n",
            "pMPSIL0347D1\r\n", "1004202338\r\n", "pMPSIL0348D1\r\n", "1004202349\r\n", "pMPSIL0349D1\r\n",
            "1004202362\r\n", "pMPSIL0350D1\r\n", "1004202278\r\n", "pMPSIL0351D1\r\n", "1004202289\r\n",
            "pMPSIL0352D1\r\n", "1004202302\r\n", "pMPSIL0353D1\r\n", "1004202313\r\n", "pMPSIL0174C\r\n",
            "1004202337\r\n", "pMPSIL0343D\r\n", "1004202350\r\n", "pMPSIL0343C\r\n", "1004202361\r\n",
            "pMPSIL305D\r\n", "1004202279\r\n", "pMPSIL305C\r\n", "1004202288\r\n", "pMPSIL0312D\r\n",
            "1004202303\r\n", "pMPSIL0312C\r\n", "1004202312\r\n", "pMPSIL0307D\r\n", "1004202327\r\n",
            "pMPSIL0307C\r\n", "1004202336\r\n", "pMPSIL0310D\r\n", "1004202351\r\n", "pMPSIL0310C\r\n",
            "1004202360\r\n", "pMPSIL0304D\r\n", "1004202280\r\n", "pMPSIL0304C\r\n", "1004202287\r\n",
            "pMPSIL0309C\r\n", "1004202304\r\n", "pMPSIL0001C\r\n", "1004202328\r\n", "pMPSIL0039C\r\n",
            "1004202335\r\n", "pMPSIL0071C\r\n", "1004202359\r\n", "pMPSIL0118D1\r\n", "1004202281\r\n",
            "pMPSIL0151C\r\n", "1004202286\r\n", "pMPSIL0151D1\r\n", "1004202305\r\n", "pMPSIL0222C\r\n",
            "1004202310\r\n", "pMPSIL0278C\r\n", "1004202334\r\n", "pMPSIL0280C\r\n", "1004202353\r\n",
            "pMPSIL0282D1\r\n", "1004202358\r\n", "pMPSIL0283C\r\n", "1004201292\r\n", "pMPSIL0300D1\r\n",
            "1004202306\r\n", "pMPSIL0306C\r\n", "1004202309\r\n", "pMPSIL0444C\r\n", "1004202330\r\n",
            "pMPSIL0444C\r\n", "1004202333\r\n", "pMPSIL0446D1\r\n", "1004202357\r\n", "pMPSIL0037D1\r\n",
            "1004199584\r\n", "pMPSIL0037C\r\n", "1004198727\r\n", "pMPSIL0045D1\r\n", "1004198728\r\n",
            "pMPSIL0045C\r\n", "1004199631\r\n", "pMPSIL0071D1\r\n", "1004199632\r\n", "pMPSIL0168C\r\n",
            "1004199655\r\n", "pMPSIL0169D\r\n", "1004199656\r\n", "pMPSIL0170D\r\n", "1004199679\r\n",
            "pMPSIL0169C\r\n", "1004199585\r\n", "pMPSIL0170C\r\n", "1004199606\r\n", "pMPSIL0175C\r\n",
            "1004199609\r\n", "pMPSIL0177C\r\n", "1004199630\r\n", "pMPSIL0288C\r\n", "1004199633\r\n",
            "pMPSIL0283D\r\n", "1004199587\r\n", "pMPSIL0222D\r\n", "1004198740\r\n", "pMPSIL0227D\r\n",
            "1004198736\r\n", "pMPSIL0227C\r\n", "1004199628\r\n", "pMPSIL0289C\r\n", "1004199635\r\n",
            "pMPSIL0301C\r\n", "1004199652\r\n", "pMPSIL0301D\r\n", "1004199659\r\n", "pMPSIL0306E\r\n",
            "1004199676\r\n", "pMPSIL0306D1\r\n", "1004199588\r\n", "pMPSIL0338C\r\n", "1004199603\r\n",
            "pMPSIL0342C\r\n", "1004198737\r\n", "pMPSIL0342D\r\n", "1004199627\r\n", "pMPSIL0345C\r\n",
            "1004199636\r\n", "pMPSIL0191C\r\n", "1004201616\r\n", "pMPSIL0191D1\r\n", "1004201631\r\n",
            "pMPSIL0196C\r\n", "1004201655\r\n", "pMPSIL0196D1\r\n", "1004201664\r\n", "pMPSIL0197C\r\n",
            "1004201688\r\n", "pMPSIL0197D1\r\n", "1004201608\r\n", "pMPSIL0198C\r\n", "1004201632\r\n",
            "pMPSIL0198D1\r\n", "1004201639\r\n", "pMPSIL0079C\r\n", "1004201635\r\n", "pMPSIL0079D1\r\n",
            "1004201636\r\n" };

    public static void main(final String[] args) throws AccessException, ConstraintException,
        AbortedException {

        final AbstractModel model = ModelImpl.getModel();

        final WritableVersion wv = model.getWritableVersion(Access.ADMINISTRATOR);

        System.out.println("Remove Leeds Samples Data");

        final Collection<Sample> samples = wv.getAll(Sample.class, 0, Integer.MAX_VALUE);
        final LinkedList<Sample> sampleList = new LinkedList<Sample>(samples);

        try {
            for (final String constructName : LeedsConstructRemover.constructs) {
                for (final Sample s : samples) {
                    if (s.getName().startsWith(constructName.trim())) {
                        if (s.getOutputSample() == null) {
                            System.out.println("Deleting... " + s);
                            sampleList.remove(s);
                            s.delete();
                        }
                    }
                }
            }

            for (final String constructName : LeedsConstructRemover.constructs) {
                for (final Sample s : sampleList) {
                    if (s.getBatchNum().equals(constructName.trim())) {
                        if (s.getOutputSample() == null) {
                            System.out.println("Found by barcode Deleting " + s);
                            s.delete();
                        }
                    }
                }
            }

            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        System.out.println("Leeds sample remover has finished!");
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Remove samples from deleted constructs";
    }

}
