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
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.BallMillTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BallMillBlock extends SteamNSteelStructureBlock
{
    public static final String NAME = "ballMill";

    public BallMillBlock()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
                        .withProperty(propMirror, false)
        );
    }

    private static float rndRC()
    {
        return ((float)Math.random())*1.0f-0.5f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {
        final float x = coord.x + 0.5f;
        final float y = coord.y + 0.5f;
        final float z = coord.z + 0.5f;

        for (int i = 0; i < 1; ++i)
        {
            //world.spawnParticle("explode", x + rndRC(), y + 1, z + rndRC(), sx, sy, sz);
            //world.spawnParticle("largesmoke", x, y, z, sx, sy, sz);
            //world.spawnParticle("explode", x + rndRC(), y, z + rndRC(), sx, sy, sz);
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
        return new BallMillTE(getPattern(), (EnumFacing)state.getValue(BlockDirectional.FACING), (Boolean)state.getValue(propMirror));
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'b', "steamnsteel:blockBrass",
                'S', "steamnsteel:blockSteel"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "S S S",
                        "    -"
                },
                new String[]{
                        "     ",
                        "  b  "
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(2,1,1));

        builder.setConfiguration(TripleCoord.of(0,0,0),
                new String[]{
                        "-M---",
                        "---- "
                },
                new String[]{
                        "-----",
                        "-----"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,0.0f, 1.0f,1.49f,2.0f},
                new float[]{1.0f,0.0f,0.2f, 3.5f,1.77f,1.8f},
                new float[]{3.5f,0.0f,0.0f, 4.0f,1.49f,2.0f},
                new float[]{4.0f,0.0f,0.0f, 4.6f,1.49f,1.0f},
                new float[]{4.6f,0.0f,0.0f, 5.0f,1.1f ,1.0f},
                new float[]{3.5f,1.1f,1.1f, 5.0f,1.9f ,1.9f}
        );

        return builder;
    }
}
