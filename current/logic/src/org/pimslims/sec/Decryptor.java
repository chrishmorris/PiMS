/**
 * current-pims-web org.pimslims.data Decryptor.java
 * 
 * @author Petr
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

/**
 * Decryptor
 * 
 */
@Deprecated
// this work was not finished
class Decryptor {

    Cipher cipher;

    /**
     * Constructor for Decryptor
     * 
     * @throws InvalidKeyException
     */
    public Decryptor(final SecretKey key, final Cipher cipher) throws InvalidKeyException {
        this.cipher = cipher;
        // Initialize the same cipher for decryption
        this.cipher.init(Cipher.DECRYPT_MODE, key);
    }

    public byte[] decrypt(final byte[] content) throws IllegalBlockSizeException {
        // Decrypt the ciphertext
        byte[] cleartext1;
        try {
            cleartext1 = this.cipher.doFinal(content);
        } catch (final BadPaddingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
        return cleartext1;
    }

    public void decrypt(final InputStream content, final OutputStream outStream) throws IOException {
        // Bytes read from in will be decrypted
        final CipherInputStream in = new CipherInputStream(content, this.cipher);
        final byte[] buf = new byte[1024];

        // Read in the decrypted bytes and write the cleartext to out
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            outStream.write(buf, 0, numRead);
        }
        in.close();
    }

    public void decrypt(final File encContent, final File planeContent) throws IOException {
        // Bytes read from in will be decrypted
        final FileInputStream fio = new FileInputStream(encContent);
        final FileOutputStream fout = new FileOutputStream(planeContent);

        final CipherInputStream in = new CipherInputStream(fio, this.cipher);
        final byte[] buf = new byte[1024];

        // Read in the decrypted bytes and write the cleartext to out
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            fout.write(buf, 0, numRead);
        }
        in.close();
        fio.close();
        fout.close();
    }

}
