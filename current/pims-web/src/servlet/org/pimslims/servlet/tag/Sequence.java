/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.pimslims.lab.Utils;

/**
 * @author Petr Troshin
 * 
 */
public class Sequence extends PIMSTag {

    /**
     * Information about the attribute to input
     */
    private String sequence = null;

    private int format = 0;

    private int escapeStyle = 0;

    // TODO remove private String space = " "; // 1 default space character

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        this.sequence = this.getFormatedSequence();
        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(this.sequence.trim());
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    /*
     * 
     */
    public void setSequence(final String sequence) {
        this.sequence = sequence.replace("<", "");
    }

    public void setEscapeStyle(final String escapeStyle) {
        if (escapeStyle.equals("TEXT")) {
            this.escapeStyle = Utils.StringFormatter.TEXT;
        }
        if (escapeStyle.equals("HTML")) {
            this.escapeStyle = Utils.StringFormatter.HTML;
        }
        if (escapeStyle.equals("NONE")) {
            this.escapeStyle = Utils.StringFormatter.NONE;
        }
    }

    public void setFormat(final String format) {
        if (format.equalsIgnoreCase("DEFAULT")) {
            this.format = Utils.StringFormatter.DEFAULT;
        }
        if (format.equalsIgnoreCase("FASTA")) {
            this.format = Utils.StringFormatter.FASTA;
        }
        if (format.equalsIgnoreCase("GENBANK")) {
            this.format = Utils.StringFormatter.GENBANK;
        }
    }

    private String getFormatedSequence() {
        String fseq = "";
        switch (this.format) {
            case Utils.StringFormatter.FASTA:
                fseq = this.getFormatedSequence(60, 1);
                break;
            case Utils.StringFormatter.GENBANK:
                fseq = this.getFormatedSequence(10, 6);
                break;
            default: // Let it leaking
            case Utils.StringFormatter.DEFAULT:
                fseq = this.getFormatedSequence(20, 6);
                break;
        }
        return fseq;
    }

    private String getFormatedSequence(final int gap_distance, final int sets_in_line) {
        return Utils.StringFormatter.getFormatedSequence(gap_distance, sets_in_line, this.sequence,
            this.escapeStyle, this.format);
    }

    public static String getFormatedSequence(final String sequence) {
        return Utils.StringFormatter.getFormatedSequence(10, 6, sequence, Utils.StringFormatter.HTML,
            Utils.StringFormatter.DEFAULT);
    }

    public static void main(final String[] arg) {
        final Sequence s = new Sequence();
        s.setEscapeStyle("HTML");
        s.setFormat("GENBANK");
        s.sequence =
            "AAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTAAAAATTTTTT";
        System.out.println(s.getFormatedSequence());
    }

}
