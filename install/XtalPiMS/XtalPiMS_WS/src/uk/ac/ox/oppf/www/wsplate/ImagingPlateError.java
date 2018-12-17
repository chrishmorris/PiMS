
/**
 * ImagingPlateError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

package uk.ac.ox.oppf.www.wsplate;

public class ImagingPlateError extends java.lang.Exception{
    
    private uk.ac.ox.oppf.www.wsplate.ImagingPlateFault faultMessage;

    
        public ImagingPlateError() {
            super("ImagingPlateError");
        }

        public ImagingPlateError(java.lang.String s) {
           super(s);
        }

        public ImagingPlateError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ImagingPlateError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(uk.ac.ox.oppf.www.wsplate.ImagingPlateFault msg){
       faultMessage = msg;
    }
    
    public uk.ac.ox.oppf.www.wsplate.ImagingPlateFault getFaultMessage(){
       return faultMessage;
    }
}
    