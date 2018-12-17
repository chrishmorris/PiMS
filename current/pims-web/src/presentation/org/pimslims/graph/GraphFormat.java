package org.pimslims.graph;

import java.util.HashMap;
import java.util.Map;

public class GraphFormat {

    public static final String DOT_ID = "id";

    public static final String DOT_COLOR = "color";

    public static final String DOT_SHAPE = "shape";

    public static final String DOT_FILLCOLOR = "fillcolor";

    public static final String DOT_STYLE = "style";

    public static final String DOT_URL = "URL";

    public static final String DOT_LABEL = "label";

    public static final String DOT_TOOLTIP = "tooltip";

    public static final String DOT_FONTSIZE = "fontsize";

    public static final String DOT_SHAPE_HEXAGON = "hexagon";

    public static final String DOT_SHAPE_CIRCLE = "circle";

    public static final String DOT_SHAPE_DIAMOND = "diamond";

    public static final String DOT_SHAPE_ELLIPSE = "ellipse";

    public static final String DOT_SHAPE_INVHOUSE = "invhouse";

    public static final String DOT_SHAPE_OCTAGON = "octagon";

    public static final String DOT_SHAPE_POLYGON = "polygon";

    public static final String DOT_SHAPE_RECTANGLE = "box";

    public static final String DOT_SHAPE_TRIANGLE = "triangle";

    public static final String DOT_SHAPE_TRAPEZIUM = "trapezium";

    public static final String DOT_SHAPE_PARALLELOGRAM = "parallelogram";

    private static final Map<String, String> BlueGraphNodeMap = new HashMap();
    static {
        // GraphFormat.BlueGraphNodeMap.put(GraphFormat.DOT_COLOR, "black");
        GraphFormat.BlueGraphNodeMap.put(GraphFormat.DOT_STYLE, "filled");
        GraphFormat.BlueGraphNodeMap.put(GraphFormat.DOT_FILLCOLOR, "#bfefff55");
        GraphFormat.BlueGraphNodeMap.put("gradientangle", "90");

        // do not expand node to fit label
        // required, otherwise cmapx does not match png
        GraphFormat.BlueGraphNodeMap.put("fixedsize", "true");
        // could supply width, height. Probably proportional to the square root of the number of nodes.
        // but ratio=fill for the graph means that dot rescales for us
    }

    private static final Map<String, String> CoralGraphNodeMap = new HashMap();
    static {
        GraphFormat.CoralGraphNodeMap.put(GraphFormat.DOT_COLOR, "coral");
        GraphFormat.CoralGraphNodeMap.put(GraphFormat.DOT_STYLE, "filled");
        GraphFormat.CoralGraphNodeMap.put(GraphFormat.DOT_FILLCOLOR, "coral");
    }

    public static Map getGraphNodeMap() {
        return GraphFormat.BlueGraphNodeMap;
    }

}
