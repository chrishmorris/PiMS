/*
 * BaseService.java
 *
 * Created on 10 August 2007, 13:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business;

/**
 * This is the base class for storing details of the underlying data store whether that be a database
 * or file.  DataStorage will store the details of the connection / filehandle, whether that be a 
 * Hibernate Session, PIMS Transaction, etc.
 * 
 * It is envisaged that the developer of the implementations would add more meaningful accessors for 
 * their particular implementation requirements 
 * 
 * 
 * @author IMB
 */
public interface BaseService {
    
    /**
     * 
     * @return 
     */
    public DataStorage getDataStorage();

    //public void setDataStorage(DataStorage dataStorage);    
}
