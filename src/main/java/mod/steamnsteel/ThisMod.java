/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel;

import mod.steamnsteel.configuration.ConfigurationHandler;
import mod.steamnsteel.crafting.Recipes;
import mod.steamnsteel.library.Blocks;
import mod.steamnsteel.library.Constants;
import mod.steamnsteel.library.Items;
import mod.steamnsteel.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mod.steamnsteel.proxy.Proxies;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, useMetadata = true, guiFactory = Constants.MOD_GUI_FACTORY)
public class ThisMod
{
    @SuppressWarnings({"StaticVariableOfConcreteClass", "StaticNonFinalField", "PublicField", "StaticVariableMayNotBeInitialized"})
    @Mod.Instance
    public static ThisMod instance;

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        PacketHandler.init();

        Items.init();

        Blocks.init();
    }

    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(ConfigurationHandler.INSTANCE);
        Recipes.init();
        Proxies.render.init();
    }

    @Mod.EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event)
    {
        // TODO: Handle interaction with other mods, complete your setup based on this.
    }
}