/**
 * 
 */
package uk.ac.sspf.spot.beans;

/**
 * Bean to represent a fasta sequence
 * 
 * @author Jon Diprose
 */
@Deprecated
public class ConstructFastaBean {

    private String description = ">";

    private String sequence = "";

    /**
     * Zero-arg constructor
     * 
     */
    public ConstructFastaBean() { /* empty */
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {

        // If description is null, map it to an empty description line
        if (description == null) {
            description = ">";
        } else if (!">".equals(description.substring(0, 1))) {
            description = ">" + description;
        }

        // Store the description
        this.description = description;

    }

    /**
     * @return Returns the sequence.
     */
    public String getSequence() {
        return this.sequence;
    }

    /**
     * @param sequence The sequence to set.
     */
    public void setSequence(String sequence) {

        // If description is null, map it to an empty description line
        if (sequence == null) {
            sequence = "";
        }

        // Store the description
        this.sequence = sequence;

    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(this.getDescription());
        sb.append("\n");

        final byte[] bytes = this.getSequence().getBytes();
        int c = 0;
        for (int i = 0; i < bytes.length; i++) {
            sb.append((char) bytes[i]);
            c++;
            if (c == 60) {
                sb.append("\n");
                c = 0;
            }
        }
        //sb.append(this.getSequence());
        return sb.toString();
    }

}
