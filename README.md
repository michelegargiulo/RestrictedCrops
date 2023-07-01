# RestrictedCrops

This mod is a complete rewrite from scratch of the Biome Growth Controls mod (https://www.curseforge.com/minecraft/mc-mods/restricted-saplings) byLothrazar

The mod is a continuation for Minecraft 1.12.2, increasing performance, control and functionality over the original mod.

# Feature overview
- Restricts crops to specific Biome/Dimension combinations
- Restricts Bonemealing on plants that cannot grow there
- Sends chat messages in case  the player bonemeals a plant that cannot grow (configurable)
- Destroys or drops crop and saplings (configurable)
- Comes with default config
- In-game reloadable config
- Compat with OTG (configurable)
- Extensive optional logging

# Notable Improvements
- The mod can specify a list of specific Biome/Dimension combination for where a crop/sapling can grow. This allows the creation of more complex rules, that for example limits the growth of Mystical Agriculture crops to the Mushroom Island biome in the Overworld or on Any Biome in The End.
- The mod backports an improvement from the modern versions of Biome Growth Controls, where instead of specifying which crops can grow in a biome, speficies in which biomes (and dimensions) a crop can grow in, and stores this information in a HashMap for a fast look-up
- The mod comes with an in-game config GUI, to edit configuration at run-time
- Extensive logging for modpack makers. Something doesn't work? Enable loggings for all events and debug your rules
- Some mods are able to add custom salings, such as Open Terrain Generator (or OTG). OTG does not allow disabling this functionality, resulting in some Saplings able to grow even in restricted biomes/dimensions. This mod comes with a fix that disables OTG Sapling Listener, effectively disabling such functionality and enforcing restrictions

# Ok, but why?
Two reasons:
- Growing Cacti in a snowy mountaintop makes no sense
- Encourages players to build decentralized farms, forcing them to spread out their bases, connect them and transport resources from one to another
