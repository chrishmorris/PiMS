/**
 * pims-web org.pimslims.utils.graph.implementation LocationComponentNode.java
 * 
 * @author Marc Savitsky
 * @date 3 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.graph.implementation;

import java.util.Map;

import org.pimslims.graph.INode;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;

/**
 * LocationComponentNode
 * 
 */
public class LocationNode implements INode {

    private final Containable object;

    public LocationNode(final Containable object) {
        this.object = object;
    }

    public final String getId() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Location: ");
        if (this.object instanceof Container) {
            final Container location = (Container) this.object;
            sb.append("Type ");
            sb.append(location.getContainerType().getName());
        }
        sb.append("; ");
        sb.append("Name ");
        sb.append(this.object.getName());
        sb.append(";");
        return sb.toString();
    }

    public final String getLabel() {
        return this.object.getName();

    }

    @Override
    public boolean equals(final Object other) {
        if (null == other) {
            return false;
        }
        if (other instanceof LocationNode) {
            return this.object.equals(((LocationNode) other).object);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.object.hashCode();
    }

    /**
     * IGraphElement.getType
     * 
     * @see org.pimslims.graph.IGraphElement#getType()
     */
    public String getType() {
        return "Location";
    }

    /**
     * IGraphElement.getAttributes
     * 
     * @see org.pimslims.graph.IGraphElement#getAttributes()
     */
    public Map getAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * IGraphElement.setAttribute
     * 
     * @see org.pimslims.graph.IGraphElement#setAttribute(java.lang.String, java.lang.String)
     */
    public void setAttribute(final String attName, final String attValue) {
        // TODO Auto-generated method stub

    }

    /**
     * LocationNode.get_Hook
     * 
     * @return
     */
    public String get_Hook() {
        return this.object.get_Hook();
    }

    /**
     * LocationNode.compareTo
     * 
     * @param object2
     * @return
     */
    public int compareTo(final Object object2) {
        return this.getLabel().compareTo(object2.toString());
    }

    /**
     * INode.getCluster
     * 
     * @see org.pimslims.graph.INode#getCluster()
     */
    @Override
    public String getCluster() {
        // TODO Auto-generated method stub
        return null;
    }

}
