/**
 * IspybRest2 org.pimslims.ispyb ConnectionTest.java
 * 
 * @author cm65
 * @date 30 Apr 2012
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ispyb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;

/**
 * ConnectionTest
 * 
 */
public class ConnectionTest {

    public static void main(String[] args) {
        try {
            System.out.println("http.proxyHost: " + System.getProperty("http.proxyHost"));
            System.out.println("http.proxyPort: " + System.getProperty("http.proxyPort"));
			java.net.URL url = new java.net.URL("https://ispyb.diamond.ac.uk/");
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getMessage().startsWith("Server returned HTTP response code:")) {
                System.exit(0); // connection made
            }
            System.exit(1);
        }
    }

}
