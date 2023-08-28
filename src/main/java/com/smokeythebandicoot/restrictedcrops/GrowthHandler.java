package com.smokeythebandicoot.restrictedcrops;

import com.smokeythebandicoot.restrictedcrops.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

public class GrowthHandler {

    @SubscribeEvent
    public void onBoneMealEvent(BonemealEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if (pos == null || world.isAirBlock(pos)) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        String crop = state.getBlock().getRegistryName().toString();
        String cropMeta = String.valueOf(block.getMetaFromState(state));
        String biome = world.getBiome(pos).getRegistryName().toString();
        int dim = world.provider.getDimension();

        if (ModConfig.logBonemeal) RestrictedCrops.logger.info(String.format("Caught BONEMEAL event of block state %s in biome %s in dim %s", crop, biome, dim));
        if (ModConfig.bonemealDebug) event.getEntityPlayer().sendMessage(new TextComponentString(String.format("Caught BONEMEAL event of block state %s in biome %s in dim %s", crop, biome, dim)));

        boolean isAllowed = evaluateRule(crop, cropMeta, biome, dim);
        if (ModConfig.bonemealDebug) RestrictedCrops.logger.info("Result of bonemeal event: " + isAllowed);
        if (!isAllowed) {
            // If allowed, send a chat message explaining that the bonemeal cannot be applied to the plant
            if (ModConfig.sendBonemealChatMessage && !event.getWorld().isRemote)
                //event.getEntityPlayer().sendMessage(new TextComponentTranslation("message.bonemeal.fail"));
                event.getEntityPlayer().sendMessage(new TextComponentString(I18n.format("message.bonemeal.fail")));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCropGrowEvent(CropGrowEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if (pos == null || world.isAirBlock(pos)) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        String crop = state.getBlock().getRegistryName().toString();
        String cropMeta = String.valueOf(block.getMetaFromState(state));
        String biome = world.getBiome(pos).getRegistryName().toString();
        int dim = world.provider.getDimension();

        if (ModConfig.logCrops)
            RestrictedCrops.logger.info(String.format("Caught CROP GROWTH of block state %s in biome %s in dim %s", crop, biome, dim));

        if (!evaluateRule(crop, cropMeta, biome, dim)) {
            if (ModConfig.logCrops) RestrictedCrops.logger.info("Canceled CROP GROWHT event");
            try {
                if (block == Blocks.CHORUS_PLANT) {
                    if (world.getBlockState(pos.up()).getBlock() == Blocks.CHORUS_FLOWER) {
                        world.destroyBlock(pos.up(), ModConfig.dropBlockOnDeny);
                        world.destroyBlock(pos, false);
                    }
                }
                event.setResult(Result.DENY);
                if (ModConfig.deadBushOnDeath.contains(crop)) {
                    world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
                } else if (ModConfig.dropBlockOnDeny) {
                    world.destroyBlock(pos, true);
                }
            }
            catch (Throwable e) {
                RestrictedCrops.logger.error("[RC] Error while processing Crop Growth Event: " + e.toString() + ". Event result: " + event.getResult().toString());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = world.getBlockState(pos).getBlock();

        String sapling = state.getBlock().getRegistryName().toString();
        String saplingMeta = String.valueOf(block.getMetaFromState(state));
        String biome = world.getBiome(pos).getRegistryName().toString();
        int dim = world.provider.getDimension();

        if (ModConfig.logSaplings) RestrictedCrops.logger.info(String.format("Caught SAPLING GROWTH of block state %s in biome %s in dim %s", sapling, biome, dim));

        if (!evaluateRule(sapling, saplingMeta, biome, dim)) {
            if (ModConfig.logSaplings) RestrictedCrops.logger.info("Canceled SAPLING GROWHT event");
            try {
                event.setResult(Result.DENY);
                if (ModConfig.deadBushOnDeath.contains(sapling)) {
                    world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
                } else if (ModConfig.dropBlockOnDeny) {
                    world.destroyBlock(pos, true);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            }
            catch (Throwable e) {
                RestrictedCrops.logger.error("[RC] Error while processing Sapling Growth Event: " + e.toString() + ". Event result: " + event.getResult().toString());
            }
        }
    }

    public boolean evaluateRule(String blockRegistryName, String blockMeta, String biome, int dim) {

        String fullstate = blockRegistryName + ":" + blockMeta;
        boolean wildcardMeta = false;

        // Does the state have any rules associated with it? If no rules, then -> allowed
        if (!ModConfig.cropRules.containsKey(blockRegistryName + ":" + blockMeta)) {
            if (ModConfig.logRuleEvaluation) {
                RestrictedCrops.logger.log(Level.INFO, String.format(
                    "Crop has no rules, so %s is allowed to grow in Biome %s, Dimension %s. " +
                    "Checking for wildcard meta as a fallback", fullstate, biome, dim));
            }

            if (!ModConfig.cropRules.containsKey(blockRegistryName)) {
                if (ModConfig.logRuleEvaluation) {
                    RestrictedCrops.logger.log(Level.INFO, String.format("Crop has no rules, so %s is allowed to grow in Biome %s, Dimension %s", blockRegistryName, biome, dim));
                }
                return true;
            }
            wildcardMeta = true;
        }

        // There are rules associated. Can the crop grow?
        if (ModConfig.cropRules.get(wildcardMeta ? blockRegistryName : fullstate).canGrowIn(biome, dim)) {
            if (ModConfig.logRuleEvaluation) RestrictedCrops.logger.log(Level.INFO, String.format("Crop %s is allowed to grow in Biome %s, Dimension %s", wildcardMeta ? blockRegistryName : fullstate, biome, dim));
            return true;
        }
        if (ModConfig.logRuleEvaluation) RestrictedCrops.logger.log(Level.INFO, String.format("Crop %s is NOT allowed to grow in Biome %s, Dimension %s", wildcardMeta ? blockRegistryName : fullstate, biome, dim));
        return false;
    }
}
