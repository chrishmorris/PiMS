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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * SecurityTokenManager
 * 
 * Generates and checks unguessable tokens. This uses two sources of unpredictability: the
 * java.security.SecureRandom class, and strings provided by the caller.
 * 
 */
public class SecurityTokenManager {

    private final SecureRandom random;

    private final Set<String> tokens = new HashSet<String>();

    /**
     * Constructor for SecurityTokenManager
     * 
     * @param seed A starting seed - preferably both unique and hard for an attacker to guess
     */
    public SecurityTokenManager() {
        try {
            this.random = SecureRandom.getInstance("SHA1PRNG");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * SecurityTokenManager.createToken
     * 
     * @param seed a string, the security does not depend on it being unguessable
     * @return
     */
    public String createToken() {
        final StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < sb.capacity(); i++) {
            final int index = Math.abs(this.random.nextInt()) % 61;
            final char ch = (char) (index + 1 + '"'); // all these are printable
            sb.append(ch);
        }
        final String token = sb.toString(); //new Long(this.random.nextLong()).toString(); 
        this.tokens.add(token);
        return token;
    }

    /**
     * SecurityTokenManager.checkToken
     * 
     * @param string
     * @return true if the token was issued, and has not yet been used
     */
    public boolean checkToken(final String token) {
        if (!this.tokens.contains(token)) {
            return false;
        }
        this.tokens.remove(token);
        return true;
    }

}
