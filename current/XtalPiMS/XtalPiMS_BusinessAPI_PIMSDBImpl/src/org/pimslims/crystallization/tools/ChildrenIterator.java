package org.pimslims.crystallization.tools;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class ChildrenIterator implements Iterator<Element> {

    private final String childName;

    private final Element parent;

    private Node next;

    /**
     * Constructor for ChildrenIterator
     * 
     * @param element the parent element
     * @param name the name of the child elements, or null if any name is acceptable
     */
    public ChildrenIterator(Element element, String name) {
        this.parent = element;
        this.childName = name;
        this.next = parent.getFirstChild();
        skipText();
    }

    public boolean hasNext() {
        return null != this.next;
    }

    public Element next() {
        if (null == this.next) {
            throw new java.util.NoSuchElementException();
        }
        Element ret = (Element) this.next;
        this.next = this.next.getNextSibling();
        skipText();
        return ret;
    }

    private void skipText() {
        while (this.next instanceof Text) {
            this.next = this.next.getNextSibling();
        }
        assert null == this.next || null == this.childName || this.childName.equals(this.next.getLocalName()) : "Unexpected element: "
            + this.next.getLocalName();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
