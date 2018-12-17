/**
 * V4_3-web org.pimslims.csv MultiPlateScreen.java
 * 
 * @author cm65
 * @date 25 Nov 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.spreadsheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MultiPlateScreen
 * 
 */
public class MultiPlateScreen {

    /**
     * Barcode => Well => Keyword => Value
     */
    private final Map<String, Map<String, Map<String, String>>> values = new HashMap();

    /**
     * wells Invariant: all wells in values map are also in here Invariant: all wells in here are in every
     * values.get(barcode).
     */
    private final Collection<String> wells = new HashSet<String>();

    /**
     * keywords Collection<String> All parameters, all input samples Also perhaps target, construct, status
     * Invariant: all keywords in values map are also in here
     */
    private final Collection<String> keywords = new HashSet<String>();

    /**
     * wellProperties Map<String,String> keyword => well => value Invariant: a keyword is in here if and only
     * if every non-null value in any plate is the same for the specified well.
     */
    private final Map<String, Map<String, String>> wellProperties =
        new HashMap<String, Map<String, String>>();

    /**
     * plateProperties Map<String,String> keyword => plate => value Invariant: a keyword is in here if and
     * only if every non-null value is the same throughout the plate
     */
    private final Map<String, Map<String, String>> plateProperties =
        new HashMap<String, Map<String, String>>();

    private static final int PLATE_ROW = 0;

    private static final int WELL_COLUMN = 0;

    /**
     * Constructor for MultiPlateScreen
     * 
     * @param stringArray
     */
    public MultiPlateScreen(final String[][] array) {
        this();
        final List<String> barcodes = new ArrayList<String>();
        int plate_column = 0;
        for (;; plate_column++) {
            if ("Plate".equals(array[MultiPlateScreen.PLATE_ROW][plate_column])) {
                break;
            }
        }
        final String[] plates = array[MultiPlateScreen.PLATE_ROW];
        for (int column = plate_column + 1; column < plates.length; column++) {
            if (!"".equals(plates[column])) {
                barcodes.add(plates[column]);
            }
        }
        // find plate keywords
        final List<String> plateProps = new ArrayList<String>();
        int well_row = MultiPlateScreen.PLATE_ROW + 1;
        for (;; well_row++) {
            if ("Well".equals(array[well_row][MultiPlateScreen.WELL_COLUMN])) {
                break;
            }
            plateProps.add(array[well_row][plate_column]);
        }
        final String[] resultProps = array[well_row]; //"Well", wellProps, resultProps, resultProps
        //TODO process well parameters

        // now there is one row for each well
        final Collection<String> wellNames = new ArrayList<String>(96);
        for (int rowNum = well_row + 1; rowNum < array.length; rowNum++) {
            final String[] row = array[rowNum];
            final String well = row[MultiPlateScreen.WELL_COLUMN];
            wellNames.add(well);
            //process well values  
            for (int colNum = MultiPlateScreen.WELL_COLUMN + 1; colNum <= plate_column; colNum++) {
                final String keyword = array[well_row][colNum];
                final String value = row[colNum];
                for (final Iterator iterator = barcodes.iterator(); iterator.hasNext();) {
                    final String barcode = (String) iterator.next();
                    this.add(barcode, well, keyword, value);
                }
            }

            // process result values
            String barcode = null;
            for (int colNum = plate_column + 1; colNum < row.length; colNum++) {
                if (!"".equals(array[MultiPlateScreen.PLATE_ROW][colNum])) {
                    barcode = array[MultiPlateScreen.PLATE_ROW][colNum];
                }
                final String keyword = resultProps[colNum];
                if (!"".equals(keyword)) {
                    this.add(barcode, well, keyword, row[colNum]);
                }
            }
        }
        // process plate parameters
        for (int rowNum = MultiPlateScreen.PLATE_ROW + 1; rowNum < well_row; rowNum++) {
            final String keyword = array[rowNum][plate_column];
            for (int colNum = plate_column + 1; colNum < array[rowNum].length; colNum++) {
                if (!"".equals(array[MultiPlateScreen.PLATE_ROW][colNum])) {
                    final String barcode = array[MultiPlateScreen.PLATE_ROW][colNum];
                    final String value = array[rowNum][colNum];
                    for (final Iterator iterator = wellNames.iterator(); iterator.hasNext();) {
                        final String well = (String) iterator.next();
                        this.add(barcode, well, keyword, value);
                    }
                }
            }
        }
    }

    /**
     * Constructor for testing MultiPlateScreen
     */
    MultiPlateScreen() {
        super();
    }

    /**
     * MultiPlateScreen.add
     * 
     * @param string
     * @param string2
     * @param string3
     * @param string4
     */
    public void add(final String barcode, final String well, final String keyword, String value) {
        if (null == value) {
            value = "";
        }
        this.log("added", barcode, well, keyword, value);

        this.addBarcode(barcode);
        this.addWell(well);
        /* 
         * The code is simplified by assuming values are not updated
         * */
        assert !this.getPlateWell(barcode, well).containsKey(value) : "Repeated value for: " + barcode + ":"
            + well + ":" + keyword + "=" + value;

        this.getPlateWell(barcode, well).put(keyword, value);

        // process summaries
        if (this.keywords.contains(keyword)) {
            // if necessary, remove from summaries 
            if (this.wellProperties.containsKey(keyword)) {
                final String common = this.wellProperties.get(keyword).get(well);
                if (null == value) { //TODO should that be "".equals(value)
                    // no action needed to maintain invariant
                } else if (null == common) {
                    // this keyword is not yet recorded for this well position, or is recorded as null
                    this.wellProperties.get(keyword).put(well, value);
                } else {
                    if (!value.equals(common)) {
                        this.wellProperties.remove(keyword);
                    }
                }
            }
            if (this.plateProperties.containsKey(keyword)) {
                final String common = this.plateProperties.get(keyword).get(barcode);
                if (null == value) {//TODO should that be "".equals(value)
                    // no action needed to maintain invariant
                } else if (null == common) {
                    // this keyword is not yet recorded for this plate, or is recorded as null
                    this.plateProperties.get(keyword).put(barcode, value);
                } else {
                    if (!value.equals(common)) {
                        this.plateProperties.remove(keyword);
                    }
                }
            }
        } else {
            // first instance of this keyword
            this.keywords.add(keyword);
            final Map<String, String> wellValues = new HashMap();
            wellValues.put(well, value);
            this.wellProperties.put(keyword, wellValues);
            final Map<String, String> plateValues = new HashMap();
            plateValues.put(barcode, value);
            this.plateProperties.put(keyword, plateValues);
        }

    }

    /**
     * MultiPlateScreen.log
     * 
     * @param string
     * @param barcode
     * @param well
     * @param keyword
     * @param value
     */
    private void log(final String... strings) {
        for (final String string : strings) {
            System.out.print(string + ":");
        }
        System.out.print("\n");
    }

    /**
     * MultiPlateScreen.addWell
     * 
     * @param well
     */
    private void addWell(final String well) {
        if (this.wells.contains(well)) {
            return;
        }
        this.wells.add(well);
        for (final Map<String, Map<String, String>> map : this.values.values()) {
            map.put(well, new HashMap<String, String>());
        } //TODO add to wellProperties
    }

    /**
     * MultiPlateScreen.addBarcode
     * 
     * @param barcode
     */
    private void addBarcode(final String barcode) {
        if (this.values.containsKey(barcode)) {
            return;
        }
        final HashMap<String, Map<String, String>> wellValues = new HashMap<String, Map<String, String>>();
        for (final String well : this.wells) {
            wellValues.put(well, new HashMap<String, String>());
        }
        this.values.put(barcode, wellValues); //TODO add to plateProperties
    }

    private Map<String, String> getPlateWell(final String barcode, final String well) {
        return this.values.get(barcode).get(well);
    }

    /**
     * MultiPlateScreen.getValue
     * 
     * @param string
     * @param string2
     * @param string3
     * @return
     */
    public String getValue(final String barcode, final String well, final String keyword) {
        final Map<String, String> plateWell = this.getPlateWell(barcode, well);
        if (!plateWell.containsKey(keyword)) {
            return "";
        }
        return plateWell.get(keyword);
    }

    /**
     * MultiPlateScreen.getKeywords
     * 
     * @return
     */
    public Collection<String> getKeywords() {
        return new HashSet(this.keywords);
    }

    /**
     * MultiPlateScreen.getWells
     * 
     * @return
     */
    public Collection<String> getWells() {
        return new HashSet(this.wells);
    }

    /**
     * MultiPlateScreen.getPlates
     * 
     * @return
     */
    public Collection<String> getPlates() {
        return new HashSet(this.values.keySet());
    }

    @Override
    public int hashCode() {
        int ret = 5309;
        final int plateNum = 0, wellNum = 0, keywordNum = 0;
        for (final String barcode : this.getPlates()) {
            ret += barcode.hashCode() * 3529;
            for (final String well : this.getWells()) {
                ret += well.hashCode() * 4051;
                for (final String keyword : this.getKeywords()) {
                    ret += keyword.hashCode() * 4789;
                    final String value = this.getValue(barcode, well, keyword);
                    ret += value.hashCode() * (2 * plateNum + 1) * (6 * wellNum + 1) * (10 * keywordNum) + 1;
                }
            }
        }
        return ret;
    }

    /**
     * MultiPlateScreen.equals
     * 
     * @see java.lang.Object#equals(java.lang.Object) Implements value equality Useful for test cases.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.hashCode() != this.hashCode()) {
            return false; // quick, inexpensive check
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final MultiPlateScreen other = (MultiPlateScreen) obj;
        if (this.keywords.size() != other.keywords.size()) {
            return false;
        }
        if (this.wells.size() != other.wells.size()) {
            return false;
        }
        if (this.getPlates().size() != other.getPlates().size()) {
            return false;
        }
        for (final String barcode : this.getPlates()) {
            if (!other.getPlates().contains(barcode)) {
                return false;
            }
            for (final String well : this.getWells()) {
                if (!other.getWells().contains(well)) {
                    return false;
                }
                for (final String keyword : this.getKeywords()) {
                    if (!other.getKeywords().contains(keyword)) {
                        return false;
                    }
                    final String thisValue = this.getValue(barcode, well, keyword);
                    final String otherValue = other.getValue(barcode, well, keyword);
                    if (null == thisValue) {
                        if (null != otherValue) {
                            return false;
                        }
                    } else {
                        if (!thisValue.equals(otherValue)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * MultiPlateScreen.getWellProperties
     * 
     * 
     * @return Keyword => well => value
     */
    public Map<String, Map<String, String>> getWellProperties() {
        final Map ret = new HashMap<String, Map<String, String>>();
        keyword: for (final String keyword : this.wellProperties.keySet()) {
            final Map<String, String> wellValues = this.wellProperties.get(keyword);
            // That includes all the right values, but also some that are null in some plates
            // we must check that the value is uniform in all plates
            well: for (final String well : this.getWells()) {
                final String value = wellValues.get(well);
                if (null == value) {
                    // that's OK, must be null for all plates
                } else {
                    for (final String plate : this.getPlates()) {
                        if (!value.equals(this.getValue(plate, well, keyword))) {
                            // we have found a well that is not uniform for this keyword
                            continue keyword;
                        }
                    }
                }
            }
            ret.put(keyword, new HashMap(wellValues));
        }
        return ret;
    }

    /**
     * MultiPlateScreen.getPlateProperties TODO filter out ones that are sometimes null
     * 
     * @return keyword => barcode => value
     */
    public Map<String, Map<String, String>> getPlateProperties() {
        final Map ret = new HashMap<String, Map<String, String>>();
        keyword: for (final String keyword : this.plateProperties.keySet()) {
            final Map<String, String> plateValues = this.plateProperties.get(keyword);
            // That includes all the right values, but also some that are null in some positions
            // we must check that the value is uniform in all wells
            plate: for (final String barcode : this.getPlates()) {
                final String value = plateValues.get(barcode);
                if (null == value) {
                    // that's OK, must be null throughout the plate
                } else {
                    for (final String well : this.getWells()) {
                        if (!value.equals(this.getValue(barcode, well, keyword))) {
                            // we have found a plate that is not uniform for this keyword
                            continue keyword;
                        }
                    }
                }
            }
            ret.put(keyword, new HashMap(plateValues));
        }
        return ret;
    }

    /**
     * MultiPlateScreen.toStringArray
     */
    public String[][] toStringArray() {
        assert 0 < this.keywords.size();
        final List<String> plateProps = new ArrayList(this.plateProperties.keySet());
        Collections.sort(plateProps);
        final List<String> wellProps = new ArrayList(this.wellProperties.keySet());
        plateProps.removeAll(wellProps); // if it's the same everywhere, show as column not row
        Collections.sort(wellProps);
        final List<String> resultProps = new ArrayList(this.keywords);
        resultProps.removeAll(plateProps);
        resultProps.removeAll(wellProps);
        if (0 == resultProps.size()) {
            final String last = wellProps.remove(wellProps.size() - 1);
            resultProps.add(last);
        }
        Collections.sort(resultProps);
        final List<String[]> ret = new ArrayList<String[]>();

        // make first line: list of plates
        final List<String> plates = new ArrayList<String>();
        // begin with padding to make space for well keywords
        for (int i = 0; i < wellProps.size(); i++) {
            plates.add("");
        }
        plates.add("Plate");
        final List<String> platesList = new ArrayList(this.getPlates());
        Collections.sort(platesList);
        for (final Iterator iterator = platesList.iterator(); iterator.hasNext();) {
            final String barcode = (String) iterator.next();
            plates.add(barcode);
            // add padding, if there is more than one result keyword
            for (int i = 1; i < resultProps.size(); i++) {
                plates.add("");
            }
        }
        ret.add(plates.toArray(new String[] {}));

        // make a row for each plate parm
        for (final Iterator iterator = plateProps.iterator(); iterator.hasNext();) {
            final String keyword = (String) iterator.next();
            final List<String> row = new ArrayList<String>();
            // begin with padding to make space for well keywords
            for (int i = 0; i < wellProps.size(); i++) {
                plates.add("");
            }
            row.add(keyword);
            for (final Iterator plateIterator = platesList.iterator(); plateIterator.hasNext();) {
                final String barcode = (String) plateIterator.next();
                row.add(this.plateProperties.get(keyword).get(barcode));
            }
            ret.add(row.toArray(new String[] {}));
        }

        // now a row listing the keywords for results
        final List<String> resultHeader = new ArrayList<String>();
        resultHeader.add("Well");
        resultHeader.addAll(wellProps);
        // now result properties, once per plate
        for (final Iterator iterator = platesList.iterator(); iterator.hasNext(); iterator.next()) {
            resultHeader.addAll(resultProps);
        }
        ret.add(resultHeader.toArray(new String[] {}));

        // now one row per well
        final List<String> wellNames = new ArrayList(this.wells);
        Collections.sort(wellNames);
        for (final String well : wellNames) {
            final List<String> results = new ArrayList<String>();
            results.add(well);
            // well values
            for (final Iterator iterator = wellProps.iterator(); iterator.hasNext();) {
                final String keyword = (String) iterator.next();
                results.add(this.wellProperties.get(keyword).get(well));
            }
            // results values
            for (final Iterator plateIterator = platesList.iterator(); plateIterator.hasNext();) {
                final String barcode = (String) plateIterator.next();
                for (final Iterator resultIterator = resultProps.iterator(); resultIterator.hasNext();) {
                    final String keyword = (String) resultIterator.next();
                    results.add(this.getValue(barcode, well, keyword));
                }
            }
            ret.add(results.toArray(new String[] {}));
        }
        return ret.toArray(new String[][] {});
    }

}
