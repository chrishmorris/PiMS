/**
 * V3_3-web org.pimslims.presentation SecurityTokenManager.java
 * 
 * @author cm65
 * @date 7 Jan 2010
 * 
 * Protein Information Management System
 * @version: 3.2
 * 
 * Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * SecurityTokenManagerTest
 * 
 * These tests check the basic functionality of SecurityTokenManager. They do not confirm that it is secure.
 * Only a code review can do that.
 * 
 */
public class SecurityTokenManagerTest extends TestCase {

    public void testCreate() {
        final SecurityTokenManager manager = new SecurityTokenManager();
        final String token1 = manager.createToken();
        Assert.assertNotNull(token1);
        final String token2 = manager.createToken();
        Assert.assertFalse(token2.equals(token1));
        //System.out.println(token2);
    }

    public void testCheck() {
        final SecurityTokenManager manager = new SecurityTokenManager();
        final String token1 = manager.createToken();
        Assert.assertFalse(manager.checkToken("nonesuch"));
        Assert.assertTrue(manager.checkToken(token1));
        Assert.assertFalse("token can only be used once", manager.checkToken(token1));
    }

}
