package org.pimslims.ws.client.stubs;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for upload complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;upload&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;arg0&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;arg1&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}base64Binary&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;arg2&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}base64Binary&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "upload", propOrder = { "arg0", "arg1", "arg2" })
public class Upload {

    protected String arg0;

    @XmlElementRef(name = "arg1", type = JAXBElement.class)
    protected JAXBElement<byte[]> arg1;

    @XmlMimeType("application/octet-stream")
    protected DataHandler arg2;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getArg0() {
        return this.arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setArg0(final String value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     */
    public JAXBElement<byte[]> getArg1() {
        return this.arg1;
    }

    /**
     * Sets the value of the arg1 property.
     * 
     * @param value allowed object is {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * 
     */
    public void setArg1(final JAXBElement<byte[]> value) {
        this.arg1 = (value);
    }

    /**
     * Gets the value of the arg2 property.
     * 
     * @return possible object is {@link DataHandler }
     * 
     */
    public DataHandler getArg2() {
        return this.arg2;
    }

    /**
     * Sets the value of the arg2 property.
     * 
     * @param value allowed object is {@link DataHandler }
     * 
     */
    public void setArg2(final DataHandler value) {
        this.arg2 = value;
    }

}
