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
 * this program; if not, see <http://   .gnu.org/licenses>.
 */
package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.BoilerTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BoilerBlock extends SteamNSteelStructureBlock
{
    public static final String NAME = "boiler";

    @SideOnly(Side.CLIENT)
    private static float rndRC()
    {
        return ((float)Math.random())*1.0f-0.5f;
    }

    public BoilerBlock()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
                        .withProperty(propMirror, false)
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {
        final int x = coord.x;
        final int y = coord.y;
        final int z = coord.z;

        final IBlockState block = getPattern().getBlock(te.getLocal());

        if (block != null)
        {
            for (int i = 0; i < 5; ++i)
            {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, x + rndRC(), y + 1, z + rndRC(), sx, sy, sz);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, x, y + 0.5, z, sx, sy, sz);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, x + rndRC(), y, z + rndRC(), sx, sy, sz);
            }
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new BoilerTE(getPattern(), (EnumFacing)state.getValue(BlockDirectional.FACING), (Boolean)state.getValue(propMirror));
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, BlockPos callPos, EnumFacing side, TripleCoord sbID, float sx, float sy, float sz)
    {
        Logger.info("Active: " + world.getTileEntity(pos));
        return super.onStructureBlockActivated(world, pos, player, callPos, side, sbID, sx, sy, sz);
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'p', "steamnsteel:blockPlotonium",
                's', "steamnsteel:blockSteel",
                'g', "minecraft:glass_pane",
                'f', "minecraft:fire",
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "ppp",
                        "sss",
                        "ppp"
                },
                new String[]{
                        "ppp",
                        "sws",
                        "pgp"
                },
                new String[]{
                        "ppp",
                        "sfs",
                        "pgp"
                },
                new String[]{
                        "ppp",
                        "sss",
                        "ppp"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(1,2,2));

        builder.setConfiguration(TripleCoord.of(0,0,0),
                new String[]{
                        "M--",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.7f,3.5f,0.7f, 2.3f,4.0f,2.3f},
                new float[]{0.3f,3.0f,0.3f, 2.7f,3.5f,2.7f},
                new float[]{0.0f,1.0f,0.0f, 3.0f,3.0f,3.0f},
                new float[]{0.3f,0.5f,0.3f, 2.7f,1.0f,2.7f},
                new float[]{0.7f,0.0f,0.7f, 2.3f,0.5f,2.3f}
        );

        return builder;
    }


}
