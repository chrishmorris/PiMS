package org.pimslims.lab.experiment;

import java.util.Map;


public class LeftToRightGradient extends Gradient {

    public LeftToRightGradient(float min, float max, String start, String end) {
        super(min, max, start, end);
    }

    @Override
    public Map<String, Float> gradient() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
