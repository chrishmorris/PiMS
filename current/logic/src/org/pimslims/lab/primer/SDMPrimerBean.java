/**
 * pims-web org.pimslims.lab.primer SDMPrimerBean.java
 * 
 * @author Marc Savitsky
 * @date 1 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.primer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;

/**
 * SDMPrimerBean
 * 
 */
public class SDMPrimerBean extends YorkPrimerBean {

    private String mutation;

    public void setMutatedSequence(final String mutation) {
        this.mutation = mutation;
    }

    public String getCodonsBefore() {
        return this.getPrimerSeq().substring(0, this.getPrimerSeq().indexOf(this.mutation));
    }

    public String getCodonsAfter() {
        return this.getPrimerSeq().substring(0,
            this.getPrimerSeq().indexOf(this.mutation) + this.mutation.length());
    }

    public String getMutated() {
        return this.mutation;
    }

    public static List<SDMPrimerBean> makePrimerBeanList(final float desTm, final String ptype,
        final String dnaSeq, final String wildDnaSeq) {
        String seq = "";
        String wild = "";
        if ("A".equals(ptype)) {
            try {
                SymbolList symL = DNATools.createDNA(dnaSeq);
                //symL = DNATools.reverseComplement(symL);
                symL = DNATools.complement(symL);
                seq = symL.seqString();

                symL = DNATools.createDNA(wildDnaSeq);
                //symL = DNATools.reverseComplement(symL);
                symL = DNATools.complement(symL);
                wild = symL.seqString();
            } catch (final IllegalSymbolException e) {
                throw new IllegalArgumentException(e);
            } catch (final IllegalAlphabetException e) {
                throw new IllegalArgumentException(e);
            }

        } else {
            seq = dnaSeq;
            wild = wildDnaSeq;
        }
        assert null != seq;

        final PimsSDMPrimerDesigner dP = PimsSDMPrimerDesigner.getSDMPrimerDesigner();
        final List<String> primers = dP.makePrimers(new String[] { seq, wild }, desTm);

        final List<SDMPrimerBean> sdms = new ArrayList<SDMPrimerBean>();
        final Iterator pit = primers.iterator();
        while (pit.hasNext()) {
            // create primerBean for each primer
            final SDMPrimerBean pb = new SDMPrimerBean();
            // setconstructId, primerSeq,F or R, Tm and length
            final String primer = pit.next().toString();
            pb.setPrimerSeq(primer);
            pb.setPrimerType(ptype);
            final float primTm =
                PimsSDMPrimerDesigner.calcTm(primer,
                    PimsSDMPrimerDesigner.countMutations(new String[] { seq, wild }));
            pb.setPrimerTm(primTm);
            pb.setPrimerLength(Integer.valueOf(primer.length()));
            pb.setMutatedSequence(PimsSDMPrimerDesigner.getMutation(new String[] { seq, wild }));
            sdms.add(pb);
            if ("A".equals(ptype)) {
                final String primerSeq = pb.getPrimerSeq();
                // reverse the sequence PIMS-3605
                pb.setPrimerSeq(new StringBuffer(primerSeq).reverse().toString());
            }
        }
        if (sdms.size() > 10) {
            return sdms.subList(0, 10);
        }
        return sdms;
    }

}
