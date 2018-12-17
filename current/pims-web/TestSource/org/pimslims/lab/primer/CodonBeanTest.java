/**
 * current-pims-web org.pimslims.lab.primer TagsTest.java
 * 
 * @author cm65
 * @date 17 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.lab.primer;

import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.symbol.CodonPrefTools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;

/**
 * TagsTest
 * 
 */
public class CodonBeanTest extends TestCase {

    /**
     * @param name
     */
    public CodonBeanTest(final String name) {
        super(name);
    }

    public final void testCodonBean() throws IllegalAlphabetException, IllegalSymbolException {
        final CodonBean bean = new CodonBean("AAA");
        Assert.assertEquals("AAA", bean.getTriplet());
        Assert.assertEquals("Lys", bean.getAminoAcid());
    }

    public final void testTranslationTable() throws IllegalAlphabetException, IllegalSymbolException {
        final Collection<CodonBean> beans = CodonBean.getTranslationTable();
        Assert.assertEquals(64, beans.size());
        final CodonBean[] beanArray = new CodonBean[64];
        beans.toArray(beanArray);

        Assert.assertEquals("Asn", beanArray[1].getAminoAcid());
        Assert.assertEquals("AAC", beanArray[1].getTriplet());
        Assert.assertEquals("N", beanArray[1].getAminoAcid1Letter());

        Assert.assertEquals("Ser", beanArray[11].getAminoAcid());
        Assert.assertEquals("AGT", beanArray[11].getTriplet());

        Assert.assertEquals("Pro", beanArray[21].getAminoAcid());
        Assert.assertEquals("CCC", beanArray[21].getTriplet());

        Assert.assertEquals("Leu", beanArray[31].getAminoAcid());
        Assert.assertEquals("CTT", beanArray[31].getTriplet());
    }

    public final void testBestPreferrerCodonTable() throws IllegalAlphabetException, IllegalSymbolException {

        final Collection<CodonBean> beans = CodonBean.getPreferredCodonTable(CodonPrefTools.ECOLI);
        Assert.assertEquals(20, beans.size());

        CodonBean bean;
        bean = CodonBean.getCodonBean("His", beans);
        Assert.assertEquals("His", bean.getAminoAcid());
        Assert.assertEquals("CAT", bean.getTriplet());

        bean = CodonBean.getCodonBean("Ala", beans);
        Assert.assertEquals("Ala", bean.getAminoAcid());
        Assert.assertEquals("GCG", bean.getTriplet());

        bean = CodonBean.getCodonBean("Ser", beans);
        Assert.assertEquals("Ser", bean.getAminoAcid());
        Assert.assertEquals("AGC", bean.getTriplet());
    }

}
