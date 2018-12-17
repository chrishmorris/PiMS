/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.Calendar;

/**
 *
 * @author ian
 */
public class Tube {
    private Calendar createDate;
    private Calendar useByDate;
    private Condition condition = null;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Calendar getUseByDate() {
        return useByDate;
    }

    public void setUseByDate(Calendar useByDate) {
        this.useByDate = useByDate;
    }
    
    
    public Tube() {
    }

}
