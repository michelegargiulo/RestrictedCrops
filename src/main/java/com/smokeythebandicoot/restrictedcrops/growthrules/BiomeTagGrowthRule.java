package com.smokeythebandicoot.restrictedcrops.growthrules;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeTagGrowthRule implements ICropGrowthRule {

    public final String biomeTag;

    public BiomeTagGrowthRule(String source) {
        // Remove the initial "tag:" part
        this.biomeTag = source.substring(4);
    }

    @Override
    public ICropGrowthRule parse(String source) {
        return new BiomeTagGrowthRule(source);
    }

    @Override
    public boolean canGrowIn(Biome biome, int dimension) {
        for (BiomeDictionary.Type t : BiomeDictionary.getTypes(biome)) {
            if (t.getName().equals(biomeTag)) return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
