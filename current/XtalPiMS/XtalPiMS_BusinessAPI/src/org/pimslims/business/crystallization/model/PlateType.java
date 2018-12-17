/**
 * 
 */
package org.pimslims.business.crystallization.model;



/**
 * Class describing a plate
 * 
 * @author Jon Diprose
 */
public class PlateType {
    
    /**
     * The numeric identifier of this TrialPlate. </p>
     */
    private Long id = null;
    
    /**
     * The name of the PlateType
     */
    private String name;
    
    /**
     * The number of rows
     */
    int rows = 8;
    
    /**
     * The number of columns
     */
    int columns = 12;
    
    /**
     * The number of sub-positions per well
     */
    int subPositions = 4;
    
    /**
     * The one-based index of the reservoir sub-position, consistent
     * with WellPosition.subPosition
     */
    int reservoir = 4;
    
    /**
     * Zero-argument constructor
     */
    public PlateType() {
        super();
    }
    
    /**
     * Construct a PlateType with the specified name
     * 
     * @param name
     */
    public PlateType(String name) {
        setName(name);
    }
    
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        if (rows <= 0) {
            throw new IllegalArgumentException("rows must be > 0");
        }
        this.rows = rows;
    }
    
    /**
     * @return the columns
     */
    public int getColumns() {
        return columns;
    }
    
    /**
     * @param columns the columns to set
     */
    public void setColumns(int columns) {
        if (columns <= 0) {
            throw new IllegalArgumentException("columns must be > 0");
        }
        this.columns = columns;
    }
    
    /**
     * @return the subPositions
     */
    public int getSubPositions() {
        return subPositions;
    }
    
    /**
     * @param subPositions the subPositions to set
     */
    public void setSubPositions(int subpositions) {
        if (subpositions <= 0) {
            throw new IllegalArgumentException("subPositions must be > 0");
        }
        this.subPositions = subpositions;
    }
    
    /**
     * @return the well number of the reservoir
     * This should be the same as the max subposition if there is a reservoir,
     * and 100 if there is not
     */
    public int getReservoir() {
        return reservoir;
    }
    
    /**
     * Setting reservoir > subPositions is indicative of no reservoir.
     * 
     * @param reservoir the reservoir to set
     */
    public void setReservoir(int reservoir) {
        if (reservoir <= 0) {
            throw new IllegalArgumentException("reservoir must be > 0");
        }
        this.reservoir = reservoir;
    }
    

    public boolean hasReservoir() {
        return this.getReservoir() == this.subPositions;
    }

    public void setHasReservoir(boolean hasReservoir) {
        if (hasReservoir) {
            this.reservoir = this.subPositions;
        } else {
            this.reservoir = 100;
        }
    }
    
}
