/**
 * current-pims-web org.pimslims.servlet.plateExperiment SpreadsheetGetter.java
 * 
 * @author cm65
 * @date 16 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3 <<<<<<< .mine Copyright (c) 2007 cm65 *
 * 
 * 
 *           ======= Copyright (c) 2007 cm65 *
 * 
 *           >>>>>>> .r16921
 */
package org.pimslims.servlet.plateExperiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.pimslims.csv.CsvParser;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.servlet.ListFiles;

/**
 * SpreadsheetGetter
 * 
 */
public final class SpreadsheetGetter {

    private SpreadsheetGetter() {
        super();
        // provides static methods only
    }

    public static String parseISToString(final java.io.InputStream is) throws IOException {

        final BufferedReader d = new BufferedReader(new InputStreamReader(is));
        final StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = d.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    /**
     * @param request
     * @param parms
     * @return
     * @throws ServletException
     * @throws IOException TODO caller must delete temporary files
     */
    static Map<String, String> getSpreadsheet(final HttpServletRequest request,
        final Map<String, String> parms) throws ServletException, IOException {

        final Map<String, String> uploadFiles = new HashMap<String, String>();
        final DiskFileUpload upload = new DiskFileUpload();
        // Set upload parameters
        // TODO upload.setSizeThreshold(yourMaxMemorySize);
        upload.setSizeMax(ListFiles.MAX_UPLOAD);
        // TODO upload.setRepositoryPath(yourTempDirectory);

        // Parse the request
        // Upload from a spreadsheet stored as string so it can be parsed more
        // than once

        try {
            final java.util.Collection<FileItem> items = upload.parseRequest(request);

            // Process the uploaded items
            // java.util.Iterator iter = items.iterator();
            // while (iter.hasNext()) {
            // FileItem item = (FileItem) iter.next();
            for (final FileItem item : items) {
                if (item.isFormField()) {
                    parms.put(item.getFieldName(), item.getString());
                    continue;
                }
                if (0 == item.getName().trim().length()) {
                    continue;
                }

                if (0 == item.getSize()) {
                    throw new ServletException("File is of size 0");
                }

                uploadFiles.put(item.getName(), SpreadsheetGetter.parseISToString(item.getInputStream()));
            }

        } catch (final FileUploadException e1) {
            throw new ServletException(e1);
        }
        return uploadFiles;
    }

    public static List<String> getWellsFromSpreadSheet(final Reader reader) throws IOException,
        ConstraintException {

        final CsvParser parser = new CsvParser(reader);
        final List<String> labels = new ArrayList<String>(Arrays.asList(parser.getLabels()));
        final List<String> wells = new ArrayList<String>();

        // process standard column headers
        String wellName = null;
        if (labels.contains("Well")) {
            wellName = "Well";
            labels.remove("Well");
        } else if (labels.contains("well")) {
            wellName = "well";
            labels.remove("well");
        } else {
            throw new IllegalArgumentException("CSV file must contain 'Well' column");
            // LATER accept "row" and "column"
        }

        // now iterate through the file
        while (parser.getLine() != null) {
            final String well = parser.getValueByLabel(wellName);
            wells.add(HolderFactory.getPositionForName(well));
        }

        return wells;
    }

    public static Map<String, String> getHoldersFromSpreadSheet(final Reader reader,
        final HolderType holderType) throws IOException, ConstraintException {

        final CsvParser parser = new CsvParser(reader);
        final List<String> labels = new ArrayList<String>(Arrays.asList(parser.getLabels()));
        final Map<String, String> plateIds = new HashMap<String, String>();

        // process standard column headers
        String plateIdName = null;
        if (labels.contains("PlateId")) {
            plateIdName = "PlateId";
        } else {
            return Collections.EMPTY_MAP;
        }
        String wellName = null;
        if (labels.contains("Well")) {
            wellName = "Well";
            labels.remove("Well");
        } else if (labels.contains("well")) {
            wellName = "well";
            labels.remove("well");
        } else {
            throw new IllegalArgumentException("CSV file must contain 'Well' column");
            // LATER accept "row" and "column"
        }

        // now iterate through the file
        while (parser.getLine() != null) {
            final String plateId = parser.getValueByLabel(plateIdName);
            if (ServletUtil.validString(plateId) && !plateIds.containsKey(plateId)) {

                final String well = parser.getValueByLabel(wellName);
                plateIds.put(plateId, HolderFactory.getHolderPosition(holderType,
                    (HolderFactory.getRow(well) + 1), (HolderFactory.getColumn(well) + 1)));
            }
        }

        return plateIds;
    }

    public static List<String> getlabelsFromSpreadSheet(final Reader reader) throws IOException,
        ConstraintException {

        final CsvParser parser = new CsvParser(reader);
        final String[] labels = parser.getLabels();
        final List<String> labelList = new ArrayList<String>();
        for (int i = 0; i < labels.length; i++) {
            if (ServletUtil.validString(labels[i])) {
                labelList.add(labels[i]);
            }
        }
        return labelList;
    }

}
