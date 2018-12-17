/**
 * 
 */
package org.pimslims.metamodel;

import java.lang.annotation.Annotation;

/**
 * Provides information about aspects of the data model. Common attributes for MetaClasses, MetaAttributes,
 * and MetaRoles. *
 * 
 * @author cm65
 * 
 */
public interface MetaElement {

    /**
     * @return the name of the element in the data model
     */
    public String getName();

    /**
     * @return the name to use in the UI
     */
    public String getAlias();

    /**
     * Used to customize the generic UI
     * 
     * @return true if this model element should not be shown in the UI
     */
    public boolean isHidden();

    /**
     * @return documentation for this class or field
     */
    public String getHelpText();

    /**
     * @return the instance of a MetaClass
     */
    public MetaClass getMetaClass();

    /**
     * @return whether the element is mandatory (relevant to the UI)
     */
    public boolean isRequired();

    /**
     * MetaElement.getAnnotation
     * 
     * @param class1
     * @return
     */
    public <T extends Annotation> T getAnnotation(final Class<T> annotationClass);

}
