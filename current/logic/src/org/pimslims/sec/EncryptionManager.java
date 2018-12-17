/**
 * current-pims-web org.pimslims.data EncryptionManager.java
 * 
 * @author Petr Troshin aka pvt43
 * @date 5 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.sec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * EncryptionManager
 * 
 */
public class EncryptionManager {

    private static final String KEY_PASS = "test";

    private static final String KEY_ALIAS = "sequencing";

    /*
     * TODO move to wsgen.xml as Ant task! 
     */
    public static void main(final String[] args) throws NoSuchAlgorithmException, KeyStoreException,
        CertificateException, IOException {
        EncryptionManager.makeKeyAndKeyStore("c:\\test.ks");
    }

    public static final void makeKeyAndKeyStore(final String keyStore) throws NoSuchAlgorithmException,
        KeyStoreException, CertificateException, IOException {
        final File keyStoreFile = new File(keyStore);
        final FileOutputStream fos = new FileOutputStream(keyStoreFile);
        final KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null);
        final KeyGenerator keygen = KeyGenerator.getInstance("AES");

        final SecretKey key = keygen.generateKey();

        ks.setKeyEntry(EncryptionManager.KEY_ALIAS, key, EncryptionManager.KEY_PASS.toCharArray(), null);
        ks.store(fos, EncryptionManager.KEY_PASS.toCharArray());
        fos.close();

    }

    @Deprecated
    private Encryptor encryptor;

    private Decryptor decryptor;

    private SecretKey key;

    /**
     * Constructor for EncryptionManager
     */
    @Deprecated
    public EncryptionManager(final InputStream keyStore) {

        try {
            final KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(keyStore, EncryptionManager.KEY_PASS.toCharArray());

            this.key =
                (SecretKey) ks.getKey(EncryptionManager.KEY_ALIAS, EncryptionManager.KEY_PASS.toCharArray());

            final Cipher aesEncCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final Cipher aesDecCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            this.encryptor = new Encryptor(this.key, aesEncCipher);
            this.decryptor = new Decryptor(this.key, aesDecCipher);

        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (final CertificateException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor for EncryptionManager
     * 
     * @throws FileNotFoundException
     * 
     * @throws FileNotFoundException
     */
    @Deprecated
    public EncryptionManager(final String keyStore) throws FileNotFoundException {
        this(new FileInputStream(keyStore));
    }

    private byte[] encrypt(final byte[] content) {
        final byte[] enContent = this.encryptor.encrypt(content);
        return enContent;
    }

    /**
     * Verify the file agains digest
     * 
     * EncryptionManager.isFileComplete
     * 
     * @param digest
     * @param file
     * @return
     * @throws IOException
     */
    public boolean isFileComplete(final byte[] digest, final File file) throws IOException {
        final byte[] fdigest = this.getDigest(file);
        if (Arrays.equals(digest, fdigest)) {
            return true;
        }
        return false;
    }

    /**
     * EncryptionManager.getDigest
     * 
     * @param content
     * @return
     */
    public byte[] getDigest(final byte[] content) {
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(this.key);
        } catch (final InvalidKeyException e) {
            // should not happen
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            // should not happen
            throw new RuntimeException(e);
        }

        mac.update(content);
        final byte[] macbytes = mac.doFinal();
        return macbytes;
    }

    public byte[] getDigest(final File file) throws IOException {
        Mac mac = null;
        FileInputStream fis = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(this.key);

            fis = new FileInputStream(file);
            final byte[] dataBytes = new byte[1024];
            int nread = fis.read(dataBytes);
            while (nread > 0) {
                mac.update(dataBytes, 0, nread);
                nread = fis.read(dataBytes);
            }
            fis.close();
        } catch (final InvalidKeyException e) {
            // should not happen
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            // should not happen
            throw new RuntimeException(e);
        } finally {
            fis.close();
        }
        return mac.doFinal();
    }

    private void encrypt(final InputStream content, final OutputStream outStream) throws IOException {
        this.encryptor.encrypt(content, outStream);
    }

    @Deprecated
    public void encrypt(final File planeContent, final File encContent) throws IOException {
        this.encryptor.encrypt(planeContent, encContent);
    }

    public byte[] decrypt(final byte[] content) {
        try {
            return this.decryptor.decrypt(content);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public void decrypt(final InputStream content, final OutputStream outStream) throws IOException {
        this.decryptor.decrypt(content, outStream);
    }

    public void decrypt(final File encContent, final File planeContent) throws IOException {
        this.decryptor.decrypt(encContent, planeContent);
    }
}
