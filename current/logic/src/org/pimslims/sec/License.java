/**
 * V5_0-web org.pimslims.sec License.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Properties;

/**
 * License
 * 
 * To sign a license file: unix2dos license.txt; openssl dgst -sha1 -sign privkey.pem -out license.sig
 * license.txt
 * This command might be "todos" on your platform.
 * 
 * To check one: openssl dgst -sha1 -verify pubkey.pem -signature license.sig license.txt
 */
public class License {

    private static final BigInteger PUBLIC_EXPONENT = new BigInteger("10001", 16);

    private static final BigInteger MODULUS = new BigInteger("00cc35a4db8020bf46728ed3a7e198"
        + "6a728b26556d66d392d5a6448fcdaf" + "698bb16ef8a4c3c202f3ffda05a8b0"
        + "6e1c273955f64a9ce1dc7e785659d1" + "6d781be8fab349f812be3469b2f43d"
        + "f0047e88438d5af10327a7041d2d67" + "e697ec7973ff0265a23223d717bdf6"
        + "3588bfa8bdd3d76ecf4a4d26079707" + "6034962494d51a8b70b05f0e8fdc4f"
        + "58691bd8c9c030749336b7845360bc" + "1896758086e45ec50ee120aad713a7"
        + "196ac23adb7441e124b5720f62989d" + "1bcd006aa32b298f4d72e4ae55d53f"
        + "252e4f4e2787750cf752ea1e92e8e1" + "fce822b02c4026067416789bef1993"
        + "2bbf971be302c81480276ae857d00c" + "91831e62a55b116267109c2c92ed7a" + "6695", 16);

    /**
     * License.getLicense
     * 
     * @return
     */
    public static License getLicense() throws IOException {
        try {
            final byte[] data = getResource("license.txt");
            // Restore this to add security License.verify(data);
            return new License(data);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException("license.txt or license.sig not found", e);
        }
    }
    
    private static byte[] getResource(String name) throws IOException { 
    	InputStream inputStream = License.class.getResourceAsStream("/"+name);
    	assert null!=inputStream: "File not found in class path: "+name;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int readBytes = inputStream.read(); readBytes >= 0; readBytes = inputStream.read()) {
        	outputStream.write(readBytes);
        }
        byte[] byteData = outputStream.toByteArray();
        inputStream.close();    
        outputStream.close();     
        return byteData;
     }

    static void verify(final byte[] data) throws FileNotFoundException, IOException, AssertionError {
        /* restore this to add security final boolean ok =
            License.doVerifySignature(data, License.getResource("license.sig"),
                License.getPublicKey(License.MODULUS, License.PUBLIC_EXPONENT));
        if (!ok) {
            throw new AssertionError("Please update your PiMS license");
        } */
    }

    private static byte[] read(final File file) throws FileNotFoundException, IOException {
        DataInputStream dataIs = null;
        final byte[] data = new byte[(int) file.length()];
        dataIs = new DataInputStream(new FileInputStream(file));
        dataIs.readFully(data);
        //TODO use regexp to enforce \r\n as line endings
        dataIs.close();
        return data;
    }

    /**
     * License.getPublicKey
     * 
     * @return
     */
    static PublicKey getPublicKey(final BigInteger modulus, final BigInteger publicExponent) {
        try {
            return KeyFactory.getInstance("RSA")
                .generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static final boolean doVerifySignature(final byte[] message, final byte[] signature, final PublicKey key) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            final Signature sig = Signature.getInstance("SHA1withRSA", "BC");
            sig.initVerify(key);
            sig.update(message);
            return sig.verify(signature);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (final SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    private final Properties properties = new Properties();

    /**
     * Constructor for License
     */
    public License(final byte[] properties) throws IOException {
        super();
        final InputStream is = new ByteArrayInputStream(properties);
        this.properties.load(is);
    }

    /**
     * License.get
     * 
     * @param string
     * @return
     */
    public String get(final String keyword) {
        return this.properties.getProperty(keyword);
    }

    /**
     * License.getMaxUsers
     * 
     * @return
     */
    public int getMaxUsers() {
        final String maxUsers = this.get("maxUsers");
        if (null == maxUsers) {
            return -1;
        }
        return Integer.parseInt(maxUsers);
    }

}
