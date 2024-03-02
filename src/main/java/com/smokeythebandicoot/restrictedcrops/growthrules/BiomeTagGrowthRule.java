package com.smokeythebandicoot.restrictedcrops.growthrules;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.logging.log4j.Level;

import java.util.HashSet;

public class BiomeTagGrowthRule implements ICropGrowthRule {

    public final HashSet<TagRequirement> biomeReq;
    public final boolean anyDimension;
    public final Integer dimensionID;


    public BiomeTagGrowthRule(String source) {

        // Default params if parsing fails
        HashSet<TagRequirement> biomeReq = new HashSet<>();
        Integer dimId = null;
        boolean anyDim = false;

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

                // The string now may looks like: tags:TAG1+TAG2,TAG3,TAG4+TAG5,...
                // Tags may be in the format TAG or !TAG (inverted)
                for (String biomeTag : ruleInfos[1].substring(4).split("\\+")) {
                    boolean inv = biomeTag.startsWith("!");
                    TagRequirement tagReq = new TagRequirement(inv ? biomeTag.substring(1) : biomeTag, inv);
                    biomeReq.add(tagReq);
                }

            } catch (Exception ex) {
                RestrictedCrops.logger.log(Level.ERROR, String.format("Error while parsing rule: %s while processing source '%s'. Please fix your config", ex.toString(), source));
            }
        }

        this.biomeReq = biomeReq;
        this.dimensionID = dimId;
        this.anyDimension = anyDim;
    }

    @Override
    public ICropGrowthRule parse(String source) {
        return new BiomeTagGrowthRule(source);
    }

    @Override
    public boolean canGrowIn(Biome biome, int dimension) {
        for (TagRequirement req : biomeReq) {
            if (!req.isSatisfiedBy(biome)) return false;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        for (TagRequirement tag : biomeReq)
            if (tag.biomeTag == null)
                return false;
        return (anyDimension || dimensionID != null);
    }

    public class TagRequirement {
        public String biomeTag;
        public boolean inverted;

        public TagRequirement(String tag, boolean inverted) {
            this.biomeTag = tag;
            this.inverted = inverted;
        }

        public boolean isSatisfiedBy(Biome biome) {
            if (inverted) {
                for (BiomeDictionary.Type type : BiomeDictionary.getTypes(biome)) {
                    if (type.getName().equals(biomeTag)) return false;
                }
                return true;
            } else {
                for (BiomeDictionary.Type type : BiomeDictionary.getTypes(biome)) {
                    if (type.getName().equals(biomeTag)) return true;
                }
                return false;
            }
        }
    }
}
