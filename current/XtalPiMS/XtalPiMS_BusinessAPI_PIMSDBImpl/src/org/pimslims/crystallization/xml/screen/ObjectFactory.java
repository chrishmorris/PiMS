//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.24 at 01:32:15 PM BST 
//


package org.pimslims.crystallization.xml.screen;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.pimslims.crystallization.xml.screen package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.pimslims.crystallization.xml.screen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SolutionType }
     * 
     */
    public SolutionType createSolutionType() {
        return new SolutionType();
    }

    /**
     * Create an instance of {@link Screen }
     * 
     */
    public Screen createScreen() {
        return new Screen();
    }

    /**
     * Create an instance of {@link SolutionList }
     * 
     */
    public SolutionList createSolutionList() {
        return new SolutionList();
    }

    /**
     * Create an instance of {@link ComponentList }
     * 
     */
    public ComponentList createComponentList() {
        return new ComponentList();
    }

    /**
     * Create an instance of {@link ComponentType }
     * 
     */
    public ComponentType createComponentType() {
        return new ComponentType();
    }

}
