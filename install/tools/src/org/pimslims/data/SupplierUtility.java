/*
 * Created on 07-Feb-2006 12:41:20 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.util.Collection;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.people.Organisation;

/**
 * 
 * Command line utility for adding supplier data from CSV files for People.Organisation Data is:
 * organisationType,name,address1,address2,address3,address4,city,postalCode,country,phoneNumber,faxNumber,emailAddress,url
 * 
 * 
 */
public class SupplierUtility extends AbstractLoader {

    /**
     * load Organisation from file when an Organisation has same name but with different other details, it
     * will be updated otherwise, a new one will be created
     * 
     * @param CSV filename with a list of suppliers
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser lcsvp = new CsvParser(reader);

        while (lcsvp.getLine() != null) {
            final String name = lcsvp.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }

            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put(Organisation.PROP_NAME, lcsvp.getValueByLabel("name"));
            attrMap.put(Organisation.PROP_ORGANISATIONTYPE, lcsvp.getValueByLabel("organisationType"));
            final java.util.List<String> addresses = new java.util.ArrayList<String>();
            if (lcsvp.getValueByLabel("address1") != null && lcsvp.getValueByLabel("address1").length() > 0) {
                addresses.add(lcsvp.getValueByLabel("address1"));
            }
            if (lcsvp.getValueByLabel("address2") != null && lcsvp.getValueByLabel("address2").length() > 0) {
                addresses.add(lcsvp.getValueByLabel("address2"));
            }
            if (lcsvp.getValueByLabel("address3") != null && lcsvp.getValueByLabel("address3").length() > 0) {
                addresses.add(lcsvp.getValueByLabel("address3"));
            }
            if (lcsvp.getValueByLabel("address4") != null && lcsvp.getValueByLabel("address4").length() > 0) {
                addresses.add(lcsvp.getValueByLabel("address4"));
            }
            if (addresses.size() > 0) {
                attrMap.put(Organisation.PROP_ADDRESSES, addresses);
            }
            attrMap.put(Organisation.PROP_CITY, lcsvp.getValueByLabel("city"));
            attrMap.put(Organisation.PROP_POSTALCODE, lcsvp.getValueByLabel("postalCode"));
            attrMap.put(Organisation.PROP_COUNTRY, lcsvp.getValueByLabel("country"));
            attrMap.put(Organisation.PROP_PHONENUMBER, lcsvp.getValueByLabel("phoneNumber"));
            attrMap.put(Organisation.PROP_FAXNUMBER, lcsvp.getValueByLabel("faxNumber"));
            attrMap.put(Organisation.PROP_EMAILADDRESS, lcsvp.getValueByLabel("emailAddress"));
            attrMap.put(Organisation.PROP_URL, lcsvp.getValueByLabel("url"));
            // totoally same records will be ignore
            if (wv.findAll(Organisation.class, attrMap).size() > 0) {
                AbstractLoader.print("Organisation: [" + attrMap.get("organisationType") + ", "
                    + attrMap.get("name") + "] already in DB");
            }
            // create a new one when can not find same name
            else if (wv.findAll(Organisation.class, Organisation.PROP_NAME, lcsvp.getValueByLabel("name"))
                .size() == 0) {
                wv.create(Organisation.class, attrMap);
                AbstractLoader.print("+Adding Organisation: [" + attrMap.get("organisationType") + ", "
                    + attrMap.get("name") + "]");
            }
            // update orgnization which has same name
            else {
                final Collection<Organisation> orgs =
                    wv.findAll(Organisation.class, Organisation.PROP_NAME, lcsvp.getValueByLabel("name"));
                if (orgs.size() != 1) {
                    System.err.println("Found more than 1 Organisation has same name: "
                        + lcsvp.getValueByLabel("name"));
                    AbstractLoader.print("Found more than 1 Organisation has same name: "
                        + lcsvp.getValueByLabel("name"));
                }
                for (final Organisation org : orgs) {
                    org.setAddresses(addresses);
                    org.setOrganisationType((String) attrMap.get(Organisation.PROP_ORGANISATIONTYPE));
                    org.setCity((String) attrMap.get(Organisation.PROP_CITY));
                    org.setPostalCode((String) attrMap.get(Organisation.PROP_POSTALCODE));
                    org.setCountry((String) attrMap.get(Organisation.PROP_COUNTRY));
                    org.setPhoneNumber((String) attrMap.get(Organisation.PROP_PHONENUMBER));
                    org.setFaxNumber((String) attrMap.get(Organisation.PROP_FAXNUMBER));
                    org.setEmailAddress((String) attrMap.get(Organisation.PROP_EMAILADDRESS));
                    org.setUrl((String) attrMap.get(Organisation.PROP_URL));
                }
            }
        }
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: SupplierUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                SupplierUtility.loadFile(wv, filename);
                wv.commit();
                AbstractLoader.print("Loaded details from file: " + filename);
            } catch (final java.io.IOException ex) {
                AbstractLoader.print("Unable to read from file: " + filename);
                ex.printStackTrace();
            } catch (final ModelException ex) {
                AbstractLoader.print("Unable to add details from file: " + filename);
                ex.printStackTrace();
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
        AbstractLoader.print("Supplier utility has finished");
    }

}
