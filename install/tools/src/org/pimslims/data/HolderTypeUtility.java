/*
 * Created on 18-Dec-2006 17:04:58 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.io.IOException;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;

/**
 * 
 * Command line utility for adding reference data from CSV files for HolderType Data stored in db is:
 * holderCategory,name,maxRow,maxColumn,maxSubPosition,maxVolume(uL),maxVolumeDisplayUnit,details
 * 
 */
public class HolderTypeUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of holder types
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        load(wv, reader);
    }

    static void load(final WritableVersion wv, final java.io.Reader reader) throws IOException,
        ConstraintException {
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String name = parser.getValueByLabel("name");

            if ("".equals(name.trim())) { // spacer line
                continue;
            }

            final Map<String, Object> holderCatAttrMap = new java.util.HashMap<String, Object>();
            holderCatAttrMap.put(HolderCategory.PROP_NAME, parser.getValueByLabel("holderCategory"));

            final java.util.Collection holderCatFound = wv.findAll(HolderCategory.class, holderCatAttrMap);
            if (holderCatFound.isEmpty()) {
                throw new IllegalArgumentException("Unknown Holder Category: "
                    + parser.getValueByLabel("holderCategory"));
            } else if (1 == holderCatFound.size()) {
                final java.util.Set<HolderCategory> holderCategoryList =
                    new java.util.HashSet<HolderCategory>();
                holderCategoryList.add((HolderCategory) holderCatFound.iterator().next());

                final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
                attrMap.put(AbstractHolderType.PROP_NAME, parser.getValueByLabel("name"));
                final java.util.Collection holderTypeFound = wv.findAll(HolderType.class, attrMap);
                if (holderTypeFound.isEmpty()) {
                    try {
                        attrMap.put(HolderType.PROP_MAXROW, parser.getIntegerByLabel("maxRow"));
                        attrMap.put(HolderType.PROP_MAXCOLUMN, parser.getIntegerByLabel("maxColumn"));
                        attrMap.put(HolderType.PROP_MAXSUBPOSITION,
                            parser.getIntegerByLabel("maxSubPosition"));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Bad plate shape: "
                            + parser.getValueByLabel("maxRow") + "," + parser.getValueByLabel("maxColumn")
                            + "," + parser.getValueByLabel("maxSubPosition"));
                    }
                    if (!"".equals(parser.getValueByLabel("maxVolume(uL)").trim())) {
                        attrMap.put(HolderType.PROP_MAXVOLUME,
                            Float.valueOf(parser.getValueByLabel("maxVolume(uL)")));
                    }
                    attrMap.put(HolderType.PROP_MAXVOLUMEDISPLAYUNIT,
                        parser.getValueByLabel("maxVolumeDisplayUnit"));
                    attrMap.put(LabBookEntry.PROP_DETAILS, parser.getValueByLabel("details"));

                    attrMap.put(AbstractHolderType.PROP_DEFAULTHOLDERCATEGORIES, holderCategoryList);

                    String types = parser.getValueByLabel("experiment types");
                    if (null != types && types.contains("Crystallogenesis")) {
                        attrMap.put(CrystalType.PROP_RESSUBPOSITION, 100);
                        //TODO null would be better, but there was a not null constraint
                        new CrystalType(wv, attrMap);
                    } else {
                        new HolderType(wv, attrMap);
                    }
                    print("Created HolderType: [" + attrMap.get("name") + "]");
                } else if (1 == holderTypeFound.size()) {
                    AbstractLoader.debug("HolderType already exists: [" + parser.getValueByLabel("name")
                        + "]");

                }
            }

        }
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            AbstractLoader.print("Usage: HolderTypeUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                HolderTypeUtility.loadFile(wv, filename);
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
        AbstractLoader.print("Holder type utility has finished");
    }

}
