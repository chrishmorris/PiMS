/**
 * current-pims-web org.pimslims.bioinf MatrixLoader.java
 * 
 * @author pvt43
 * @date 2 Jun 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 pvt43   * 
    * 
  
  */
package org.pimslims.bioinf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * MatrixLoader
 * 
 */
public class MatrixLoader {

    /**
     * The path to the matrices within the package.
     */
    private static final String MATRICES_PATH = "org/pimslims/bioinf/util/matrices/";

    /**
	 * Loads scoring matrix by its name from Jar file or file system.
	 * 
	 * @param matrix
	 *            to load
	 * @throws IOException
	 */
	public static String load(final String matrix) throws IOException {
		final InputStream is = MatrixLoader.class.getClassLoader()
				.getResourceAsStream(MatrixLoader.MATRICES_PATH + matrix);
		assert null != is : "Unknown substitution matrix: " + matrix;
		final StringBuilder ret = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		String line = br.readLine();
		while (null != line) {
			ret.append(line);
			ret.append("\n");
			line = br.readLine();
		}
		return ret.toString();
    }

    /**
     * Returns a list of the scoring matrices from the matrices home directory
     */
    public static Collection<String> list() {

        final ArrayList<String> matrices = new ArrayList<String>();
        final URL url = MatrixLoader.class.getClassLoader().getResource(MatrixLoader.MATRICES_PATH);
        // Load from file system
        final String home = url.getFile();
        //  System.out.println(home);
        final File dir = new File(home);
        final String files[] = dir.list();

        for (int i = 0, n = files.length; i < n; i++) {
            final File file = new File(home + files[i]);

            if (file.isFile() && file.canRead()) {
                matrices.add(file.getName());
            }
        }

        Collections.sort(matrices);
        return matrices;
    }

}
