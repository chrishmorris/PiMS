/**
 * 
 */
package uk.ac.ox.oppf.www.wsplate.userdb;

/**
 * General exception for failure to obtain user info
 * 
 * @author Jon Diprose
 */
public class UserInfoException extends Exception {

	/**
	 * Code to satisy Serializable interface
	 */
	private static final long serialVersionUID = -3389327141763985341L;

	/**
	 * Zero-arg constructor
	 */
	public UserInfoException() {
		super();
	}

	/**
	 * Construct an UserInfoException with the specified message
	 * 
	 * @param message
	 */
	public UserInfoException(String message) {
		super(message);
	}

	/**
	 * Construct an UserInfoException with the specified message
	 * that wraps the specified Throwable
	 * 
	 * @param message
	 * @param throwable
	 */
	public UserInfoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Construct an UserInfoException that wraps the specified Throwable
	 * 
	 * @param throwable
	 */
	public UserInfoException(Throwable throwable) {
		super(throwable);
	}

}
