package org.pimslims.graph.implementation;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.graph.IEdge;
import org.pimslims.graph.INode;

public class DefaultModelEdge implements IEdge {

    public static final int STANDARD = 2;

    public static final int IMPORTANT = 3;

    public static final int UNIMPORTANT = 1;

    protected final INode head;

    protected final INode tail;

    private final Map<String, String> attributes = new HashMap();

    public DefaultModelEdge(final INode headNode, final INode tailNode) {
        this(headNode, tailNode, DefaultModelEdge.STANDARD);
    }

    public DefaultModelEdge(final INode headNode, final INode tailNode, final int weight) {
        super();
        assert null != headNode && null != tailNode;
        this.head = headNode;
        this.tail = tailNode;
        this.setWeight(weight);
    }

    public INode getHead() {
        return this.head;
    }

    public INode getTail() {
        return this.tail;
    }

    /**
     * DefaultModelEdge.setWeight larger weight requests that the edge is shorter and more vertical
     * 
     * @param f
     */
    private void setWeight(final int weight) {
        this.attributes.put("weight", Integer.toString(weight));
    }

    /**
     * DefaultModelEdge.getAttributes
     * 
     * @see org.pimslims.graph.IEdge#getAttributes()
     */
    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

}
