
package org.pimslims.ws.server.jaxws;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "upload", namespace = "http://server.ws.pimslims.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "upload", namespace = "http://server.ws.pimslims.org/", propOrder = {
    "arg0",
    "arg1",
    "arg2"
})
public class Upload {

    @XmlElement(name = "arg0", namespace = "")
    private String arg0;
    @XmlElement(name = "arg1", namespace = "", nillable = true)
    private byte[] arg1;
    @XmlElement(name = "arg2", namespace = "")
    @XmlMimeType("application/octet-stream")
    private DataHandler arg2;

    /**
     * 
     * @return
     *     returns String
     */
    public String getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * 
     * @return
     *     returns byte[]
     */
    public byte[] getArg1() {
        return this.arg1;
    }

    /**
     * 
     * @param arg1
     *     the value for the arg1 property
     */
    public void setArg1(byte[] arg1) {
        this.arg1 = arg1;
    }

    /**
     * 
     * @return
     *     returns DataHandler
     */
    public DataHandler getArg2() {
        return this.arg2;
    }

    /**
     * 
     * @param arg2
     *     the value for the arg2 property
     */
    public void setArg2(DataHandler arg2) {
        this.arg2 = arg2;
    }

}
