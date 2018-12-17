package org.pimslims.graph;

/**
 * Exception thrown when there are problems while creating input dot.exe from IGraphModel by GrappaModelLoader
 * 
 * @see org.pimslims.utils.graph.GrappaModelLoader
 * @author Ekaterina Pilicheva
 * @since 26.04.2006
 * 
 */
@Deprecated
// no longer used
public class GraphModelException extends Exception {

    public GraphModelException() {
        super();
    }

    public GraphModelException(final String arg0) {
        super(arg0);
    }

    public GraphModelException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public GraphModelException(final Throwable arg0) {
        super(arg0);
    }

}
