
/**
 * GetCapturePointsError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

package uk.ac.ox.oppf.www.wsplate;

public class GetCapturePointsError extends java.lang.Exception{
    
    private uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault faultMessage;

    
        public GetCapturePointsError() {
            super("GetCapturePointsError");
        }

        public GetCapturePointsError(java.lang.String s) {
           super(s);
        }

        public GetCapturePointsError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public GetCapturePointsError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault msg){
       faultMessage = msg;
    }
    
    public uk.ac.ox.oppf.www.wsplate.GetCapturePointsFault getFaultMessage(){
       return faultMessage;
    }
}
    