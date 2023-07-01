package com.smokeythebandicoot.restrictedcrops.growthrules;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import org.apache.logging.log4j.Level;

public class CropGrowthRule {

    private final String biomeRegistryName;
    private final Integer dimensionID;
    private final boolean anyBiome;
    private final boolean anyDimension;

    public CropGrowthRule(String biomeRegistryName, Integer dimensionID, boolean anyBiome, boolean anyDimension) {
        this.biomeRegistryName = biomeRegistryName;
        this.dimensionID = dimensionID;
        this.anyBiome = anyBiome;
        this.anyDimension = anyDimension;
    }

    public CropGrowthRule(String biomeRegistryName, Integer dimensionID) {
        this.biomeRegistryName = biomeRegistryName;
        this.dimensionID = dimensionID;
        this.anyBiome = false;
        this.anyDimension = false;
    }

    public CropGrowthRule(boolean anyBiome, boolean anyDimension) {
        this.biomeRegistryName = null;
        this.dimensionID = null;
        this.anyBiome = anyBiome;
        this.anyDimension = anyDimension;
    }

    public String getBiomeRegistryName() {
        return biomeRegistryName;
    }

    public int getDimensionID() {
        return dimensionID;
    }

    public boolean isAnyBiome() {
        return anyBiome;
    }

    public boolean isAnyDimension() {
        return anyDimension;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isAnyDimension() ? "*" : getDimensionID())
                .append("|")
                .append(isAnyBiome() ? "*" : getBiomeRegistryName());
        return sb.toString();
    }

    public static CropGrowthRule parse(String source) {
        // Split rules by Pipe (|)
        String[] ruleInfos = source.split("\\|");
        if (ruleInfos.length != 2) { // Rule parsing error
            RestrictedCrops.logger.log(Level.ERROR, "Invalid parsing for split '" + source + "'. Ignoring...");
            return null;
        }

        // Dimension is handled differently. Integer.parseInt throws exception if source
        // string is in an incorrect format
        int dim = 0;
        try {
            boolean anyDim = ruleInfos[0].equals("*");
            if (!anyDim)
                dim = Integer.parseInt(ruleInfos[0]);
            return new CropGrowthRule(ruleInfos[1], dim, ruleInfos[1].equals("*"), anyDim);
        } catch (Exception ex) {
            RestrictedCrops.logger.log(Level.ERROR, String.format("Error while parsing rule: %s while processing source '%s'. Please fix your config", ex.toString(), source));
        }
        return null;
    }

    public boolean canGrowIn(String biome, int dimension) {
        if (anyBiome && anyDimension) return true;
        return (anyBiome || biomeRegistryName.equals(biome)) && (anyDimension || (dimensionID == dimension));
    }
}
