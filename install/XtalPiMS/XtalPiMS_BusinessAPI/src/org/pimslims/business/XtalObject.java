package org.pimslims.business;

public abstract class XtalObject {
    public static String PROP_ID = "id";

    /**
     * the id in DB
     */
    protected Long id = -1l;

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
}
