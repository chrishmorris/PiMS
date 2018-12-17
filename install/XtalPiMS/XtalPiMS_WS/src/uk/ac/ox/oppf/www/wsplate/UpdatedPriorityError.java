
/**
 * UpdatedPriorityError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

package uk.ac.ox.oppf.www.wsplate;

public class UpdatedPriorityError extends java.lang.Exception{
    
    private uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault faultMessage;

    
        public UpdatedPriorityError() {
            super("UpdatedPriorityError");
        }

        public UpdatedPriorityError(java.lang.String s) {
           super(s);
        }

        public UpdatedPriorityError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public UpdatedPriorityError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault msg){
       faultMessage = msg;
    }
    
    public uk.ac.ox.oppf.www.wsplate.UpdatedPriorityFault getFaultMessage(){
       return faultMessage;
    }
}
    