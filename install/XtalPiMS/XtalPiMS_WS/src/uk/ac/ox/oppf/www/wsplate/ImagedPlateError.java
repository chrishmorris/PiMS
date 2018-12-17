
/**
 * ImagedPlateError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

package uk.ac.ox.oppf.www.wsplate;

public class ImagedPlateError extends java.lang.Exception{
    
    private uk.ac.ox.oppf.www.wsplate.ImagedPlateFault faultMessage;

    
        public ImagedPlateError() {
            super("ImagedPlateError");
        }

        public ImagedPlateError(java.lang.String s) {
           super(s);
        }

        public ImagedPlateError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ImagedPlateError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(uk.ac.ox.oppf.www.wsplate.ImagedPlateFault msg){
       faultMessage = msg;
    }
    
    public uk.ac.ox.oppf.www.wsplate.ImagedPlateFault getFaultMessage(){
       return faultMessage;
    }
}
    