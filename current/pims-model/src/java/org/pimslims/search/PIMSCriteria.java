/**
 * org.pimslims.hibernate Query.java
 * 
 * @date 13 Apr 2007 22:40:44
 * 
 * @author Bill Lin, bl67@dl.ac.uk Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 1.1
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.search;

import java.util.List;
import java.util.Map;

import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.search.Paging.Order;

/**
 * Query used to do search
 * 
 */
public interface PIMSCriteria {

    /**
     * @return result collection
     */
    public <T extends ModelObject> List<T> list();

    /**
     * @return size of results
     */
    public int count();

    /**
     * @param attributeMap the criteria Map for search
     * @return current query
     */
    public abstract PIMSCriteria setAttributeMap(java.util.Map<String, Object> attributeMap);

    /**
     * @param isQueryCacheable The isQueryCacheable to set.
     * @return current query
     */
    @Deprecated
    // does nothing
    public abstract PIMSCriteria setIsQueryCacheable(Boolean isQueryCacheable);

    /**
     * @param joinMetaRoles The joinMetaRoles to set.
     * @return current query
     */
    public abstract PIMSCriteria setJoinMetaRoles(List<MetaRole> joinMetaRoles);

    /**
     * @param paging
     */
    public PIMSCriteria setPaging(Paging paging);

    /**
     * @return Returns the targetJavaClass.
     */
    public Class getTargetJavaClass();

    /**
     * @return Returns the joinRoleNames.
     */
    public List<String> getJoinRoleNames();

    /**
     * @return Returns the attributeMap.
     */
    public java.util.Map<String, Object> getAttributeMap();

    /**
     * @return Returns the isQueryCacheable.
     */
    public Boolean getIsQueryCacheable();

    /**
     * @return Returns the paging.
     */
    public Paging getPaging();

    /**
     * @return
     */
    public Map<String, Order> getOrderBy();

    /**
     * @return
     */
    public MetaClass getMetaClass();

    /**
     * @return
     */
    public String getAccessControlAlias();

}
