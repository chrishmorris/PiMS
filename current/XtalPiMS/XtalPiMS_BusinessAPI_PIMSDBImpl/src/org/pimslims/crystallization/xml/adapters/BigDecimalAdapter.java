//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.24 at 12:29:03 AM BST 
//


package org.pimslims.crystallization.xml.adapters;

import java.math.BigDecimal;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter
    extends XmlAdapter<String, BigDecimal>
{


    public BigDecimal unmarshal(String value) {
        return new BigDecimal(value);
    }

    public String marshal(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
