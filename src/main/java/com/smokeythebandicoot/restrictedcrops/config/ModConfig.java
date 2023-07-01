package com.smokeythebandicoot.restrictedcrops.config;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import com.smokeythebandicoot.restrictedcrops.growthrules.CropGrowthRule;
import com.smokeythebandicoot.restrictedcrops.growthrules.CropGrowthRuleCollection;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {

    public final static String CATEGORY_GENERAL = "General";
    public final static String CATEGORY_COMPAT = "Compat";
    public static Configuration config;

    static CropGrowthRuleCollection plainsGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:plains", 0),
            new CropGrowthRule("minecraft:swampland", 0),
            new CropGrowthRule("minecraft:ocean", 0),
            new CropGrowthRule("minecraft:deep_ocean", 0),
            new CropGrowthRule("minecraft:river", 0));

    static CropGrowthRuleCollection desertGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:savanna", 0),
            new CropGrowthRule("minecraft:savanna_rock", 0),
            new CropGrowthRule("minecraft:mesa", 0),
            new CropGrowthRule("minecraft:mesa_rock", 0),
            new CropGrowthRule("minecraft:mesa_clear_rock", 0),
            new CropGrowthRule("minecraft:desert", 0),
            new CropGrowthRule("minecraft:desert_hills", 0));

    static CropGrowthRuleCollection frozenGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:taiga_cold", 0),
            new CropGrowthRule("minecraft:taiga_cold_hills", 0),
            new CropGrowthRule("minecraft:ice_flats", 0),
            new CropGrowthRule("minecraft:ice_mountains", 0),
            new CropGrowthRule("minecraft:frozen_river", 0),
            new CropGrowthRule("minecraft:frozen_ocean", 0),
            new CropGrowthRule("minecraft:cold_beach", 0));

    static CropGrowthRuleCollection jungleGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:jungle", 0),
            new CropGrowthRule("minecraft:jungle_hills", 0),
            new CropGrowthRule("minecraft:jungle_edge", 0),
            new CropGrowthRule("minecraft:beaches", 0),
            new CropGrowthRule("minecraft:stone_beach", 0));

    static CropGrowthRuleCollection forestGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:forest", 0),
            new CropGrowthRule("minecraft:forest_hills", 0),
            new CropGrowthRule("minecraft:birch_forest", 0),
            new CropGrowthRule("minecraft:birch_forest_hills", 0),
            new CropGrowthRule("minecraft:redwood_taiga", 0),
            new CropGrowthRule("minecraft:redwood_taiga_hills", 0),
            new CropGrowthRule("minecraft:roofed_forest", 0));

    static CropGrowthRuleCollection shroomGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:mushroom_island", 0),
            new CropGrowthRule("minecraft:mushroom_island_shore", 0));

    static CropGrowthRuleCollection mountsGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:smaller_extreme_hills", 0),
            new CropGrowthRule("minecraft:extreme_hills_with_trees", 0),
            new CropGrowthRule("minecraft:extreme_hills", 0));

    static CropGrowthRuleCollection netherGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:hell", -1));

    static CropGrowthRuleCollection theendGroup = new CropGrowthRuleCollection(
            new CropGrowthRule("minecraft:sky", 1));


    // Config options
    // GENERAL
    public static Map<String, CropGrowthRuleCollection> cropRules = new HashMap<>();
    public static boolean dropBlockOnDeny = true;
    public static boolean logCrops = false;
    public static boolean logSaplings = false;
    public static boolean logBonemeal = false;
    public static boolean sendBonemealChatMessage = true;
    public static boolean bonemealDebug = false;
    public static boolean logRuleEvaluation = false;

    // COMPAT
    public static boolean preventOTGSaplings = true;

    // RULES
    public static String[] rawRules = {};
    public static String[] defaultRules = new String[] {
            // Crops
            "minecraft:wheat=" + plainsGroup.appendRules(forestGroup).appendRules(jungleGroup).toString(),
            "minecraft:potatoes=" + plainsGroup.appendRules(forestGroup).appendRules(jungleGroup).appendRules(mountsGroup).toString(),
            "minecraft:carrots=" + plainsGroup.appendRules(forestGroup).appendRules(jungleGroup).toString(),
            "minecraft:beetroots=" + plainsGroup.appendRules(forestGroup).appendRules(jungleGroup).toString(),
            "minecraft:melon_stem=" + plainsGroup.appendRules(forestGroup).appendRules(jungleGroup).toString(),
            "minecraft:pumkpin_stem=" + plainsGroup.appendRules(forestGroup).appendRules(jungleGroup).toString(),

            // Beans
            "minecraft:cocoa=" + jungleGroup.toString(),

            // Shrooms
            "minecraft:brown_mushroom=" + shroomGroup.appendRules(plainsGroup).appendRules(forestGroup).appendRules(jungleGroup).appendRules(mountsGroup).toString(),
            "minecraft:red_mushroom=" + shroomGroup.appendRules(plainsGroup).appendRules(forestGroup).appendRules(jungleGroup).appendRules(mountsGroup).toString(),

            // Saplings - special case, as Meta also keeps track of growth stage
            "minecraft:sapling:0=" + plainsGroup.appendRules(desertGroup).appendRules(jungleGroup).toString(),
            "minecraft:sapling:8=" + plainsGroup.appendRules(desertGroup).appendRules(jungleGroup).toString(),
            "minecraft:sapling:1=" + forestGroup.appendRules(frozenGroup).appendRules(mountsGroup).toString(),
            "minecraft:sapling:9=" + forestGroup.appendRules(frozenGroup).appendRules(mountsGroup).toString(),
            "minecraft:sapling:2=" + plainsGroup.appendRules(forestGroup).appendRules(mountsGroup).toString(),
            "minecraft:sapling:10=" + plainsGroup.appendRules(forestGroup).appendRules(mountsGroup).toString(),
            "minecraft:sapling:3=" + jungleGroup.appendRules(forestGroup).toString(),
            "minecraft:sapling:11=" + jungleGroup.appendRules(forestGroup).toString(),
            "minecraft:sapling:4=" + desertGroup.appendRules(plainsGroup).toString(),
            "minecraft:sapling:12=" + desertGroup.appendRules(plainsGroup).toString(),
            "minecraft:sapling:5=" + forestGroup.appendRules(plainsGroup).toString(),
            "minecraft:sapling:13=" + forestGroup.appendRules(plainsGroup).toString(),

            // Special
            "minecraft:cactus=" + desertGroup.toString(),
            "minecraft:reeds=" + jungleGroup.appendRules(plainsGroup).toString(),

            // Nether
            "minecraft:nether_wart=" + netherGroup.toString(),

            // The End
            "minecraft:chorus_flower=" + theendGroup.toString(),
            "minecraft:chorus_plant=" + theendGroup.toString(),
    };

    public static void initConfigFile(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        config = new Configuration(configFile);
        syncConfig();
    }

    public static void syncConfig() {

        config.load();

        // ################################# GENERAL
        dropBlockOnDeny = config.get(CATEGORY_GENERAL, "dropBlockOnDeny", true,
                "If true, when a crop is denied to grow, when a CropGrowEvent or SaplingGrowEvent the crop will" +
                        "be dropped as an item, instead of being destroyed (replaced by air)").getBoolean();
        logCrops = config.get(CATEGORY_GENERAL, "logCrops", false,
                "If true, all CropGrowEvents will be logged to the console").getBoolean();
        logSaplings = config.get(CATEGORY_GENERAL, "logSaplings", false,
                "If true, all SaplingGrowEvents will be logged to the console").getBoolean();
        logBonemeal = config.get(CATEGORY_GENERAL, "logBonemeal", false,
                "If true, all BonemealEvents will be logged to the console").getBoolean();
        sendBonemealChatMessage = config.get(CATEGORY_GENERAL, "sendBonemealChatMessage", true,
                "If true, when the player tries to bonemeal a plant that cannot grow, a chat message" +
                        "appears").getBoolean();
        bonemealDebug = config.get(CATEGORY_GENERAL, "bonemealDebug", false,
                "If true, BonemealEvents will also be printed in the player chat").getBoolean();
        logRuleEvaluation = config.get(CATEGORY_GENERAL, "logRuleEvaluation", false,
                "If true, Rule Evaluations will be logged. Enable this only for debug purposes and disable" +
                        "it if it is no longer of any use. May generate log files larger than 500 MB").getBoolean();

        rawRules = config.get(CATEGORY_GENERAL, "rawRules", defaultRules, "Each entry must be in the following format: <CROP>=<dimension1|biome1>,<dimension2|biome2>, ... , <dimensionN|biomeN>" +
                "\nThe CROP must be in the format of <mod:registryname:meta>. For example, the Acacia Sapling is minecraft:sapling:4." +
                "\nPlease note that the the CROP is the Blockstate, not the item. " +
                "\nSo for example, to add Melon, do not add minecraft:melon_seeds, but minecraft:melon_step. " +
                "\nYou can see the Blockstate by using F3 and looking at the crop block.\n" +
                "Each rule is evalued on each CropGrowEvent and SaplingGrowEvent, the rule is evaluated and the action is taken (allow/deny)").getStringList();

        // ################################# COMPAT
        preventOTGSaplings = config.get(CATEGORY_COMPAT, "preventOTGSaplings", true, "If true, unregisters the OTG" +
                "Sapling listener, that intercepts SaplingGrowTreeEvent events and cancels them replacing the trees with their own. This makes" +
                " the player able to grow vanilla trees outside of the biomes defined in the rules.").getBoolean();

        reloadRules(rawRules);

        if (config.hasChanged())
            config.save();

    }

    private static void reloadRules(String[] rawRules) {
        for (String s : rawRules) {
            String[] expression = s.split("=");
            if (expression.length != 2) {
                RestrictedCrops.logger.log(Level.WARN, "Invalid config expression: " + s + ". Ignoring...");
                continue;
            }
            String crop = expression[0];
            String rules = expression[1];

            CropGrowthRuleCollection ruleCollection = CropGrowthRuleCollection.parse(rules);

            cropRules.put(crop, ruleCollection);
        }
    }
}
