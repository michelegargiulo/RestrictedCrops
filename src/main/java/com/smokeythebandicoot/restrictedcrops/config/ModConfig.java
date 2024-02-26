package com.smokeythebandicoot.restrictedcrops.config;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import com.smokeythebandicoot.restrictedcrops.growthrules.BiomeNameCropGrowthRule;
import com.smokeythebandicoot.restrictedcrops.growthrules.CropGrowthRuleCollection;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Config(modid = RestrictedCrops.MODID, name = "Restricted Crops Configuration")
@Mod.EventBusSubscriber(modid = RestrictedCrops.MODID)
public class ModConfig {

    @Config.Ignore
    static CropGrowthRuleCollection plainsGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:plains", 0),
            new BiomeNameCropGrowthRule("minecraft:swampland", 0),
            new BiomeNameCropGrowthRule("minecraft:ocean", 0),
            new BiomeNameCropGrowthRule("minecraft:deep_ocean", 0),
            new BiomeNameCropGrowthRule("minecraft:river", 0));

    @Config.Ignore
    static CropGrowthRuleCollection desertGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:savanna", 0),
            new BiomeNameCropGrowthRule("minecraft:savanna_rock", 0),
            new BiomeNameCropGrowthRule("minecraft:mesa", 0),
            new BiomeNameCropGrowthRule("minecraft:mesa_rock", 0),
            new BiomeNameCropGrowthRule("minecraft:mesa_clear_rock", 0),
            new BiomeNameCropGrowthRule("minecraft:desert", 0),
            new BiomeNameCropGrowthRule("minecraft:desert_hills", 0));

    @Config.Ignore
    static CropGrowthRuleCollection frozenGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:taiga_cold", 0),
            new BiomeNameCropGrowthRule("minecraft:taiga_cold_hills", 0),
            new BiomeNameCropGrowthRule("minecraft:ice_flats", 0),
            new BiomeNameCropGrowthRule("minecraft:ice_mountains", 0),
            new BiomeNameCropGrowthRule("minecraft:frozen_river", 0),
            new BiomeNameCropGrowthRule("minecraft:frozen_ocean", 0),
            new BiomeNameCropGrowthRule("minecraft:cold_beach", 0));

    @Config.Ignore
    static CropGrowthRuleCollection jungleGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:jungle", 0),
            new BiomeNameCropGrowthRule("minecraft:jungle_hills", 0),
            new BiomeNameCropGrowthRule("minecraft:jungle_edge", 0),
            new BiomeNameCropGrowthRule("minecraft:beaches", 0),
            new BiomeNameCropGrowthRule("minecraft:stone_beach", 0));

    @Config.Ignore
    static CropGrowthRuleCollection forestGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:forest", 0),
            new BiomeNameCropGrowthRule("minecraft:forest_hills", 0),
            new BiomeNameCropGrowthRule("minecraft:birch_forest", 0),
            new BiomeNameCropGrowthRule("minecraft:birch_forest_hills", 0),
            new BiomeNameCropGrowthRule("minecraft:redwood_taiga", 0),
            new BiomeNameCropGrowthRule("minecraft:redwood_taiga_hills", 0),
            new BiomeNameCropGrowthRule("minecraft:roofed_forest", 0));

    @Config.Ignore
    static CropGrowthRuleCollection shroomGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:mushroom_island", 0),
            new BiomeNameCropGrowthRule("minecraft:mushroom_island_shore", 0));

    @Config.Ignore
    static CropGrowthRuleCollection mountsGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:smaller_extreme_hills", 0),
            new BiomeNameCropGrowthRule("minecraft:extreme_hills_with_trees", 0),
            new BiomeNameCropGrowthRule("minecraft:extreme_hills", 0));

    @Config.Ignore
    static CropGrowthRuleCollection netherGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:hell", -1));

    @Config.Ignore
    static CropGrowthRuleCollection theendGroup = new CropGrowthRuleCollection(
            new BiomeNameCropGrowthRule("minecraft:sky", 1));


    // Config options
    @Config.Ignore
    public static Map<String, CropGrowthRuleCollection> cropRules = new HashMap<>();

    @Config.Comment("If true, when crops are restricted, they'll drop as items in-world. " +
            "Does not always work, sometimes it drops nothing. Depends on how the crop is implemented")
    public static boolean dropBlockOnDeny = true;

    @Config.Comment("If true, when a crop grows, a line is written in debug.log detailing information about the event.")
    public static boolean logCrops = false;

    @Config.Comment("If true, when a sapling grows, a line is written in debug.log detailing information about the event.")
    public static boolean logSaplings = false;

    @Config.Comment("If true, when a player bonemeals something, a line is written in debug.log detailing information about the event.")
    public static boolean logBonemeal = false;

    @Config.Comment("If true, when a player bonemeals a crop that cannot grow there, a localized message will be printed in the chat to that player, explaining just that")
    public static boolean sendBonemealChatMessage = true;

    @Config.Comment("If true, when a player bonemeals something, a message is printed in the chat to that player. Useful for debug purposes")
    public static boolean bonemealDebug = false;

    @Config.Comment("Ultimate debug tool. If true, when a crop or sapling grows, the rule evaluation flow gets printed in the chat. Can produce huge logs when there are a lot of crops/saplings")
    public static boolean logRuleEvaluation = false;

    // COMPAT
    @Config.Comment("Open Terrain Generator (OTG) adds its version of Vanilla saplings. " +
            "It hooks in the Sapling Growth event. When a sapling is about to grow, it cancels " +
            "the event and generates one of its saplings, overriding the behaviour of RestrictedCrops. " +
            "This makes a player able to grow (only the OTG version of) Vanilla saplings bypassing growth rules." +
            "Set to true to make OTG not mess with sapling growth. Players won't be able to grow OTG versions " +
            "of Vanilla saplings anymore.")
    public static boolean preventOTGSaplings = true;

    @Config.Ignore
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

    // RULES
    @Config.Comment("Crop Rules. Format is " +
            "<modid>:<registry_name>:<meta>=ù" +
            "[dimension1|biome1,dimension2|biome2, ... , dimensionN|biomeN]. " +
            "Visit Wiki at https://github.com/michelegargiulo/RestrictedCrops/wiki for more information")
    public static String[] rawRules = defaultRules;

    @Config.Comment("Add here blocks that should be turned into Death Bushes on growth fail. Every block in this" +
            "list will be turned into a Dead Bush. If not in this list, the crop/sapling will follow the blockDropOnDeny config")
    public static String[] rawDeadBushes = new String[] {
            "minecraft:sapling:0",
            "minecraft:sapling:8",
            "minecraft:sapling:1",
            "minecraft:sapling:9",
            "minecraft:sapling:2",
            "minecraft:sapling:10",
            "minecraft:sapling:3",
            "minecraft:sapling:11",
            "minecraft:sapling:4",
            "minecraft:sapling:12",
            "minecraft:sapling:5",
            "minecraft:sapling:13",
    };

    @Config.Ignore
    public static HashSet<String> deadBushOnDeath = new HashSet<>();

    @Mod.EventBusSubscriber(modid = RestrictedCrops.MODID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(RestrictedCrops.MODID)) {
                ConfigManager.sync(RestrictedCrops.MODID, Config.Type.INSTANCE);
                reloadRules(rawRules);
            }
        }
    }

    private static void reloadRules(String[] rawRules) {

        cropRules.clear();
        deadBushOnDeath.clear();

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

        deadBushOnDeath.addAll(Arrays.asList(rawDeadBushes));
    }
}
