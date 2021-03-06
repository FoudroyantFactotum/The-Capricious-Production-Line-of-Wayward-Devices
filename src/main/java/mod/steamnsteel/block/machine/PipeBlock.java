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

package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.tileentity.PipeTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PipeBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public static final String NAME = "pipe";
    private static int RenderId;

    public PipeBlock()
    {
        super(Material.circuits, true);
        setUnlocalizedName(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PipeTE();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        PipeTE entity = (PipeTE)world.getTileEntity(pos);
        entity.checkEnds();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (playerIn != null) {
            ItemStack itemInUse = playerIn.inventory.mainInventory[playerIn.inventory.currentItem];
            if (itemInUse != null && itemInUse.getItem() == Items.bone)
            {
                if (!worldIn.isRemote) {
                    PipeTE entity = (PipeTE) worldIn.getTileEntity(pos);
                    entity.rotatePipe();
                }
                return true;
            }
            /*if (itemInUse != null && itemInUse.getItem() == Items.name_tag) {
                BasePlumbingTE entity = (BasePlumbingTE) world.getTileEntity(pos);
                Logger.info("%s - Entity Check - %s", world.isRemote ? "client" : "server", entity.toString());
            }*/
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote) {
            PipeTE entity = (PipeTE) worldIn.getTileEntity(pos);
            if (entity != null)
            {
                entity.detach();
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PipeTE)
        {
            PipeTE te = (PipeTE)tileEntity;

            EnumFacing direction = EnumFacing.EAST;
            int facing = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0)
            {
                direction = EnumFacing.NORTH;
            }
            else if (facing == 1)
            {
                direction = EnumFacing.EAST;
            }
            else if (facing == 2)
            {
                direction = EnumFacing.SOUTH;
            }
            else if (facing == 3)
            {
                direction = EnumFacing.WEST;
            }

            te.setOrientation(direction);
            te.checkEnds();
        }
    }
}