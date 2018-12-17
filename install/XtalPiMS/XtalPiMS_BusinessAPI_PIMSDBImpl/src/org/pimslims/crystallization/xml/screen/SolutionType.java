//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.24 at 01:32:15 PM BST 
//


package org.pimslims.crystallization.xml.screen;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SolutionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SolutionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="solutionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pHfinal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="components" type="{}ComponentList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SolutionType", propOrder = {
    "solutionId",
    "localNum",
    "pHfinal",
    "components"
})
public class SolutionType {

    @XmlElement(required = true)
    protected String solutionId;
    @XmlElement(required = true)
    protected String localNum;
    protected BigDecimal pHfinal;
    @XmlElement(required = true)
    protected ComponentList components;

    /**
     * Gets the value of the solutionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolutionId() {
        return solutionId;
    }

    /**
     * Sets the value of the solutionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolutionId(String value) {
        this.solutionId = value;
    }

    /**
     * Gets the value of the localNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalNum() {
        return localNum;
    }

    /**
     * Sets the value of the localNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalNum(String value) {
        this.localNum = value;
    }

    /**
     * Gets the value of the pHfinal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPHfinal() {
        return pHfinal;
    }

    /**
     * Sets the value of the pHfinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPHfinal(BigDecimal value) {
        this.pHfinal = value;
    }

    /**
     * Gets the value of the components property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentList }
     *     
     */
    public ComponentList getComponents() {
        return components;
    }

    /**
     * Sets the value of the components property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentList }
     *     
     */
    public void setComponents(ComponentList value) {
        this.components = value;
    }

}