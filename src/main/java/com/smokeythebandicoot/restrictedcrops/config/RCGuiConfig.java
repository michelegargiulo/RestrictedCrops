package com.smokeythebandicoot.restrictedcrops.config;

import com.smokeythebandicoot.restrictedcrops.RestrictedCrops;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RCGuiConfig extends GuiConfig {

    public RCGuiConfig(GuiScreen parentScreen) {
        super(parentScreen,
                new ConfigElement(ModConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                RestrictedCrops.MODID,
                false,
                false,
                "Restricted Crops Configuration");
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        // Button IDs remain the same
        // Button ID 2000: DONE button in Forge Gui Config
        if (button.id == 2000) {
            boolean flag = true;

            try {
                if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof RCGuiConfig))
                        && (this.entryList.hasChangedEntry(true))) {

                    boolean requiresMcRestart = this.entryList.saveConfigElements();

                    if (Loader.isModLoaded(modID))
                    {
                        ConfigChangedEvent event = new ConfigChangedEvent.OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (!event.getResult().equals(Event.Result.DENY))
                            MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));

                        if (requiresMcRestart)
                        {
                            flag = false;
                            mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle",
                                    new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
                        }

                        this.needsRefresh = true;
                    }
                }
            }
            catch (Throwable e) {
                e.printStackTrace();
            }

            if (flag) this.mc.displayGuiScreen(this.parentScreen);
        }

        // Button id 2001: RESET TO DEFAULT in Forge Gui Config
        else if (button.id == 2001) {
            this.entryList.setAllToDefault(this.chkApplyGlobally.isChecked());

        // Button ID 2002: CANCEL button in Forge Gui Config
        } else if (button.id == 2002) {
            this.entryList.undoAllChanges(this.chkApplyGlobally.isChecked());
        }
    }
}
