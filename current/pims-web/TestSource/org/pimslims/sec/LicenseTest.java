/**
 * V5_0-web org.pimslims.sec LicenseTest.java
 * 
 * @author cm65
 * @date 20 Nov 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.sec;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * LicenseTest
 * 
 */
public class LicenseTest extends TestCase {

    private static final String UNIQUE = "L" + System.currentTimeMillis();

    /**
     * Constructor for LicenseTest
     * 
     * @param name
     */
    public LicenseTest(final String name) {
        super(name);
    }

    public void test() throws IOException {
        final License license = License.getLicense();
        Assert.assertEquals("4.4.0", license.get("version"));
    }

    public void testVerifyBad() throws FileNotFoundException, IOException, AssertionError {

        try {
            License.verify(LicenseTest.UNIQUE.getBytes("utf8"));
            Assert.fail("bad license accepted");
        } catch (final AssertionError e) {
            // that's right;
        }
    }

    public void testVerifyGood() throws UnsupportedEncodingException, NoSuchAlgorithmException,
        NoSuchProviderException, InvalidKeyException, SignatureException {
        final KeyPair keyPair = this.getKeyPair();

        final byte[] message = "UNIQUE".getBytes();
        final PrivateKey privateKey = keyPair.getPrivate();
        final byte[] sigBytes = this.sign(message, privateKey);

        final RSAPublicKey rsaPublic = (RSAPublicKey) keyPair.getPublic();

        final PublicKey publicKey =
            License.getPublicKey(rsaPublic.getModulus(), rsaPublic.getPublicExponent());
        final boolean ok = License.doVerifySignature(message, sigBytes, publicKey);
        Assert.assertTrue(ok);
    }

    private byte[] sign(final byte[] message, final PrivateKey privateKey) throws NoSuchAlgorithmException,
        NoSuchProviderException, InvalidKeyException, SignatureException {
        final Signature signature = Signature.getInstance("SHA1withRSA", "BC");
        signature.initSign(privateKey, new SecureRandom());
        signature.update(message);
        final byte[] sigBytes = signature.sign();
        return sigBytes;
    }

    private KeyPair getKeyPair() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(512, new SecureRandom());
            final KeyPair keyPair = keyGen.generateKeyPair();
            return keyPair;
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

}
