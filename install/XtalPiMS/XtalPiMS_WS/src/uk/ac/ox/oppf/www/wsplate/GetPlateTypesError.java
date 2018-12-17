
/**
 * GetPlateTypesError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

package uk.ac.ox.oppf.www.wsplate;

public class GetPlateTypesError extends java.lang.Exception{
    
    private uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault faultMessage;

    
        public GetPlateTypesError() {
            super("GetPlateTypesError");
        }

        public GetPlateTypesError(java.lang.String s) {
           super(s);
        }

        public GetPlateTypesError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public GetPlateTypesError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault msg){
       faultMessage = msg;
    }
    
    public uk.ac.ox.oppf.www.wsplate.GetPlateTypesFault getFaultMessage(){
       return faultMessage;
    }
}
    