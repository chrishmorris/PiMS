/**
 * pims-web org.pimslims.presentation.plateExperiment PlateNameFactory.java
 * 
 * @author Marc Savitsky
 * @date 5 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.plateExperiment;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;

/**
 * PlateNameFactory
 * 
 */

public interface PlateNameFactory {

    String suggestPlateName(ReadableVersion version, Holder holder, Protocol protocol);
}
