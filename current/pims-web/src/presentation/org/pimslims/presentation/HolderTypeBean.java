package org.pimslims.presentation;

import org.pimslims.model.reference.HolderType;

public class HolderTypeBean extends ModelObjectShortBean {

    private final Integer maxRow;

    private final Integer maxColumn;

    public HolderTypeBean(final HolderType modelObject) {
        super(modelObject);

        this.maxRow = modelObject.getMaxRow();
        this.maxColumn = modelObject.getMaxColumn();
    }

    public int getMaxRow() {
        return this.maxRow;
    }

    public int getMaxColumn() {
        return this.maxColumn;
    }
}
