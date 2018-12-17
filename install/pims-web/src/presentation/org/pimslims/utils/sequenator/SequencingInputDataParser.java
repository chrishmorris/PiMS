/**
 * V2_2-pims-web org.pimslims.utils.sequenator SequencingInputDataParser.java
 * 
 * @author Petr
 * @date 7 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.management.InvalidAttributeValueException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.utils.ExcelConnector;
import org.pimslims.utils.ExcelConnectorImpl;
import org.pimslims.utils.sequenator.SequencingOrderWriter.SequencingOrderBean;

/**
 * SequencingInputDataParser
 * 
 */
public class SequencingInputDataParser {

    final ExcelConnector connector;

    protected final HSSFWorkbook workbook;

    protected final HSSFSheet sheet;

    /**
     * The name of the plate setup template
     * 
     * private static String USER_INPUT_TEMPLATE = "UserInput.xlt";
     */

    public static final String[] header = new String[] { "Well", "Sample Name", "Primer Name", "Premix",
        "Template", "Primer", "Water", "Total Vol." };

    /* This should match exactly with data/protocols/Leeds_SpecificUI_Sequencing_Order 
    
    static final String PiName = "Principal Investigator";

    

    static final String description = "Template Description";

    static final String tlenght = "Template Length";

    static final String tconcentration = "Template Concentration";

    static final String tvolume = "Template Volume";

    static final String primerConcentration = "Primer Concentration";

    static final String primerVolume = "Primer Volume";

    static final String userName = "User Name";

    static final String userEmail = "Email";

    static final String userPhone = "Phone";

    static final String userDepartment = "Department";

    static final String returnSample = "Return sample?";
    */

    /* These must match Excel template values EXACTLY! */
    static final String userName = "NAME:";

    static final String userEmail = "E-MAIL:";

    static final String userPhone = "TEL. NO.:";

    static final String userDepartment = "DEPARTMENT:";

    static final String accountNumber = "Account Number: (Compulsory Entry)";

    static final String cdProvided = "CD PROVIDED (Y/N):";

    static final String returnSample = "EXCESS SAMPLE RETURN (Y/N):";

    WritableVersion rw;

    public static final String orderParamName = "Order ID";

    /**
     * Constructor for SequencingInputDataParser
     * 
     * @param filerName
     * @param fieldNames
     * @throws FileNotFoundException public SequencingInputDataParser(final WritableVersion rw, final String
     *             fileName) throws FileNotFoundException { try {
     * 
     *             this.connector = new ExcelConnectorImpl();
     * 
     *             final InputStream is =
     *             SequencePrepSheet.class.getResourceAsStream(SequencingInputDataParser.USER_INPUT_TEMPLATE);
     *             assert (null != is) : "UserInput Template is not found ";
     * 
     *             this.workbook = this.connector.readTemplate(is); this.sheet = this.workbook.getSheetAt(0);
     *             // TODO if (!this.isValidUserInputTemplate()) { throw new IOException("Wrong template!"); }
     *             } catch (final IOException e) { throw new RuntimeException(e); }
     * 
     *             this.rw = rw; }
     */

    public SequencingInputDataParser(final WritableVersion rw, final InputStream fileData)
        throws FileNotFoundException {
        try {

            this.connector = new ExcelConnectorImpl();
            assert (null != fileData) : "UserInput Template is not found ";

            this.workbook = this.connector.readTemplate(fileData);
            this.sheet = this.workbook.getSheetAt(0);
            /* could
            if (!this.isValidUserInputTemplate()) {
                throw new IOException("Wrong template!");
            }*/
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        this.rw = rw;
    }

    public boolean validateData() throws FileNotFoundException, InvalidAttributeValueException,
        AccessException, ConstraintException, AbortedException, AddressException {

        final HSSFCell cName = this.connector.getCell(SequencingInputDataParser.userName, this.sheet);
        final String nameVal = this.connector.getNextCell(cName).getRichStringCellValue().getString();

        final HSSFCell cDep = this.connector.getCell(SequencingInputDataParser.userDepartment, this.sheet);
        //final String depVal = 
        this.connector.getNextCell(cDep).getRichStringCellValue().getString();

        final HSSFCell cTel = this.connector.getCell(SequencingInputDataParser.userPhone, this.sheet);
        //String telVal = null;
        try {
            new Long((long) (this.connector.getNextCell(cTel).getNumericCellValue())).toString();
        } catch (final IllegalStateException e) {
            this.connector.getNextCell(cTel).getRichStringCellValue().getString();
        }

        final HSSFCell cEmail = this.connector.getCell(SequencingInputDataParser.userEmail, this.sheet);
        final String emailVal = this.connector.getNextCell(cEmail).getRichStringCellValue().getString();

        final HSSFCell cAccount = this.connector.getCell(SequencingInputDataParser.accountNumber, this.sheet);
        //TODO make sure that purely numeric values are accepted!
        final String accountVal = this.connector.getNextCell(cAccount).getRichStringCellValue().getString();

        final HSSFCell cPI =
            this.connector.getCellAt(cAccount.getRowIndex(), cAccount.getColumnIndex() + 3, this.sheet);

        final String PIName = cPI.getRichStringCellValue().getString();

        if (Util.isEmpty(PIName)) {
            throw new InvalidAttributeValueException("PI name cannot be empty");
        }
        if (Util.isEmpty(accountVal)) {
            throw new InvalidAttributeValueException("Account number cannot be empty");
        }
        if (Util.isEmpty(nameVal)) {
            throw new InvalidAttributeValueException("User name cannot be empty");
        }
        final InternetAddress userMailAddr = new InternetAddress(emailVal);
        userMailAddr.validate();

        if (Util.isEmpty(emailVal)) {
            throw new InvalidAttributeValueException("User email cannot be empty");
        }
        final HSSFRow dataRow =
            this.sheet.getRow(this.connector.getRow("Sample Ref. No.", this.sheet).getRowNum() + 3);

        final int startRowIdx = dataRow.getRowNum();
        final int endRowIdx =
            this.connector.getRow(SequencingInputDataParser.cdProvided, this.sheet).getRowNum();
        int sampleCounter = 0;
        for (int i = startRowIdx; i < endRowIdx - 1; i++) {
            //set parameter values from file
            //get values from file
            sampleCounter++;
            final HSSFRow row = this.sheet.getRow(i);

            final String tname = row.getCell(1).getRichStringCellValue().getString();
            final String pname = row.getCell(7).getRichStringCellValue().getString();
            if (this.isEndRiched(tname, pname)) {
                // assume further only the empty rows
                break;
            }
            if (sampleCounter > 96) {
                throw new InvalidAttributeValueException(
                    "The maximum number of samples in one submission is 96. Please complete another spreadsheet if you have more samples.");
            }

            final double tconc = row.getCell(4).getNumericCellValue();
            final double pconc = row.getCell(8).getNumericCellValue();

            if (Util.isEmpty(tname)) {
                throw new InvalidAttributeValueException("Template name cannot be empty");
            }
            if (tconc == 0) {
                throw new InvalidAttributeValueException("Template concentration cannot be empty");
            }
            if (Util.isEmpty(pname)) {
                throw new InvalidAttributeValueException("Primer name cannot be empty");
            }
            if (pconc == 0) {
                throw new InvalidAttributeValueException("Primer concentration cannot be empty");
            }

            // should Validate that pname+tname+usersurname is unique and has not be recored as experiment already
        }

        return true;
    }

    boolean isEndRiched(final String templateName, final String primerName) {
        return Util.isEmpty(templateName) && Util.isEmpty(primerName);
    }

    /**
     * Make sure that prohibited characters are not used or replaced & length of the field is according to
     * requirements
     */
    static String validateValue(String value, final int maxlength) {
        assert !Util.isEmpty(value);
        value = value.trim();
        // Remove dot from the beginning and the end
        if (value.startsWith(".")) {
            value = value.substring(1).trim();
        }
        if (value.endsWith(".")) {
            value = value.substring(0, value.length() - 1).trim();
        }
        // Get rid of any non alphanumeric characters but retain spaces and dots  
        value = value.replaceAll("[^a-zA-Z0-9\\.\\-\\s]+", "");
        // Replace all white spaces with -
        value = value.replaceAll("\\s++", "-");
        // Replace all multiple dots with one 
        value = value.replaceAll("\\.++", ".");

        // Trim to required length
        if (maxlength > 0) {
            if (value.length() > maxlength) {
                value = value.substring(0, maxlength);
            }
        }
        return value;
    }

    public String recordExperiments(final Protocol protocol) throws FileNotFoundException, AccessException,
        ConstraintException, InvalidAttributeValueException {
        final ArrayList<SequencingOrderBean> sblist = new ArrayList<SequencingOrderBean>();
        SequencingOrderBean sb = new SequencingOrderBean();

        /* User details */
        final HSSFCell cName = this.connector.getCell(SequencingInputDataParser.userName, this.sheet);
        sb.userName = this.connector.getNextCell(cName).getRichStringCellValue().getString();

        final HSSFCell cDep = this.connector.getCell(SequencingInputDataParser.userDepartment, this.sheet);
        sb.department = this.connector.getNextCell(cDep).getRichStringCellValue().getString();

        final HSSFCell cTel = this.connector.getCell(SequencingInputDataParser.userPhone, this.sheet);
        try {
            sb.userPhone = this.connector.getNextCell(cTel).getRichStringCellValue().getString();
        } catch (final IllegalStateException e) {
            sb.userPhone = new Long((long) this.connector.getNextCell(cTel).getNumericCellValue()).toString();
        }

        final HSSFCell cEmail = this.connector.getCell(SequencingInputDataParser.userEmail, this.sheet);
        sb.userEmail = this.connector.getNextCell(cEmail).getRichStringCellValue().getString();

        /* PI details */
        final HSSFCell cAccount = this.connector.getCell(SequencingInputDataParser.accountNumber, this.sheet);

        sb.accountNumber = this.connector.getNextCell(cAccount).getRichStringCellValue().getString();
        final HSSFCell cPI =
            this.connector.getCellAt(cAccount.getRowIndex(), cAccount.getColumnIndex() + 3, this.sheet);
        sb.PIName = cPI.getRichStringCellValue().getString();

        sb.cdProvided =
            this.connector
                .getNextCell(this.connector.getCell(SequencingInputDataParser.cdProvided, this.sheet))
                .getRichStringCellValue().getString();

        sb.returnSample =
            this.connector
                .getNextCell(this.connector.getCell(SequencingInputDataParser.returnSample, this.sheet))
                .getRichStringCellValue().getString();

        final HSSFRow dataRow =
            this.sheet.getRow(this.connector.getRow("Sample Ref. No.", this.sheet).getRowNum() + 3);

        final int startRowIdx = dataRow.getRowNum();
        final int endRowIdx =
            this.connector.getRow(SequencingInputDataParser.cdProvided, this.sheet).getRowNum();

        for (int i = startRowIdx; i < endRowIdx - 1; i++) {
            //set parameter values from file
            //get values from file

            final HSSFRow row = this.sheet.getRow(i);

            sb.tName = row.getCell(1).getRichStringCellValue().getString();
            sb.pName = row.getCell(7).getRichStringCellValue().getString();
            if (this.isEndRiched(sb.tName, sb.pName)) {
                // assume further only the empty rows
                break;
            }

            // Template & primer names can only contain  alphanumeric characters & -  
            sb.tName = SequencingInputDataParser.validateValue(sb.tName, 15);
            sb.pName = SequencingInputDataParser.validateValue(sb.pName, 15);
            // COULD accept doubles do not convert
            sb.tConc = new Double(row.getCell(4).getNumericCellValue()).toString();
            sb.tVolume = new Double(row.getCell(5).getNumericCellValue()).toString();
            sb.tLength = new Double(row.getCell(3).getNumericCellValue()).toString();
            sb.tReqReadLength = new Double(row.getCell(6).getNumericCellValue()).toString();
            sb.pConc = new Double(row.getCell(8).getNumericCellValue()).toString();
            sb.pVolume = new Double(row.getCell(9).getNumericCellValue()).toString();
            sb.description = row.getCell(2).getRichStringCellValue().getString();
            sblist.add(sb);
            // Copy the datails set above to a new bean and set the rest of the datails on the 
            // new iteration 
            // TODO redo this.
            sb = sb.getPartCopy();
        }
        final SequencingOrderWriter sw = new SequencingOrderWriter(sblist);
        final String orderId = sw.record(this.rw);
        return orderId;
    }

}
