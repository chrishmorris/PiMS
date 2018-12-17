/*
 * PersonService.java
 *
 * Created on 17 April 2007, 09:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.service;

import org.pimslims.business.BaseService;
import java.util.Collection;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;

/**
 * This interface describes the interaction with the database to extract and import Person information
 * 
 * @author IMB
 */
public interface PersonService extends BaseService {
    /**
     * Get information on a specific user.
     * 
     * 
     * @return information about user (name, surname, current group and institute, start and end date, active profile, email, tel)
     * @param userId The Person Reference
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public Person find (long userId) throws BusinessException;
    
    /**
     * 
     * @param username
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Person findByUsername(String username) throws BusinessException;
    
    /**
     * Finds all users.
     * @param paging so that paging can be done
     * @return the list of all users.
     * @throws org.pimslims.business.exception.BusinessException on error
     */
    public Collection<Person> findAll(BusinessCriteria criteria) throws BusinessException;    
      
    /**
     * This will create a user in the local database (not the User Management db)
     * so only certain fields will be used.  The act of creating a user in the User
     * Management database is up to the user management app really....
     * This function is only ever likely to be called when the user logs into the system
     * for the first time and the local user account details are set up.
     *
     * @param user information (name, surname, group, institute, start date, email, tel) - we have to be sure that the institute and the group already exists in the DB.
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void create(Person user) throws BusinessException;
        
    /**
     * This will update a user in the local database (not the User Management db)
     * so only certain fields will be used.  The act of creating a user in the User
     * Management database is up to the user management app really....
     * This function is only ever likely to be called when the user logs into the system
     * for the first time and the local user account details are set up.
     * 
     * @param user 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void update (Person user) throws BusinessException;
    
    /**
     * This would make a user no longer active / enabled.
     * Really this is the realm of the User Management app which would 
     * stop the user logging in to any resource such as PIMS.
     * LATER: Is this ever likely to get implemented?
     * @param user 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void close (Person user) throws BusinessException;
}
