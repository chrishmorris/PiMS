package org.pimslims.data;

/**
 * @author Peter Troshin
 * @date September 2006
 * 
 * Utility to perform import of University of Leeds MS Access database which contains chemicals to the PIMS
 * 
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.pimslims.bioinf.targets.PIMSClassesDescription;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.molecule.Molecule;

@Deprecated
public class LeedsReagentImporter {

    final AbstractModel model;

    WritableVersion rw = null;

    public LeedsReagentImporter(final String dataOwner) {
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    void createReagent() throws ConstraintException, AccessException, ClassNotFoundException {
        final HashMap parms = new HashMap();
        Util.getOrCreate(this.rw, Molecule.class.getName(), "Reference", parms);
    }

    public static void main(final String[] args) throws Exception {
        new LeedsReagentImporter("test");
        Reagent.getReagent();

    }

    /**
     * ValueObject for Leeds reagents
     * 
     * @author Petr Troshin
     */
    static class Reagent {

        static Connection connection = Reagent.getConnection();

        String substance;

        String supplier;

        String location;

        String category;

        String formula;

        String code;

        String amountMesure;

        double fw;

        double amount;

        public Reagent(final String substance, final String supplier, final String location,
            final String category, final String formula, final String code, final double fw,
            final double amount, final String amountMesure) {
            this.substance = substance;
            this.supplier = supplier;
            this.location = location;
            this.category = category;
            this.formula = formula;
            this.code = code;
            this.fw = fw;
            this.amount = amount;
            this.amountMesure = amountMesure;
        }

        static Reagent getReagent() throws SQLException {
            final Statement st = Reagent.connection.createStatement();
            final ResultSet rs = st.executeQuery("select * from Chemicals");
            while (rs.next()) {
                final String substance = rs.getString(1);
                final String location = rs.getString(2);
                final String supplier = rs.getString(3);
                final String code = rs.getString(4);
                final String amount = rs.getString(5);
                double amountd = 0;
                String amountmesure = null;
                if (amount != null && amount.trim().length() != 0) {
                    amountd = Reagent.parseAmount(amount);
                    amountmesure = Reagent.getAmountMesurement(amount);
                }
                final String formula = rs.getString(6);
                final String fw = rs.getString(7);
                double fwd = 0;
                if (fw != null && fw.trim().length() != 0) {
                    fwd = Reagent.parseFw(fw);
                }
                final String category = rs.getString(8);
                final Reagent r =
                    new Reagent(substance, supplier, location, category, formula, code, fwd, amountd,
                        amountmesure);
                r.printValues();
                r.validate();
            }
            return null;
        }

        static double parseFw(final String fw) {
            return Double.parseDouble(fw);
        }

        static String getAmountMesurement(String amount) {
            amount = amount.trim();
            int firstLetterPos = -1;
            if (Character.isLetter(amount.charAt(amount.length() - 1))) {
                firstLetterPos = Reagent.findFirstLetter(amount);
                return amount.substring(firstLetterPos);
            }
            return "";
        }

        static double parseAmount(String amount) {
            amount = amount.trim();
            double amountd = 0;
            // If amount is not specified
            if (Character.isLetter(amount.charAt(0))) {
                return 0;
            }
            amount = amount.replace(",", "");
            amount = amount.replace(".", "");
            if (Character.isLetter(amount.charAt(amount.length() - 1))) {
                final int firstLetterPos = Reagent.findFirstLetter(amount);
                System.out.println("AM " + amount + " fl " + firstLetterPos);
                amountd = Double.parseDouble(amount.substring(0, firstLetterPos));
            } else {
                amountd = Double.parseDouble(amount);
            }
            return amountd;
        }

        static int findFirstLetter(final String amount) {
            assert amount != null : "amount is null!";
            for (int i = 0; i < amount.length(); i++) {
                if (Character.isLetter(amount.charAt(i))) {
                    return i;
                }
            }
            // This should not happend
            return -1;
        }

        void validate() {
            if (this.amount != 0 && this.amountMesure == null) {
                System.err.println("Not valid record!");
            }
        }

        public void printValues() {
            System.out.println(this.toString());
            System.out.println();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Substance 1: " + this.substance + "\n" + "Location 2: " + this.location + "\n"
                + "Supplier 3: " + this.supplier + "\n" + "Code 4: " + this.code + "\n" + "Amount 5: "
                + this.amount + "\n" + "Amount mesurement : " + this.amountMesure + "\n" + "Formula 6: "
                + this.formula + "\n" + "FW 7: " + this.fw + "\n" + "Category 8: " + this.category + "\n";
        }

        /**
         * Get connection to the Leeds chemical database P.S. The ODBC connection to this database file should
         * be configured first It is assumed that the name of the database is "Chemicals"
         * 
         * @return Connection
         */
        static Connection getConnection() {
            if (null != Reagent.connection) {
                return Reagent.connection;
            }
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                Reagent.connection = java.sql.DriverManager.getConnection("jdbc:odbc:Chemicals");

                Reagent.connection.setAutoCommit(false);
                System.out.println("LeedsReagentImporter has connected to database Chemicals");

            } catch (final SQLException ex) {
                Throwable cause = ex;
                while (null != cause.getCause()) {
                    cause = cause.getCause();
                }
                cause.printStackTrace(); // LOG
                throw new RuntimeException(cause);
            } catch (final ClassNotFoundException ex) {
                ex.printStackTrace(); // LOG
                throw new RuntimeException(ex);
            }
            return Reagent.connection;
        }

        /*
         * void toPIMSReagent(WritableVersion rw) { HashMap param = new HashMap(); //
         * param.put(org.pimslims.model.molecule.Molecule.PROP_NAME, value);
         * param.put(org.pimslims.model.molecule.Molecule.PROP_SYNONYMS, value);
         * param.put(org.pimslims.model.people.Organisation.PROP_NAME, value);
         * param.put(org.pimslims.model.sample.RefSample.PROP_NAME, value);
         * //param.put(org.pimslims.model.sample.SampleComponent.PROP, value);
         * param.put(org.pimslims.model.sample.RefSampleSource.PROP_CATALOGNUM, value);
         * param.put(org.pimslims.model.sample.SampleComponent.PROP_CONTENTS, value);
         * param.put(org.pimslims.model.sample.Sample.PROP_NAME, value);
         * param.put(org.pimslims.model.sample.Sample.PROP_SAMPLECATEGORIES, value);
         * param.put(org.pimslims.model.reference.SampleCategory.PROP_NAME, value);
         * param.put(org.pimslims.model.holder.Holder.PROP_NAME, value); param.put(org.pimslims.model.location.Location.PROP_NAME,
         * value); param.put(org.pimslims.model.holder.HolderLocation.PROP_HOLDER, value); }
         */
        void putToDb() {
            // Util.getOrCreate(rw, "org.pimslims.model.molecule.Molecule",
            // "Leeds_Private", params)
        }

    } // class Reagent end

} // class end

