//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.24 at 12:41:30 AM BST 
//


package org.pimslims.crystallization.xml.rmxml;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.pimslims.crystallization.xml.adapters.BigDecimalAdapter;


/**
 * <p>Java class for stock complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stock">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="localID" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="stockConcentration" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="units" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="defaultLowConcentration" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="defaultHighConcentration" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="useAsBuffer" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="pH" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="vendorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vendorPartNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stock", propOrder = {

})
public class Stock {

    @XmlElement(required = true)
    protected BigInteger localID;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    @XmlSchemaType(name = "double")
    protected BigDecimal stockConcentration;
    @XmlElement(required = true)
    protected String units;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    @XmlSchemaType(name = "double")
    protected BigDecimal defaultLowConcentration;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    @XmlSchemaType(name = "double")
    protected BigDecimal defaultHighConcentration;
    protected Boolean useAsBuffer;
    @XmlElement(name = "pH", type = String.class)
    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    @XmlSchemaType(name = "double")
    protected BigDecimal ph;
    protected String vendorName;
    protected String vendorPartNumber;
    protected String comments;

    /**
     * Gets the value of the localID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLocalID() {
        return localID;
    }

    /**
     * Sets the value of the localID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLocalID(BigInteger value) {
        this.localID = value;
    }

    /**
     * Gets the value of the stockConcentration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getStockConcentration() {
        return stockConcentration;
    }

    /**
     * Sets the value of the stockConcentration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStockConcentration(BigDecimal value) {
        this.stockConcentration = value;
    }

    /**
     * Gets the value of the units property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnits() {
        return units;
    }

    /**
     * Sets the value of the units property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnits(String value) {
        this.units = value;
    }

    /**
     * Gets the value of the defaultLowConcentration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getDefaultLowConcentration() {
        return defaultLowConcentration;
    }

    /**
     * Sets the value of the defaultLowConcentration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultLowConcentration(BigDecimal value) {
        this.defaultLowConcentration = value;
    }

    /**
     * Gets the value of the defaultHighConcentration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getDefaultHighConcentration() {
        return defaultHighConcentration;
    }

    /**
     * Sets the value of the defaultHighConcentration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultHighConcentration(BigDecimal value) {
        this.defaultHighConcentration = value;
    }

    /**
     * Gets the value of the useAsBuffer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUseAsBuffer() {
        return useAsBuffer;
    }

    /**
     * Sets the value of the useAsBuffer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseAsBuffer(Boolean value) {
        this.useAsBuffer = value;
    }

    /**
     * Gets the value of the ph property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getPH() {
        return ph;
    }

    /**
     * Sets the value of the ph property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPH(BigDecimal value) {
        this.ph = value;
    }

    /**
     * Gets the value of the vendorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the value of the vendorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorName(String value) {
        this.vendorName = value;
    }

    /**
     * Gets the value of the vendorPartNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorPartNumber() {
        return vendorPartNumber;
    }

    /**
     * Sets the value of the vendorPartNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorPartNumber(String value) {
        this.vendorPartNumber = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

}
