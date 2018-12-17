package org.pimslims.command.leeds.primerOrder;

import java.util.Set;

import org.pimslims.presentation.order.IOrderBean;

@Deprecated
public interface ILeedsPrimerOrderBean extends IOrderBean {

    public Float getAmountInUg();

    public String getBufferType();

    public Float getConcertrationInUM();

    public Float getMw();

    public String getNormalization();

    public Float getOd();

    public String getPurificationType();

    public String getRestrictionSites();

    public String getStartingSynthesisScale();

    public Integer getLenghtOnGene();

    public Float getTmFull();

    public Float getTmGene();

    public Float getTmSeller();

    public Float getVolumnInNL();

    /**
     * ILeedsPrimerOrderBean.getFiles
     * 
     * @return the hooks of the annotations
     */
    public Set<org.pimslims.util.File> getFiles();

}
