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

package mod.steamnsteel.block.resource.ore;

import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.configuration.Settings;

public class ZincOre extends SteamNSteelOreBlock
{
    public static final String NAME = "oreZinc";

    public ZincOre()
    {
        setUnlocalizedName(NAME);
        setHarvestLevel("pickaxe", 1); // stone pick
    }

    @Override
    public boolean isGenEnabled()
    {
        return Settings.World.isZincGenerated();
    }
}
