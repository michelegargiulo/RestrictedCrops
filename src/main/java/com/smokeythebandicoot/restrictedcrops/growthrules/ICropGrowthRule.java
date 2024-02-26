package com.smokeythebandicoot.restrictedcrops.growthrules;

import net.minecraft.world.biome.Biome;

public interface ICropGrowthRule {

    /**
     * Parses the rule from a String
     * @param source
     * @return
     */
    public ICropGrowthRule parse(String source);

    /**
     * Returns True if the rule matches the conditions
     * @param biome
     * @param dimension
     * @return
     */
    public boolean canGrowIn(Biome biome, int dimension);

    /**
     * Checks if the rule parsing was successful
     * @return True if the rule is valid
     */
    public boolean isValid();

}
