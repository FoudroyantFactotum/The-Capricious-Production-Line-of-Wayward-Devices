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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.IStructure.IStructureTE;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.structure.registry.StructureRegistry;
import mod.steamnsteel.tileentity.structure.StructureShapeTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static mod.steamnsteel.block.SteamNSteelStructureBlock.*;
import static mod.steamnsteel.structure.coordinates.TransformLAG.localToGlobalCollisionBoxes;
import static mod.steamnsteel.utility.Orientation.getdecodedOrientation;

public class StructureShapeBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static boolean _DEBUG = false;
    public static final String NAME = "structureShape";
    public static final AxisAlignedBB EMPTY_BOUNDS = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

    public StructureShapeBlock()
    {
        setBlockName(NAME);
    }

    @Override
    public int quantityDropped(Random rnd)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return EMPTY_BOUNDS;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List boundingBoxList, Entity entityColliding)
    {
        final IStructureTE te = (IStructureTE) world.getTileEntity(x, y, z);

        if (te != null)
        {
            final int meta = world.getBlockMetadata(x, y, z);
            final TripleCoord mloc = te.getMasterBlockLocation();
            final SteamNSteelStructureBlock sb = StructureRegistry.getStructureBlock(te.getRegHash());

            if (sb == null || sb.getPattern().getCollisionBoxes() == null)
            {
                return;
            }

            localToGlobalCollisionBoxes(mloc.x, mloc.y, mloc.z,
                    aabb, boundingBoxList, sb.getPattern().getCollisionBoxes(), getdecodedOrientation(meta), isMirrored(meta), sb.getPattern().getBlockBounds());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new StructureShapeTE();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        final IStructureTE te = (IStructureTE) world.getTileEntity(x, y, z);

        if (te != null)
        {
            final SteamNSteelStructureBlock block = te.getMasterBlockInstance();

            if (block != null)
            {
                final TripleCoord mloc = te.getMasterBlockLocation();
                return block.addDestroyEffects(world, mloc.x, mloc.y, mloc.z, meta, effectRenderer);
            }
        }

        return true; //No Destroy Effects
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        final int meta = world.getBlockMetadata(x, y, z);
        final IStructureTE te = (IStructureTE) world.getTileEntity(x, y, z);
        final boolean isPlayerCreative = player != null && player.capabilities.isCreativeMode;

        final SteamNSteelStructureBlock sb = StructureRegistry.getStructureBlock(te.getRegHash());

        if (sb != null)
        {
            breakStructure(world,
                    te.getMasterBlockLocation(),
                    sb.getPattern(),
                    getdecodedOrientation(meta),
                    isMirrored(meta),
                    isPlayerCreative
            );
            updateExternalNeighbours(world,
                    te.getMasterBlockLocation(),
                    sb.getPattern(),
                    getdecodedOrientation(meta),
                    isMirrored(meta),
                    false
            );

        } else
        {
            world.setBlockToAir(x, y, z);
        }

        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float sx, float sy, float sz)
    {
        final StructureShapeTE te = (StructureShapeTE) world.getTileEntity(x, y, z);

        if (te != null)
        {
            final SteamNSteelStructureBlock block = te.getMasterBlockInstance();

            if (block != null)
            {
                final int meta = world.getBlockMetadata(x, y, z);
                final TripleCoord mloc = te.getMasterBlockLocation();

                return block.onStructureBlockActivated(world, mloc.x, mloc.y, mloc.z, player, meta, sx, sy, sz, te.getLocal(), x, y, z);
            }
        }

        world.setBlockToAir(x, y, z);

        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        onSharedNeighbourBlockChange(world, x, y, z, ((StructureShapeTE) world.getTileEntity(x, y, z)).getRegHash(), block);
    }

    //======================================
    //      V i s u a l   D e b u g
    //======================================

    @Override
    public boolean renderAsNormalBlock() {
        return !_DEBUG && super.renderAsNormalBlock();
    }

    @Override
    public int getRenderType()
    {
        return _DEBUG?0:super.getRenderType();
    }

    @Override
    public int getRenderBlockPass() {
        return _DEBUG?1:super.getRenderType();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return !_DEBUG && super.isOpaqueCube();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return _DEBUG?Blocks.stained_glass.getIcon(side, meta): super.getIcon(side, meta);
    }
}
