package com.smokeythebandicoot.restrictedcrops.growthrules;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class CropGrowthRuleCollection {

    private HashSet<ICropGrowthRule> growthRules;

    public CropGrowthRuleCollection(HashSet<ICropGrowthRule> growthRules) {
        this.growthRules = growthRules;
    }

    public CropGrowthRuleCollection() {
        this.growthRules = new HashSet<ICropGrowthRule>();
    }

    public CropGrowthRuleCollection(ICropGrowthRule...rules) {
        this.growthRules = new HashSet<>();
        growthRules.addAll(Arrays.asList(rules));
    }

    public CropGrowthRuleCollection appendRule(ICropGrowthRule rule) {
        growthRules.add(rule);
        return this;
    }

    public CropGrowthRuleCollection appendRules(CropGrowthRuleCollection ruleCollection) {
        growthRules.addAll(ruleCollection.growthRules);
        return this;
    }

    public boolean canGrowIn(Biome biome, int dimension) {
        for (ICropGrowthRule rule : growthRules) {
            if (rule == null || rule.canGrowIn(biome, dimension)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<ICropGrowthRule> iterator = growthRules.iterator();
        while (iterator.hasNext()) {
            ICropGrowthRule rule = iterator.next();
            sb.append(rule.toString()).append(iterator.hasNext() ? "," : "");
        }
        return sb.toString();
    }

    public static CropGrowthRuleCollection parse(String source) {

        CropGrowthRuleCollection growthRules = new CropGrowthRuleCollection();

        String[] splits = source.split(",");

        for (String split : splits) {
            if (split.isEmpty()) continue; // Ignore empty rules

            // Decide if the split has to be parsed into a BiomeName Rule or BiomeTag Rule
            ICropGrowthRule rule;
            if (source.startsWith("tag:"))
                rule = new BiomeTagGrowthRule(split);
            else
                rule = new BiomeNameCropGrowthRule(split);
            if (!rule.isValid()) {
                RestrictedCrops.logger.log(Level.WARN, "Cannot parse rule: " + split);
                continue;
            }
            growthRules.appendRule(rule);
        }

        return growthRules;
    }
}
