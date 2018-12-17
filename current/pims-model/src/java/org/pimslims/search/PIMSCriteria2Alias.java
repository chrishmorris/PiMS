/** 
 * pims-dm org.pimslims.search PIMSCriteria2Alias.java
 * @author jon
 * @date 11 Nov 2008
 *
 * Protein Information Management System
 * @version: 2.3
 *
 * Copyright (c) 2008 jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search;

import org.hibernate.criterion.CriteriaSpecification;

/**
 * PIMSCriteria2Alias.
 *
 */
public class PIMSCriteria2Alias {

    /**
     * Enum to restrict joinType to allowed values.
     */
    public enum JoinType {
        /**
         * INNER_JOIN joinType.
         */
        INNER_JOIN(CriteriaSpecification.INNER_JOIN),
        /**
         * LEFT_JOIN joinType.
         */
        LEFT_JOIN(CriteriaSpecification.LEFT_JOIN),
        /**
         * FULL_JOIN joinType.
         */
        FULL_JOIN(CriteriaSpecification.FULL_JOIN);
        /**
         * The joinType.
         */
        private final int joinType;
        /**
         * Construct a JoinType.
         * @param joinType - the joinType to set
         */
        private JoinType(final int joinType) {
            this.joinType = joinType;
        }
        /**
         * @return The joinType
         */
        public int getJoinType() {
            return this.joinType;
        }

    }

    /**
     * The associationPath of the Alias.
     */
    private final String associationPath;

    /**
     * The alias of the Alias.
     */
    private final String alias;

    /**
     * The joinType of the Alias.
     */
    private final JoinType joinType;

    /**
     * Construct an Alias with the specified associationPath, alias
     * and joinType=JoinType.INNER_JOIN.
     * 
     * @param associationPath - the associationPath to be aliased
     * @param alias - the alias for the associationPath
     * @see #PIMSCriteria2Alias(String, String, JoinType)
     */
    public PIMSCriteria2Alias(final String associationPath, final String alias) {
        this(associationPath, alias, JoinType.INNER_JOIN);
    }

    /**
     * Construct an Alias with the specified associationPath, alias
     * and joinType.
     * 
     * @param associationPath - the associationPath to be aliased
     * @param alias - the alias for the associationPath
     * @param joinType - the joinType by which the associationPath will be joined
     */
    public PIMSCriteria2Alias(final String associationPath, final String alias, final JoinType joinType) {
        if (null == associationPath) {
            this.associationPath = "";
        }
        else {
            this.associationPath = associationPath;
        }
        if (null == alias) {
            this.alias = "";
        }
        else {
            this.alias = alias;
        }
        this.joinType = joinType;
    }

    /**
     * @return Returns the associationPath.
     */
    protected String getAssociationPath() {
        return associationPath;
    }

    /**
     * @return Returns the alias.
     */
    protected String getAlias() {
        return alias;
    }

    /**
     * @return Returns the joinType.
     */
    protected int getJoinType() {
        return joinType.getJoinType();
    }
    
    /**
     * PIMSCriteria2Alias.equals
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if ((null != obj) && (obj instanceof PIMSCriteria2Alias)) {
            PIMSCriteria2Alias a = (PIMSCriteria2Alias) obj;
            return (this.getAlias().equals(a.getAlias())
                && this.getAssociationPath().equals(a.getAssociationPath())
                && (this.getJoinType() == a.getJoinType()));
        }
        return false;
    }

    /**
     * PIMSCriteria2Alias.hashCode
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.associationPath.hashCode()
            + (7 * this.alias.hashCode())
            + (13 * this.joinType.getJoinType());
    }

}
