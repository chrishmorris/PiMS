/**
 * pims-web org.pimslims.presentation.export.displaytag FastaView.java
 * 
 * @author Marc Savitsky
 * @date 6 Jan 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.displaytag.export;

import org.apache.commons.lang.StringUtils;
import org.displaytag.export.BaseExportView;
import org.displaytag.model.TableModel;

/**
 * FastaView
 * 
 */
/**
 * Export view for comma separated value exporting.
 * 
 * @author Fabrizio Giustina
 * @version $Revision: 1081 $ ($Author: fgiust $)
 */
public class FastaView extends BaseExportView {

    /**
     * @see org.displaytag.export.BaseExportView#setParameters(TableModel, boolean, boolean, boolean)
     */
    @Override
    public void setParameters(final TableModel tableModel, final boolean exportFullList,
        final boolean includeHeader, final boolean decorateValues) {
        super.setParameters(tableModel, exportFullList, includeHeader, decorateValues);
    }

    /**
     * @see org.displaytag.export.BaseExportView#getRowEnd()
     */
    @Override
    protected String getRowEnd() {
        return "\n"; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BaseExportView#getCellEnd()
     */
    @Override
    protected String getCellEnd() {
        return ","; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BaseExportView#getAlwaysAppendCellEnd()
     */
    @Override
    protected boolean getAlwaysAppendCellEnd() {
        return false;
    }

    /**
     * @see org.displaytag.export.BaseExportView#getAlwaysAppendRowEnd()
     */
    @Override
    protected boolean getAlwaysAppendRowEnd() {
        return true;
    }

    /**
     * @see org.displaytag.export.ExportView#getMimeType()
     */
    public String getMimeType() {
        //return "text/csv"; //$NON-NLS-1$
        return "text/plain";
    }

    /**
     * Escaping for csv format.
     * <ul>
     * <li>Quotes inside quoted strings are escaped with a /</li>
     * <li>Fields containings newlines or , are surrounded by "</li>
     * </ul>
     * Note this is the standard CVS format and it's not handled well by excel.
     * 
     * @see org.displaytag.export.BaseExportView#escapeColumnValue(java.lang.Object)
     */

    @Override
    protected String escapeColumnValue(final Object value) {
        final String stringValue = StringUtils.trim(value.toString());
        return stringValue;
    }

}
