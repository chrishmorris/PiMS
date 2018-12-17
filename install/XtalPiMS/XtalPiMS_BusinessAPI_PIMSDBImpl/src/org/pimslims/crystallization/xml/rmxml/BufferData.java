//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.24 at 12:41:30 AM BST 
//


package org.pimslims.crystallization.xml.rmxml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.pimslims.crystallization.xml.adapters.BigDecimalAdapter;


/**
 * <p>Java class for bufferData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bufferData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="pKa" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="titrationTable" type="{}titrationTable"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bufferData", propOrder = {
    "pKa",
    "titrationTable"
})
public class BufferData {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    @XmlSchemaType(name = "double")
    protected BigDecimal pKa;
    protected TitrationTable titrationTable;

    /**
     * Gets the value of the pKa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getPKa() {
        return pKa;
    }

    /**
     * Sets the value of the pKa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPKa(BigDecimal value) {
        this.pKa = value;
    }

    /**
     * Gets the value of the titrationTable property.
     * 
     * @return
     *     possible object is
     *     {@link TitrationTable }
     *     
     */
    public TitrationTable getTitrationTable() {
        return titrationTable;
    }

    /**
     * Sets the value of the titrationTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link TitrationTable }
     *     
     */
    public void setTitrationTable(TitrationTable value) {
        this.titrationTable = value;
    }

}