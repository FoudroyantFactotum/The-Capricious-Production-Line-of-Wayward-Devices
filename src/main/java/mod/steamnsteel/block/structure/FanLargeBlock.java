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

package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.client.model.opengex.OpenGEXAnimationFrameProperty;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.FanLargeTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.block.BlockDirectional.FACING;

public class FanLargeBlock extends SteamNSteelStructureBlock
{
    public static final PropertyBool RENDER_DYNAMIC = PropertyBool.create("render-dynamic");

    public static final String NAME = "fanLarge";

    public static int counter;

    public FanLargeBlock()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                blockState
                        .getBaseState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(propMirror, false)
                        .withProperty(RENDER_DYNAMIC, false)
        );
    }

    @Override
    protected BlockState createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[]{FACING, propMirror, RENDER_DYNAMIC}, new IUnlistedProperty[]{OpenGEXAnimationFrameProperty.instance});
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, BlockPos callPos, EnumFacing side, TripleCoord sbID, float sx, float sy, float sz)
    {
        if (world.isRemote)
        {
            Logger.info("click " + counter);

            if (player.isSneaking())
                --counter;
            else
                ++counter;

            //if(counter >= model.getNode().getKeys().size()) counter = 0;
            world.markBlockRangeForRenderUpdate(pos, pos);
        }

        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new FanLargeTE(getPattern(), (EnumFacing)state.getValue(BlockDirectional.FACING), (Boolean)state.getValue(propMirror));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {
        /*final int x = coord.getX();
        final int y = coord.getY();
        final int z = coord.getZ();

        final Block block = coord.getStructureDefinition().getBlock(coord.getLX(), coord.getLY(), coord.getLZ());

        if (block != null)
        {
            for (int i = 0; i < 5; ++i)
            {
                world.spawnParticle("explode", x + rndRC(), y + 1, z + rndRC(), sx, sy, sz);
                world.spawnParticle("explode", x, y + 0.5, z, sx, sy, sz);
                world.spawnParticle("explode", x + rndRC(), y, z + rndRC(), sx, sy, sz);
            }
        }*/
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'c', "minecraft:cobblestone",
                's', "minecraft:sand"
        ));
        builder.assignConstructionBlocks(
                new String[]{
                        "ccc"
                },
                new String[]{
                        "csc"
                },
                new String[]{
                        "ccc"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(1,1,0));

        builder.setConfiguration(TripleCoord.of(0,0,0),
                new String[]{
                        "---"
                },
                new String[]{
                        "-M-"
                },
                new String[]{
                        "---"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,0.0f, 3.0f,3.0f,1.0f}
        );

        return builder;
    }

}

