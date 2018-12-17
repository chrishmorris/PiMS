package uk.ac.ox.oppf.www.wsplate.userdb;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Hibernate;
import org.pimslims.user.userdb.dao.RoleDAO;
import org.pimslims.user.userdb.dao.UserDAO;
import org.pimslims.user.userdb.dto.Role;
import org.pimslims.user.userdb.dto.User;

/**
 * Utility class to provider user details
 * 
 * @author Jon Diprose
 */
public class UserInfoProvider {
	
    private static EntityManagerFactory emf = null;;
    
    public static void setEmf(EntityManagerFactory emf) {
        UserInfoProvider.emf = emf;
    }
    public static EntityManagerFactory getEmf() {
        return UserInfoProvider.emf;
    }

	/**
	 * Zero-arg constructor
	 *
	 */
	public UserInfoProvider() {
		// Empty Constructor
	}
	
	/**
	 * <p>Get the User object for the specified username.</p>
	 * 
	 * @param username - the username of the user to find
	 * @return The User with the specified username
	 */
	public User getUserByUsername(String username) {
		
		// Get an EntityManager
	    EntityManager em = emf.createEntityManager();
	    
		// Get a UserDAO
		UserDAO userDAO = new UserDAO();
		
		// TODO Does this throw an exception if not found?
		User user = userDAO.findByUsername(username, true, em);
		
        // Close the EntityManager
        em.close();
        
		// Return the user we found
		return user;
		
	}
	
	/**
	 * <p>Get the User object for the specified userId.</p>
	 * 
	 * @param userId - the identifier of the user to find
	 * @return The User with the specified userId
	 */
	public User getUserByUserid(Long userId) {
		
        // Get an EntityManager
        EntityManager em = emf.createEntityManager();
        
		// Get a UserDAO
		UserDAO userDAO = new UserDAO();
		
		// TODO Does this throw an exception if not found?
		User user = userDAO.findById(userId, em);
		
        // Close the EntityManager
        em.close();
        
		// Return the user we found
		return user;
		
	}
	
	/**
	 * <p>Get all the Users.</p>
	 * 
	 * @return The Users
	 */
	public Collection<User> getUsers() {
		
        // Get an EntityManager
        EntityManager em = emf.createEntityManager();
        
		// Get a RoleDAO
        RoleDAO roleDAO = new RoleDAO();
        
        // Find the "xtaluser" Role
        // TODO What happens if its not found?
        Role xtalUserRole = roleDAO.findByRole("xtaluser", em);
        
        // Get the set of Users for this Role
        Collection<User> users = xtalUserRole.getUsers();
        
        // Force initialization
        Hibernate.initialize(users);
        
        // Close the EntityManager
        em.close();
        
        // Return the set of Users for this Role
        return users;
                
	}
	
	/**
	 * <p>Get all the group heads as a Collection of Users.</p>
	 * 
	 * @return The group heads as a Collection of Users
	 */
	public Collection<User> getGroupHeads() {
		
        // Get an EntityManager
        EntityManager em = emf.createEntityManager();
        
		// Get a RoleDAO
        RoleDAO roleDAO = new RoleDAO();
        
        // Find the "grouphead" Role
        // TODO What happens if its not found?
        Role groupHeadRole = roleDAO.findByRole("grouphead", em);
        
        // Get the set of Users for this Role
        Collection<User> users = groupHeadRole.getUsers();
        
        // Force initialization
        Hibernate.initialize(users);
        
        // Close the EntityManager
        em.close();
        
        // Return the set of Users for this Role
        return users;
                
	}
	
	/**
	 * <p>Check if the user specified by userId is a member
	 * of role localuser.</p>
	 * 
	 * @param userId the identifier of the user to check
	 * @return True if a member of localuser, otherwise false
	 */
	public boolean isLocalUser(Long userId) {
		
        // Get an EntityManager
        EntityManager em = emf.createEntityManager();
        
		// Really I just want to do select 1 from userrole, roles where userrole.roleid = roles.roleid and role = 'localuser' and userrole.userid = ?
		
		// Declare the return value
		boolean localUser = false;
		
		// Find the user
		UserDAO userDAO = new UserDAO();
		User user = userDAO.findById(userId, em);
		if (null != user) {
			
			// Find the user's roles
			Collection<Role> roles = user.getRoles();
			if (null != roles) {
				
		        // Force initialization
		        Hibernate.initialize(roles);
		        
				// Find the localuser role
				RoleDAO roleDAO = new RoleDAO();
		        Role localRole = roleDAO.findByRole("localuser", em);
				if (null != localRole) {
					
			        // Check if locaRole is in roles
					localUser = roles.contains(localRole);
					
				}
				
			}
			
		}
		
        // Close the EntityManager
        em.close();
        
		// Return true if the user is a member of localuser
		return localUser;
		
	}
	
	/**
	 * <p>Produce a stringified version of User. User should really
	 * override toString()!</p>
	 * 
	 * @param user the User to stringify
	 * @return The stringified user
	 */
	public static String stringify(User user) {
		return "[" + user.getClass().getName() +
		    ":userid=" + user.getUserId().toString() +
		    ";username=" + user.getUsername() +
		    ";firstName=" + user.getFirstName() +
		    ";surname=" + user.getSurname() +
		    ";email=" + user.getEmail() +
		    "]";
	}
	
}
