/**
 * 
 */
package org.pimslims.business.crystallization.model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Class to describe the position of a trial drop
 * within a trial plate. WellPosition is immutable.</p>
 * 
 * <p>Note that row, column and subPosition are one-based and
 * that a WellPosition with row = 1, column = 1 and
 * subPosition = 1 will return "A01.1" from a call to the
 * toString() method.</p>
 * 
 * <p>If using the {@link #WellPosition(String)} constructor,
 * the name is first converted to upper case and then the row
 * and column numbers are parsed using the pattern
 * "^([A-Z]+)(\d+)(\.\d+)?$". The row is assumed to be encoded in
 * base 26 in the alphabetic component and the column given
 * by the numeric component. When using this constructor,
 * the row is A-based and the column and subPosition 1-based, i.e.
 * the name "A01.1" will be parsed to row = 1, column = 1 and
 * subPosition = 1.</p>
 * 
 * <p>If constructed using {@link #WellPosition(String)}, the
 * WellPosition will remember the name specified in the constructor
 * and will return it when {@link #toString()} is called, though
 * not when {@link #toStringNoSubPosition()} is called. Note that
 * the name is not used in equality testing or hashcode calculation.</p>
 * 
 * <p>This implementation means that equivalent WellPositions
 * can be parsed from many different names. For example, the
 * following are all equal:</p>
 * 
 * <p><pre><code>     WellPosition wp1 = new WellPosition("A1");
 *     WellPosition wp2 = new WellPosition("A01");
 *     WellPosition wp3 = new WellPosition("A0001");
 *     WellPosition wp4 = new WellPosition("A1.1");
 *     WellPosition wp5 = new WellPosition("A01.01");</code></pre></p>
 * 
 * @author Jon Diprose
 */
public class WellPosition implements Comparable<Object> {

    /**
     * <p>RegExp pattern to use to extract row and column from name.</p>
     * 
     * <p><code>NAME_PATTERN = "^([A-Z]+)(\\d+)(\\.\\d+)?$";</code></p>
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^([A-Z]+)(\\d+)(\\.\\d+)?$");
    /**
     * <p>The one-based row, i.e. row = 1 for the first row.</p>
     */
    private final int row;
    /**
     * <p>The one-based column, i.e. column = 1 for the first column.</p>
     */
    private final int column;
    /**
     * <p>The one-based subPosition, i.e. subPosition = 1 for the first
     * subPosition.</p>
     */
    private final int subPosition;
    /**
     * <p>The human-readable name of this WellPosition.</p>
     */
    private String name = null;

    /**
     * <p>Format the name from the row, column and subPosition. The
     * row is encoded in base 26. The column and subPosition are
     * used as given, with the subPosition separated from the column
     * by a '.'. If subPosition is < 1 then both it and the separator
     * will be omitted. The formatted name conforms to {@link #NAME_PATTERN}
     * and can be parsed by {@link #parseName(String)}.</p>
     * 
     * @param row - the row
     * @param column - the column
     * @param subPosition - the subPosition
     * @return The name
     */
    public static String formatName(int row, int column, int subPosition) {

        if ((row < 1) || (column < 1)) {
            throw new IllegalArgumentException("row and column must be > 0");
        }

        StringBuffer sb = new StringBuffer();
        if (1 == row) {
            sb.append("A");
        } else {
            long r = row - 1;
            long power = Math.round(Math.floor(Math.log(r) / Math.log(26)));
            for (int i = 0; i <= power; i++) {
                char c = (char) (65 + r % 26);
                sb.append(Character.toString(c));
                r = Math.round(r / 26) - 1;
            }
            sb = sb.reverse();
        }

        DecimalFormat df = new DecimalFormat("#00");
        sb.append(df.format(column));

        if (subPosition > 0) {
            sb.append(".");
            sb.append(subPosition);
        }

        return sb.toString();

    }

    /**
     * <p>Parse the row and column from a name. The name must match
     * {@link #NAME_PATTERN}, otherwise an IllegalArgumentException
     * is thrown. The row is assumed to be encoded in base 26 in
     * the initial alphabetic part of the name. The column is given
     * by the subsequent numeric part. The subPosition is separated
     * from the column by a '.'. If no subPosition is specified, it
     * is assumed to be 1.</p>
     * 
     * <p>Returns a three-element array of int containing the row, column
     * and subPosition as elements 0, 1 and 2 respectively.</p>
     * 
     * @param name - the name to parse
     * @return int[] {row, column, subposition}
     * @throws IllegalArgumentException if the name does not match {@link #NAME_PATTERN}
     */
    public static int[] parseName(String name) {

        Matcher matcher = NAME_PATTERN.matcher(name);

        if (matcher.matches()) {

            String sr = matcher.group(1);
            int r = 0;
            for (int i = 0; i < sr.length(); i++) {
                int j = sr.charAt(sr.length() - 1 - i) - 64;
                r += j * Math.pow(26, i);
            }

            int c = Integer.parseInt(matcher.group(2));

            int s = 1;
            if (null != matcher.group(3)) {
                s = Integer.parseInt(matcher.group(3).substring(1));
            }

            if ((r < 1) || (c < 1) || (s < 1)) {
                throw new IllegalArgumentException("Invalid well name: "+name);
            }

            return new int[]{r, c, s};

        } else {
            throw new IllegalArgumentException("Invalid format for well name: "+name);
        }

    }

    /**
     * <p>Construct a WellPosition with the specified row and column
     * and subPosition = 1.</p>
     * 
     * @param row - the row
     * @param column - the column
     */
    public WellPosition(int row, int column) {
        if ((row < 1) || (column < 1)) {
            throw new IllegalArgumentException("row, column and subPosition must be > 0");
        }
        this.row = row;
        this.column = column;
        this.subPosition = 1;
    }

    /**
     * <p>Construct a WellPosition with the specified row, column
     * and subPosition.</p>
     * 
     * @param row - the row
     * @param column - the column
     * @param subPosition - the subPosition
     */
    public WellPosition(int row, int column, int subPosition) {
        if ((row < 1) || (column < 1) || (subPosition < 1)) {
            throw new IllegalArgumentException("row, column and subPosition must be > 0");
        }
        this.row = row;
        this.column = column;
        this.subPosition = subPosition;
    }

    /**
     * <p>Construct a WellPosition with row and column from the
     * specified WellPosition but the specified subPosition.</p>
     * 
     * @param wellPosition - the WellPosition from which row and column are obtained
     * @param subPosition - the subPosition
     */
    public WellPosition(WellPosition wellPosition, int subPosition) {
        if (subPosition < 1) {
            throw new IllegalArgumentException("subPosition must be > 0");
        }
        this.row = wellPosition.getRow();
        this.column = wellPosition.getColumn();
        this.subPosition = subPosition;
    }

    /**
     * <p>Construct a WellPosition with the specified name. The row,
     * column and subPosition are parsed from the name by
     * {@link #parseName(String)}.</p>
     * 
     * @param name - the name
     * @see #parseName(String)
     */
    public WellPosition(String name) {
        this.name = name.toUpperCase();
        int[] ii = parseName(this.name);
        this.row = ii[0];
        this.column = ii[1];
        this.subPosition = ii[2];
    }

    /**
     * @return row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * @return subPosition
     */
    public int getSubPosition() {
        return this.subPosition;
    }

    /**
     * <p>Return the name of this WellPosition with no subPosition
     * component. {@link #formatName(int, int, int)} is always used,
     * with the third argument (subPosition) set to zero.</p>
     * 
     * @return The name of this WellPosition with no subPosition component
     * @see #formatName(int, int, int)
     */
    public String toStringNoSubPosition() {
        return formatName(this.row, this.column, 0);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new WellPosition(this.row, this.column, this.subPosition);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WellPosition)) {
            return false;
        }
        WellPosition other = (WellPosition) obj;
        return ((this.column == other.column) && (this.row == other.row) && (this.subPosition == other.subPosition));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.getClass().hashCode() +
                (37 * this.row) +
                (43 * this.column) +
                (79 * this.subPosition);
    }

    /**
     * <p>Return the name of this WellPosition. If the
     * {@link #WellPosition(String)} constructor was used, this
     * method returns the name supplied to the constructor.
     * Otherwise {@link #formatName(int, int, int)} is used
     * to format the name.</p>
     * 
     * @return The name of this WellPosition
     * @see java.lang.Object#toString()
     * {@link #formatName(int, int, int)}
     */
    @Override
    public String toString() {
        if (null == this.name) {
            this.name = formatName(this.row, this.column, this.subPosition);
        }
        return this.name;
    }

    public int compareTo(Object o) {
        if (!(o instanceof WellPosition)) {
            throw new ClassCastException("A WellPosition object expected.");
        }
        WellPosition otherWell = (WellPosition)o;
        if (otherWell.getRow() > this.getRow()) {
            return -1;
        } else if (otherWell.getRow() < this.getRow()) {
            return 1;
        }
        //so this means that the rows are the same...
        if (otherWell.getColumn() > this.getColumn()) {
            return -1;
        } else if (otherWell.getColumn() < this.getColumn()) {
            return 1;
        }
        //then they must be in the same well...
        if (otherWell.getSubPosition() > this.getSubPosition()) {
            return -1;
        } else if (otherWell.getSubPosition() < this.getSubPosition()) {
            return 1;
        }
        //then they must the same well...
        return 0;
    }
}
