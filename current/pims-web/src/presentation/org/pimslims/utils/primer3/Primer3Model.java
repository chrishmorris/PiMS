package org.pimslims.utils.primer3;

public class Primer3Model {

    private String primerSequenceID;

    private String sequence;

    private String includedRegion;

    private String primerProductSizeRange;

    private final char linefeedchar = 10;

    public Primer3Model() { // empty constructor
    }

    public final void setPrimerSequenceID(final String s) {
        this.primerSequenceID = s;
    }

    public final void setSequence(final String s) {
        this.sequence = s;
    }

    public final void setIncludedRegion(final String s) {
        this.includedRegion = s;
    }

    public final void setPrimerProductSizeRange(final String s) {
        this.primerProductSizeRange = s;
    }

    public final byte[] toBytes() {
        final StringBuffer sb = new StringBuffer();
        sb.append("PRIMER_SEQUENCE_ID=" + primerSequenceID + linefeedchar);
        sb.append("SEQUENCE=" + sequence + linefeedchar);
        sb.append("INCLUDED_REGION=" + includedRegion + linefeedchar);
        sb.append("PRIMER_PRODUCT_SIZE_RANGE=" + primerProductSizeRange + linefeedchar);
        sb.append("PRIMER_GC_CLAMP=1" + linefeedchar);
        sb.append("PRIMER_OPT_SIZE=18" + linefeedchar);
        sb.append("PRIMER_MIN_SIZE=15" + linefeedchar);
        sb.append("PRIMER_MAX_SIZE=36" + linefeedchar);
        sb.append("PRIMER_OPT_TM=70" + linefeedchar);
        sb.append("PRIMER_MIN_TM=65" + linefeedchar);
        sb.append("PRIMER_MAX_TM=75" + linefeedchar);
        sb.append("PRIMER_MAX_DIFF_TM=6" + linefeedchar);
        sb.append("PRIMER_SELF_END=10" + linefeedchar);
        sb.append("PRIMER_SELF_ANY=20" + linefeedchar);
        sb.append("PRIMER_MAX_POLY_X=5" + linefeedchar);
        sb.append("PRIMER_NUM_NS_ACCEPTED=3" + linefeedchar);
        sb.append("PRIMER_FILE_FLAG=0" + linefeedchar);
        sb.append("PRIMER_FIRST_BASE_INDEX=1" + linefeedchar);
        sb.append("PRIMER_NUM_RETURN=1" + linefeedchar);
        sb.append("PRIMER_PICK_INTERNAL_OLIGO=0" + linefeedchar);
        sb.append("PRIMER_EXPLAIN_FLAG=1" + linefeedchar);
        return sb.toString().getBytes();
    }

}
