package com.smokeythebandicoot.restrictedcrops.config;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import com.smokeythebandicoot.restrictedcrops.growthrules.BiomeNameCropGrowthRule;
import com.smokeythebandicoot.restrictedcrops.growthrules.CropGrowthRuleCollection;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Config(modid = RestrictedCrops.MODID, name = "restrictedcrops")
@Mod.EventBusSubscriber(modid = RestrictedCrops.MODID)
public class ModConfig {

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
            "minecraft:wheat=*|tag:PLAINS,*|tag:FOREST",
            "minecraft:potatoes=*|tag:PLAINS,*|tag:FOREST,*|tag:MOUNTAIN",
            "minecraft:carrots=*|tag:PLAINS,*|tag:FOREST",
            "minecraft:beetroots=*|tag:PLAINS,*|tag:FOREST",
            "minecraft:melon_stem=*|tag:PLAINS,*|tag:FOREST,*|tag:JUNGLE",
            "minecraft:pumkpin_stem=*|tag:PLAINS,*|tag:FOREST,*|tag:MOUNTAIN",

            // Beans
            "minecraft:cocoa=*|tag:JUNGLE",

            // Shrooms
            "minecraft:brown_mushroom=*|tag:MUSHROOM,*|tag:PLAINS,*|tag:FOREST,*|tag:JUNGLE,*|tag:MOUNTAIN",
            "minecraft:red_mushroom=*|tag:MUSHROOM,*|tag:PLAINS,*|tag:FOREST,*|tag:JUNGLE,*|tag:MOUNTAIN",

            // Saplings - special case, as Meta also keeps track of growth stage
            "minecraft:sapling:0=*|tag:PLAINS,*|tag:FOREST,*|tag:!COLD+!HOT",
            "minecraft:sapling:8=*|tag:PLAINS,*|tag:FOREST,*|tag:!COLD+!HOT",
            "minecraft:sapling:1=*|tag:FOREST,*|tag:COLD+!HOT",
            "minecraft:sapling:9=*|tag:FOREST,*|tag:COLD+!HOT",
            "minecraft:sapling:2=*|tag:PLAINS,*|tag:FOREST,*|tag:MOUNTAIN",
            "minecraft:sapling:10=*|tag:PLAINS,*|tag:FOREST,*|tag:MOUNTAIN",
            "minecraft:sapling:3=*|tag:JUNGLE+HOT+!DRY",
            "minecraft:sapling:11=*|tag:JUNGLE+HOT+!DRY",
            "minecraft:sapling:4=*|tag:DESERT,*|tag:HOT,*|tag:!WET",
            "minecraft:sapling:12=*|tag:DESERT,*|tag:HOT,*|tag:!WET",
            "minecraft:sapling:5=*|tag:PLAINS,*|tag:FOREST,*|tag:!COLD,*|tag:!HOT",
            "minecraft:sapling:13=*|tag:PLAINS,*|tag:FOREST,*|tag:!COLD,*|tag:!HOT",

            // Special
            "minecraft:cactus=*|tag:DESERT,*|tag:HOT+DRY",
            "minecraft:reeds=*|tag:WET",

            // Nether
            "minecraft:nether_wart=*|tag:NETHER",

            // The End
            "minecraft:chorus_flower=*|tag:END",
            "minecraft:chorus_plant=*|tag:END",
    };

    // RULES
    @Config.Comment("Crop Rules. Format is " +
            "<modid>:<registry_name>:<meta>=" +
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

        public static void initRules() {
            ConfigManager.sync(RestrictedCrops.MODID, Config.Type.INSTANCE);
            reloadRules(rawRules);
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
