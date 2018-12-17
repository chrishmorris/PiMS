package org.pimslims.lab.experiment;

import java.util.Map;

public abstract class Gradient {

    protected final float min;

    protected final float max;

    protected final String start;

    protected final String end;

    protected Gradient(float min, float max, String start, String end) {
        this.min = min;
        this.max = max;
        this.start = start;
        this.end = end;
    }

    public abstract Map<String, Float> gradient();

}
