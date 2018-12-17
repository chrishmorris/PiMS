/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import org.pimslims.business.core.model.Person;
import java.util.Calendar;

/**
 * <p>After consultation with users, a user annotation or score of an image 
 * relates to the Trial Drop and not just the single image whereas an
 * automated software annotation / score relates to a single image</p>
 * @author IMB
 */
public class UserScore {
    private long id = -1l;
    private ScoreValue value = null;
    private Person user = null;
    private Calendar date = null;
    private TrialDrop drop = null;

    public UserScore() {
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ScoreValue getValue() {
        return value;
    }

    public void setValue(ScoreValue value) {
        this.value = value;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public TrialDrop getDrop() {
        return drop;
    }

    public void setDrop(TrialDrop drop) {
        this.drop = drop;
    }


    
    
}
