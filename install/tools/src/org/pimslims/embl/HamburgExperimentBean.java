package org.pimslims.embl;

class HamburgExperimentBean {

    private final String expressionQuality;
    private final Float expressionLevel;
    private final String strain;
    private final String vector;
    private final String constructDescription;
    private final String crystalSize;
    private final String resolution;
    private final String ligand;
    private final String condition;
    private final String crystalForm;

    public HamburgExperimentBean(String expressionQuality,
            Float expressionLevel, String strain, String vector,
            String constructDescription, String crystalSize, String resolution,
            String ligand, String condition, String crystalForm) {
        super();
        this.expressionQuality = expressionQuality;
        this.expressionLevel = expressionLevel;
        this.strain = strain;
        this.vector = vector;
        this.constructDescription = constructDescription;
        this.crystalSize = crystalSize;
        this.resolution = resolution;
        this.ligand = ligand;
        this.condition = condition;
        this.crystalForm = crystalForm;
    }

    public String getExpressionQuality() {
        return expressionQuality;
    }

    public Float getExpressionLevel() {
        return expressionLevel;
    }

    public String getStrain() {
        return strain;
    }

    public String getVector() {
        return vector;
    }

    public String getConstructDescription() {
        return constructDescription;
    }

    public String getCrystalSize() {
        return crystalSize;
    }

    public String getResolution() {
        return resolution;
    }

    public String getLigand() {
        return ligand;
    }

    public String getCrystalForm() {
        return crystalForm;
    }

    public String getCondition() {
        return this.condition;
    }

    public boolean isChrystallography() {
        boolean noCrystallography = "".equals(condition)
                && "".equals(crystalForm) && "".equals(ligand)
                && "".equals(resolution) && "".equals(crystalSize);
        return !noCrystallography;
    }

}
