/**
 * pims-model org.pimslims.dao ModelVisitor.java
 * 
 * @author cm65
 * @date 17 Jun 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dao;

import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;

/**
 * ModelVisitor
 * 
 */
public interface ModelVisitor {

    /**
     * ModelVisitor.visitClass
     * 
     * @param metaClass
     * 
     *            Guaranteed to be called first, before the methods below are applied to the classes
     *            properties
     */
    public void openClass(MetaClass metaClass);

    /**
     * ModelVisitor.visitAttribute
     * 
     * @param attribute an attribute declared in the class
     * @return
     * 
     *         TODO may also need visitInheritedAttribute
     */
    public Object visitDeclaredAttribute(MetaAttribute attribute);

    /**
     * ModelVisitor.visitDeclaredRole
     * 
     * @param role a role declared in the class
     * @return
     * 
     *         TODO may also need visitInheritedRole
     */
    public Object visitDeclaredRole(MetaRole role);

    /**
     * ModelVisitor.closeClass
     * 
     * @return
     * 
     *         Called after the methods above
     */
    public Object closeClass();

}
