package org.pimslims.business.core.model;

import org.pimslims.business.XtalObject;

public class Protein extends XtalObject {
    public static String PROP_NAME  = "name";
    public static String PROP_TYPE  = "type";
    public static String PROP_SEQUENCE  = "sequence";
    public static String PROP_DESCRIPTION  = "description";
    
    private final String name;

    /**
     * must be one of 'protein', 'DNA', 'RNA', 'DNA/RNA', 'carbohydrate', 'other'
     */
    private final String type;

    private String sequence = "";

    private String description = "";

    /**
     * @param name
     * @param type
     */
    public Protein(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    /**
     * @return Returns the sequence.
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence The sequence to set.
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

}
