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
import com.google.common.collect.ImmutableMap.Builder;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.BlastFurnaceTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlastFurnaceBlock extends SteamNSteelStructureBlock
{
    public static final String NAME = "blastFurnace";

    @SideOnly(Side.CLIENT)
    private static float rndRC()
    {
        return ((float)Math.random())*1.0f-0.5f;
    }

    public BlastFurnaceBlock()
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
        /*final int x = coord.getX();
        final int y = coord.getY();
        final int z = coord.getZ();

        for (int i = 0; i < 5; ++i) {
            world.spawnParticle("explode", x + rndRC(), y + 0.5,  z + rndRC(), sx, sy, sz);
        }*/
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new BlastFurnaceTE(getPattern(), (EnumFacing)state.getValue(BlockDirectional.FACING), (Boolean)state.getValue(propMirror));
    }


    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'b', "steamnsteel:blockBrass",
                'T', "minecraft:stone_stairs",
                'l', "minecraft:lava",
                'p', "minecraft:glass_pane"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "bbb",
                        "TbT",
                        "TTT"
                },
                new String[]{
                        "bbb",
                        "p p",
                        "ppp"
                },
                new String[]{
                        "bbb",
                        "TbT",
                        "TTT"
                }
        );

        final Builder<Character, String> stateList = ImmutableMap.builder();

        stateList.put('l', "facing:east,half:bottom,shape:straight");
        stateList.put('q', "facing:north,half:bottom,shape:outer_left");
        stateList.put('s', "facing:north,half:bottom,shape:straight");
        stateList.put('y', "facing:north,half:bottom,shape:outer_right");
        stateList.put('r', "facing:west,half:bottom,shape:straight");

        //inverse of above
        /*stateList.put('l', "facing:west,half:bottom,shape:straight");
        stateList.put('q', "facing:south,half:bottom,shape:inner_left");
        stateList.put('s', "facing:south,half:bottom,shape:straight");
        stateList.put('y', "facing:south,half:bottom,shape:inner_right");
        stateList.put('r', "facing:east,half:bottom,shape:straight");*/

        stateList.put('L', "facing:east,half:top,shape:straight");
        stateList.put('Y', "facing:east,half:top,shape:outer_right");
        stateList.put('S', "facing:north,half:top,shape:straight");
        stateList.put('Q', "facing:north,half:top,shape:outer_right");
        stateList.put('R', "facing:west,half:top,shape:straight");

        builder.assignStateDefinitions(stateList.build());
        builder.assignConstructionStates(
                new String[]{
                        "   ",
                        "L R",
                        "YSQ"
                },
                new String[]{
                        "   ",
                        "   ",
                        "   "
                },
                new String[]{
                        "   ",
                        "l r",
                        "ysq"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(1,1,2));

        builder.setConfiguration(TripleCoord.of(0, 0, 0),
                new String[]{
                        "M--",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---",
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,1.3f ,1.0f,0.7f,1.7f},        //left leg
                new float[]{2.0f,0.0f,1.3f ,3.0f,0.7f,1.7f},        //right leg
                new float[]{1.1f,1.1f,0.0f ,1.9f,1.9f,1.0f},        //pipe

                new float[]{0.7f,0.0f,0.7f ,2.3f,0.5f,2.3f},        //base
                new float[]{0.4f,0.5f,0.4f ,2.6f,1.0f,2.6f},

                new float[]{0.4f,0.5f,0.4f ,2.6f,1.0f,2.6f},        //top
                new float[]{0.6f,1.5f,0.6f ,2.4f,2.0f,2.4f},
                new float[]{0.7f,2.0f,0.7f ,2.3f,2.5f,2.3f},
                new float[]{1.0f,2.3f,1.0f ,2.0f,3.0f,2.0f}
        );

        return builder;
    }
}
