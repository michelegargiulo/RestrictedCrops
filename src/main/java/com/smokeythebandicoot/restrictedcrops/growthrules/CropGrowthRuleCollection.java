package com.smokeythebandicoot.restrictedcrops.growthrules;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Iterator;

public class CropGrowthRuleCollection {

    private HashSet<CropGrowthRule> growthRules;

    public CropGrowthRuleCollection(HashSet<CropGrowthRule> growthRules) {
        this.growthRules = growthRules;
    }

    public CropGrowthRuleCollection() {
        this.growthRules = new HashSet<CropGrowthRule>();
    }

    public CropGrowthRuleCollection(CropGrowthRule...rules) {
        this.growthRules = new HashSet<>();
        for (CropGrowthRule rule : rules) {
            growthRules.add(rule);
        }
    }

    public CropGrowthRuleCollection appendRule(CropGrowthRule rule) {
        growthRules.add(rule);
        return this;
    }

    public CropGrowthRuleCollection appendRules(CropGrowthRuleCollection ruleCollection) {
        for (CropGrowthRule rule : ruleCollection.growthRules) {
            growthRules.add(rule);
        }
        return this;
    }

    public boolean canGrowIn(String biome, int dimension) {
        for (CropGrowthRule rule : growthRules) {
            if (rule == null || rule.canGrowIn(biome, dimension)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<CropGrowthRule> iterator = growthRules.iterator();
        while (iterator.hasNext()) {
            CropGrowthRule rule = iterator.next();
            sb.append(rule.toString()).append(iterator.hasNext() ? "," : "");
        }
        return sb.toString();
    }

    public static CropGrowthRuleCollection parse(String source) {

        CropGrowthRuleCollection growthRules = new CropGrowthRuleCollection();

        String[] splits = source.split(",");
        if (splits.length == 0) {
            return growthRules;
        }

        for (String split : splits) {
            if (split.isEmpty()) continue; // Ignore empty rules
            CropGrowthRule rule = CropGrowthRule.parse(split);
            if (rule == null) {
                RestrictedCrops.logger.log(Level.WARN, "Cannot parse rule: " + split);
                continue;
            }
            growthRules.appendRule(rule);
        }

        return growthRules;
    }
}
