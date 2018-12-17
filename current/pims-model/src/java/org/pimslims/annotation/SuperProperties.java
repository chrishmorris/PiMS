/**
 * pims-model org.pimslims.annotation ExportProperties.java
 * 
 * @author cm65
 * @date 17 Jun 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.annotation;

/**
 * SuperProperties
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SuperProperties {

    SubPropertyOf[] value();

}
