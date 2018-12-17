package org.pimslims.graph.implementation;

/**
 * Represents a relationship between two PiMS records
 * 
 * @author cm65
 * 
 */
public class ModelObjectLink extends DefaultModelEdge implements org.pimslims.graph.IEdge {

    ModelObjectLink(final ModelObjectNode head, final ModelObjectNode tail) {
        super(head, tail);
    }

    ModelObjectLink(final ModelObjectNode head, final ModelObjectNode tail, final int weight) {
        super(head, tail, weight);
    }

    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object) Note that this method must be reconsidered if we allow
     *      one sample to be used as an input to the same experiment multiple times.
     */
    @Override
    public boolean equals(final Object object) {
        if (null == object) {
            return false;
        }
        if (!(object instanceof ModelObjectLink)) {
            return false;
        }
        final ModelObjectLink other = (ModelObjectLink) object;
        final boolean ret =
            (((ModelObjectNode) this.head).getObject() == ((ModelObjectNode) other.head).getObject())
                && (((ModelObjectNode) this.tail).getObject() == ((ModelObjectNode) other.tail).getObject());
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.head.hashCode() + 43 * this.tail.hashCode();
    }

}
