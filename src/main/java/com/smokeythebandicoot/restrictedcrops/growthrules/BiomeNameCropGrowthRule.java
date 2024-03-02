package com.smokeythebandicoot.restrictedcrops.growthrules;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;

public class BiomeNameCropGrowthRule implements ICropGrowthRule {

    private final String biomeRegistryName;
    private final Integer dimensionID;
    private final boolean anyBiome;
    private final boolean anyDimension;

    public BiomeNameCropGrowthRule(String biomeRegistryName, Integer dimensionID, boolean anyBiome, boolean anyDimension) {
        this.biomeRegistryName = biomeRegistryName;
        this.dimensionID = dimensionID;
        this.anyBiome = anyBiome;
        this.anyDimension = anyDimension;
    }

    public BiomeNameCropGrowthRule(String biomeRegistryName, Integer dimensionID) {
        this.biomeRegistryName = biomeRegistryName;
        this.dimensionID = dimensionID;
        this.anyBiome = false;
        this.anyDimension = false;
    }

    public BiomeNameCropGrowthRule(boolean anyBiome, boolean anyDimension) {
        this.biomeRegistryName = null;
        this.dimensionID = null;
        this.anyBiome = anyBiome;
        this.anyDimension = anyDimension;
    }

    public BiomeNameCropGrowthRule(String source) {

        // Default params if parsing fails
        String biomeRegName = null;
        Integer dimId = null;
        boolean anyDim = false;
        boolean anyBiome = false;

        String[] ruleInfos = source.split("\\|");
        if (ruleInfos.length != 2) { // Rule parsing error
            RestrictedCrops.logger.log(Level.ERROR, "Invalid parsing for split '" + source + "'. Ignoring...");
        } else {

            // Dimension is handled differently. Integer.parseInt throws exception if source
            // string is in an incorrect format
            try {
                anyDim = ruleInfos[0].equals("*");
                if (!anyDim)
                    dimId = Integer.parseInt(ruleInfos[0]);

                anyBiome = ruleInfos[1].equals("*");
                if (!anyBiome)
                    biomeRegName = ruleInfos[1];

            } catch (Exception ex) {
                RestrictedCrops.logger.log(Level.ERROR, String.format("Error while parsing rule: %s while processing source '%s'. Please fix your config", ex.toString(), source));
            }
        }

        this.biomeRegistryName = biomeRegName;
        this.dimensionID = dimId;
        this.anyBiome = anyBiome;
        this.anyDimension = anyDim;
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

    @Override
    public boolean canGrowIn(Biome biome, int dimension) {
        if (anyBiome && anyDimension) return true;
        if (biome == null || biome.getRegistryName() == null) return true;
        return (anyBiome || biomeRegistryName.equals(biome.getRegistryName().toString())) && (anyDimension || (dimensionID == dimension));
    }

    @Override
    public ICropGrowthRule parse(String source) {
        return new BiomeNameCropGrowthRule(source);
    }

    @Override
    public boolean isValid() {
        return (anyBiome || biomeRegistryName != null) || (anyDimension && dimensionID != null);
    }
}
