package org.pimslims.command.DataUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanWriter;

public class ComponentCategoryFixer implements IDataFixer {

    private static AbstractModel model;

    private static int MAX = 1000;

    public Boolean fixData(final WritableVersion version) throws ConstraintException, AccessException {
        boolean everything_is_correct = true;
        final Collection<Molecule> molComponents = version.getAll(Molecule.class, 0, Integer.MAX_VALUE); // doesn't matter if this is slow
        int i = 0;

        for (final Molecule molComponent : molComponents) {
            if (molComponent.getCategories().size() == 0) {

                i++;
                if (i > ComponentCategoryFixer.MAX) {
                    return false;
                }
                String category = null;

                if (molComponent.getName().contains("PCR Product")) {
                    category = "PCRProduct";
                }

                if (molComponent.getName().contains("PCRProduct")) {
                    category = "PCRProduct";
                }

                if (molComponent.getName().contains("construct")) {
                    category = "construct";
                }

                if (molComponent.getName().contains("template")) {
                    category = "template";
                }

                if (molComponent.getName().contains("Template")) {
                    category = "template";
                }

                if (molComponent.getName().endsWith("F")) {
                    category = "forwardprimer";
                }

                if (molComponent.getName().endsWith("R")) {
                    category = "reverseprimer";
                }

                if (molComponent.getName().endsWith("DNA")) {
                    category = "dna";
                }

                if (molComponent.getName().startsWith("OPTIC") && molComponent.getMolType().equals("protein")) {
                    category = "protein";
                }

                if (molComponent.getName().startsWith("OPPF") && molComponent.getMolType().equals("protein")) {
                    category = "protein";
                }

                if (molComponent.getName().equals("a hydroxyisobutyric acid")) {
                    category = "chelator";
                }
                if (molComponent.getName().equals("2-ethyl-5-phenylisoxazolium sulfonate")) {
                    category = "modifyingagent";
                }
                if (molComponent.getName().equals("dipicolinic acid")) {
                    category = "chelator";
                }
                if (molComponent.getName().equals("copper(II) chloride dihydrate")) {
                    category = "salt";
                }
                if (molComponent.getName().equals("2-amino-2-methyl-1,3-propanediol")) {
                    category = "bufferingagent";
                }

                if (molComponent.getName().endsWith("Expressed Protein")) {
                    category = "expressedProt";
                } else if (molComponent.getName().endsWith("Final Protein")) {
                    category = "finalProt";
                } else if (molComponent.getName().endsWith("Protein")) {
                    category = "construct";
                }

                if (null != category) {

                    final String categoryName = ComponentCategoryFixer.spotPimsMapping.get(category);

                    final ComponentCategory componentCategory =
                        version.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME, categoryName);
                    if (null == componentCategory) {
                        System.err.println("Can not find ComponentCategory:" + categoryName);
                    } else {
                        if (!molComponent.getCategories().contains(componentCategory)) {
                            molComponent.addCategory(componentCategory);
                            System.out.println("add Category " + componentCategory + " for " + molComponent);
                            everything_is_correct = false;
                        }
                    }

                } /*else {

                                                                                                                                                                          System.out.println("MolComponent [" + molComponent.getName() + ":"
                                                                                                                                                                              + molComponent.getCategories().size() + ":" + molComponent.getMolType() + "]");
                                                                                                                                                                      }*/
            }
        }

        return everything_is_correct;
    }

    private static final HashMap<String, String> spotPimsMapping = new HashMap<String, String>();
    static {
        // Primer design mappings
        ComponentCategoryFixer.spotPimsMapping.put("primerDesignExpt", "Primer Design");
        //spotPimsMapping.put("forwardTag", FORWARD_TAG); // obsolete
        //spotPimsMapping.put("reverseTag", REVERSE_TAG); // obsolete
        ComponentCategoryFixer.spotPimsMapping.put("fwdPrimer", ConstructBeanWriter.FPRIMER); // obsolete
        ComponentCategoryFixer.spotPimsMapping.put("revPrimer", ConstructBeanWriter.RPRIMER); // obsolete
        //spotPimsMapping.put("fwdOverlap", FORWARD_OVERLAP);// obsolete
        //spotPimsMapping.put("revOverlap", REVERSE_OVERLAP);// obsolete

        // TestMolecule role mappings - see findSeqs()
        //spotPimsMapping.put("compCatNamingSys", ConstructUtility.SPOTCONSTRUCT);
        ComponentCategoryFixer.spotPimsMapping.put("construct", ConstructBean.PROTEIN); //obsolete
        ComponentCategoryFixer.spotPimsMapping.put("template", "Template"); // see ConstructbeanWriter
        ComponentCategoryFixer.spotPimsMapping.put("expressedProt", ConstructBean.EXPRESSED_PROTEIN); //obsolete
        ComponentCategoryFixer.spotPimsMapping.put("finalProt", ConstructBean.FINAL_PROTEIN); //obsolete
        //ComponentCategoryFixer.spotPimsMapping.put("PCRProduct", ConstructBean.PCR_PRODUCT); //obsolete
        ComponentCategoryFixer.spotPimsMapping.put("forwardprimer", "Primer");
        ComponentCategoryFixer.spotPimsMapping.put("reverseprimer", "Primer");
        ComponentCategoryFixer.spotPimsMapping.put("dna", "Nucleic acid");
        ComponentCategoryFixer.spotPimsMapping.put("protein", ConstructBean.PROTEIN); //obsolete
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {

        return "PiMS 1516 the Component Catergory is not set for TrialMolComponent in the construct Writer";
    }

    /**
     * @param args
     */
    public static void main(final String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            ComponentCategoryFixer.model = ModelImpl.getModel();
        } else {
            ComponentCategoryFixer.initModel(args[0]);
        }

        System.out.println("\n---------ComponentCategoryFixer-----------------------------------");

        final WritableVersion wv =
            ComponentCategoryFixer.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        if (null == wv) {
            return;
        }

        final ComponentCategoryFixer fixer = new ComponentCategoryFixer();
        boolean b = false;

        try {
            b = fixer.fixData(wv).booleanValue();
            wv.commit();

        } catch (final ConstraintException e) {
            e.printStackTrace();
            System.out.println("Exception caught [ " + e.getLocalizedMessage() + "]");
            //} catch (AccessException e) {
            //    e.printStackTrace();
            //    System.out.println("Exception caught [ " + e.getLocalizedMessage() + "]");
        } catch (final AbortedException e) {
            e.printStackTrace();
            System.out.println("Exception caught [ " + e.getLocalizedMessage() + "]");
        } catch (final AccessException e) {
            e.printStackTrace();
            System.out.println("Exception caught [ " + e.getLocalizedMessage() + "]");
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        if (b) {
            System.out
                .println("-------------ComponentCategoryFixer has finished!---------------------------");
        } else {
            System.out
                .println("-------------ComponentCategoryFixer has NOT finished!-----------------------");
        }
    }

    /**
     * @param string
     * @throws FileNotFoundException
     */
    private static void initModel(final String propertyFileName) throws FileNotFoundException {

        //start from propertyFile if provided
        System.out.println("loading DB connection info from: " + propertyFileName);
        final File properties = new java.io.File(propertyFileName);
        if (!properties.exists()) {
            System.out.println("file does NOT exist:" + propertyFileName);
        } else if (!properties.isFile()) {
            System.out.println("please give a file NOT a directory: " + propertyFileName);
        }
        ComponentCategoryFixer.model = org.pimslims.dao.ModelImpl.getModel(properties);

    }

}
