/*
 * Created on 17-Dec-2005-01:32:50 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2005 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaProperty;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;

/**
 * 
 * Command line utility for adding reference data from CSV files for ComponentCategory Data is: name,details
 * 
 */
public class GenericRefLoader extends AbstractLoader {

    /**
     * @param fileName2
     * @param CSV filename with a list of sample categories
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String className, final String fileName)
        throws java.io.IOException, AccessException, ConstraintException {
        //get and check metaClass for the class to be loaded
        final AbstractModel model = wv.getModel();
        final MetaClass metaClass = model.getMetaClass(className);
        assert metaClass != null;
        //open the file
        java.io.Reader reader;
        File file = new File(fileName);
        try {
            reader = new java.io.FileReader(file);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsolutePath());
            throw e;
        }
        final CsvParser lcsvp = new CsvParser(reader);

        //get the labels which should be attribute/role name
        final List<String> labels = Arrays.asList(lcsvp.getLabels());
        if (!labels.contains("name")) {
            throw new RuntimeException(
                "GenericRefLoader can not handle data file which doese not have a column for 'name'!");
        }

        while (lcsvp.getLine() != null) {
            final String name = lcsvp.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }
            //full attributes
            final Map<String, Object> fullAttrMap = new java.util.HashMap<String, Object>();
            for (final String propertyName : labels) {
                GenericRefLoader.loadProperty(wv, fullAttrMap, metaClass, propertyName, lcsvp
                    .getValueByLabel(propertyName));
            }
            //key attribute: name
            final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
            keyAttrMap.put("name", lcsvp.getValueByLabel("name"));

            AbstractLoader.UpdateOrCreate(wv, metaClass.getJavaClass(), keyAttrMap, null, fullAttrMap);

        }
    }

    /**
     * @param wv
     * @param fullAttrMap
     * @param metaClass
     * @param propertyName
     * @param valueByLabel
     */
    private static void loadProperty(final WritableVersion wv, final Map<String, Object> fullAttrMap,
        final MetaClass metaClass, final String propertyName, final String propertyValue) {
        final MetaProperty property = metaClass.getProperty(propertyName);
        if (property == null) {
            throw new RuntimeException(metaClass.getName() + " does not have property: " + propertyName);
        }

        if (property instanceof MetaAttribute) {
            final Object value = GenericRefLoader.convertValue((MetaAttribute) property, propertyValue);
            fullAttrMap.put(propertyName, value);
        } else {
            final MetaRole role = (MetaRole) property;
            if (propertyValue == null) {
                return;
            }
            final String[] values = propertyValue.split(";");
            final Collection<ModelObject> otherSideObjects = new HashSet<ModelObject>();
            for (int i = 0; i < values.length; i++) {
                //value[i] should be the name of otherside's modelObject
                final ModelObject propertyValueObject =
                    wv.findFirst(role.getOtherMetaClass().getJavaClass(), "name", values[i].trim());
                if (propertyValueObject == null) {
                    throw new RuntimeException("Can not find " + role.getOtherMetaClass().getName()
                        + " which name is " + values[i].trim());
                }
                otherSideObjects.add(propertyValueObject);
            }
            fullAttrMap.put(propertyName, otherSideObjects);
        }
    }

    /**
     * GenericRefLoader.convertValue
     * 
     * @param property
     * @param propertyValue
     * @return
     */
    private static Object convertValue(final MetaAttribute property, final String propertyValue) {
        if (property.getType() == String.class) {
            return propertyValue;
        }
        if (property.getType() == Float.class) {
            return new Float(propertyValue);
        }
        if (property.getType() == Integer.class) {
            return new Integer(propertyValue);
        } else {
            throw new RuntimeException("Please add code to convert String to " + property.getType());
        }
    }

    public static void main(final String[] args) {

        if (args.length != 2) {
            AbstractLoader.print("Usage: GenericRefLoader javaClassName filename.csv");
            return;
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        final String className = args[0];
        final String fileName = args[1];
        final MetaClass metaClass = model.getMetaClass(className);
        if (metaClass == null) {
            AbstractLoader.print("Can not load class:" + className);
            return;
        }

        final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            System.out.println("loading " + className + " from " + fileName);
            GenericRefLoader.loadFile(wv, className, fileName);
            wv.commit();
            AbstractLoader.print("Loaded details from file: " + fileName + " for " + className);
        } catch (final java.io.IOException ex) {
            AbstractLoader.print("Unable to read from file: " + fileName);
            ex.printStackTrace();
        } catch (final ModelException ex) {
            AbstractLoader.print("Unable to add details from file: " + fileName);
            ex.printStackTrace();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

}
