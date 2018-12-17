/**
 * V2_2-pims-web org.pimslims.utils.sequenator Encrypter.java
 * 
 * @author Petr
 * @date 9 Nov 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.sec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

/**
 * Encrypter
 * 
 */

@Deprecated
class Encryptor {

    Cipher cipher;

    /**
     * Constructor for Encrypter
     * 
     * @throws InvalidKeyException
     */
    public Encryptor(final SecretKey key, final Cipher cipher) throws InvalidKeyException {
        this.cipher = cipher;
        // Initialize the cipher for encryption
        this.cipher.init(Cipher.ENCRYPT_MODE, key);

    }

    /**
     * Good for small files which fit into memory Encryptor.encrypt
     * 
     * @param content
     * @return
     */
    public byte[] encrypt(final byte[] content) {
        byte[] ciphertext;
        try {
            ciphertext = this.cipher.doFinal(content);
        } catch (final IllegalBlockSizeException e) {
            //Should not happen
            throw new RuntimeException(e);
        } catch (final BadPaddingException e) {
            //Should not happen
            throw new RuntimeException(e);
        }

        return ciphertext;
    }

    /**
     * Streaming encryptor to be used for large files Encryptor.encrypt
     * 
     * @param content
     * @param outStream
     * @throws IOException
     */
    public void encrypt(final InputStream content, final OutputStream outStream) throws IOException {

        final byte[] buf = new byte[1024];
        final CipherOutputStream out = new CipherOutputStream(outStream, this.cipher);
        int numRead = 0;
        while ((numRead = content.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();

    }

    public void encrypt(final File planeContent, final File encContent) throws IOException {

        final byte[] buf = new byte[1024];
        final FileInputStream fio = new FileInputStream(planeContent);
        final FileOutputStream fout = new FileOutputStream(encContent);
        final CipherOutputStream out = new CipherOutputStream(fout, this.cipher);
        int numRead = 0;
        while ((numRead = fio.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
        fio.close();
        fout.close();
    }

}
