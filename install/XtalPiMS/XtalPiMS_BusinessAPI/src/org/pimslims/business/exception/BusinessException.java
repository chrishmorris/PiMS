/*
 * BusinessException.java
 *
 * Created on 27 June 2007, 11:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.exception;

/**
 *
 * @author Ian Berry
 */
public class BusinessException extends java.lang.Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5097806470509406260L;
	/**
     * Creates a new instance of <code>BusinessException</code> without detail message.
     */
    public BusinessException() {
    }
    
   /**
     * Constructs an instance of <code>BusinessException</code> with the specified detail message and cause.
     * @param msg the detail message.
     * @param cause the cause of the exception
     */
    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    /**
     * Constructs an instance of <code>BusinessException</code> with the cause.
     * @param cause the cause of the exception
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }
    /**
     * Constructs an instance of <code>BusinessException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
