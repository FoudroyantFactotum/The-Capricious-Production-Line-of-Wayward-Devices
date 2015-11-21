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
package mod.steamnsteel.tileentity.structure;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.IStructure.IStructureFluidHandler;
import mod.steamnsteel.structure.IStructure.IStructurePipe;
import mod.steamnsteel.structure.IStructure.IStructureSidedInventory;
import mod.steamnsteel.structure.IStructure.IStructureTE;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.structure.net.StructurePacket;
import mod.steamnsteel.structure.registry.GeneralBlock.IGeneralBlock;
import mod.steamnsteel.structure.registry.StructureDefinition;
import mod.steamnsteel.structure.registry.StructureRegistry;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static mod.steamnsteel.block.SteamNSteelStructureBlock.ORIGIN;
import static mod.steamnsteel.block.SteamNSteelStructureBlock.propMirror;
import static mod.steamnsteel.structure.coordinates.TransformLAG.flagEnumFacing;
import static mod.steamnsteel.structure.coordinates.TransformLAG.localToGlobalBoundingBox;

public abstract class SteamNSteelStructureTE extends SteamNSteelTE implements IStructureTE, IStructureSidedInventory, IStructureFluidHandler, IStructurePipe
{
    static final int maskBlockID = 0x00FFFFFF;

    static final String BLOCK_INFO = "blockINFO";
    static final String BLOCK_PATTERN_NAME = "blockPatternHash";

    private TripleCoord local = ORIGIN;
    private int definitionHash = -1;

    protected EnumFacing orientation;
    protected boolean mirror;

    private Optional<AxisAlignedBB> renderBounds = Optional.absent();

    public SteamNSteelStructureTE()
    {
        //noop
    }

    public SteamNSteelStructureTE(StructureDefinition sd, EnumFacing orientation, boolean mirror)
    {
        this.orientation = orientation;
        this.mirror = mirror;

        transformDirectionsOnLoad(sd);
    }

    //================================================================
    //                 S T R U C T U R E   C O N F I G
    //================================================================

    @Override
    public SteamNSteelStructureBlock getMasterBlockInstance()
    {
        return StructureRegistry.getStructureBlock(definitionHash);
    }

    @Override
    public TripleCoord getMasterBlockLocation()
    {
        return TripleCoord.of(pos);
    }
    @Override
    public int getRegHash()
    {
        return definitionHash;
    }

    @Override
    public void configureBlock(TripleCoord local, int definitionHash)
    {
        this.definitionHash = definitionHash;
        this.local = local;
    }

    @Override
    public IBlockState getTransmutedBlock()
    {
        SteamNSteelStructureBlock sb = StructureRegistry.getStructureBlock(definitionHash);

        if (sb != null)
        {
            IBlockState block = sb.getPattern().getBlock(local);
            return block == null || block instanceof IGeneralBlock ?
                    Blocks.air.getDefaultState() :
                    block;
        }

        return Blocks.air.getDefaultState();
    }

    @Override
    public TripleCoord getLocal()
    {
        return local;
    }

    public EnumFacing getOrientation()
    {
        return orientation;
    }

    public boolean getMirror()
    {
        return mirror;
    }

    //================================================================
    //                     I T E M   I N P U T
    //================================================================

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side)
    {
        return canStructureInsertItem(slotIndex, itemStack, side, local);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side)
    {
        return canStructureExtractItem(slotIndex, itemStack, side, local);
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    //================================================================
    //                  F L U I D   H A N D L E R
    //================================================================

    public static final FluidTankInfo[] emptyFluidTankInfo = {};

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        return structureFill(from, resource, doFill, local);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return structureDrain(from, resource, doDrain, local);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return structureDrain(from, maxDrain, doDrain, local);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return canStructureFill(from, fluid, local);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return canStructureDrain(from, fluid, local);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return getStructureTankInfo(from, local);
    }

    //================================================================
    //                 P I P E   C O N E C T I O N
    //================================================================

    @Override
    public boolean isSideConnected(EnumFacing opposite)
    {
        return isStructureSideConnected(opposite, local);
    }

    @Override
    public boolean tryConnect(EnumFacing opposite)
    {
        return tryStructureConnect(opposite, local);
    }

    @Override
    public boolean canConnect(EnumFacing opposite)
    {
        return canStructureConnect(opposite, local);
    }

    @Override
    public void recalculateVisuals()
    {
        //noop
    }

    @Override
    public void disconnect(EnumFacing opposite)
    {
        disconnectStructure(opposite, local);
    }

    //================================================================
    //                            N B T
    //================================================================

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);

        return new S35PacketUpdateTileEntity(pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        final int blockInfo = nbt.getInteger(BLOCK_INFO);
        definitionHash = nbt.getInteger(BLOCK_PATTERN_NAME);

        local = TripleCoord.dehashLoc(blockInfo & maskBlockID);

        orientation = EnumFacing.VALUES[blockInfo >> 24 & 0x7];
        mirror = (blockInfo >> 24 & StructurePacket.flagMirrored) != 0;

        transformDirectionsOnLoad(getMasterBlockInstance().getPattern());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger(BLOCK_INFO, local.hashCode() | (orientation.ordinal() | (mirror ? StructurePacket.flagMirrored:0)) << 24);
        nbt.setInteger(BLOCK_PATTERN_NAME, definitionHash);
    }

    protected void transformDirectionsOnLoad(StructureDefinition sd) { }


    //================================================================
    //                    C L I E N T   S I D E
    //================================================================

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (!renderBounds.isPresent())
        {
            final SteamNSteelStructureBlock sb = StructureRegistry.getStructureBlock(definitionHash);

            if (sb == null)
            {
                return INFINITE_EXTENT_AABB;
            }

            final IBlockState state = getWorld().getBlockState(pos);
            final EnumFacing orientation = (EnumFacing)state.getValue(BlockDirectional.FACING);
            final boolean mirror = (Boolean)state.getValue(propMirror);

            renderBounds = Optional.of(localToGlobalBoundingBox(pos, local, sb.getPattern(), orientation, mirror));
        }

        return renderBounds.get();
    }

    //================================================================
    //              S t r u c t u r e   H e l p e r s
    //================================================================

    public static boolean isSide(int flag, EnumFacing d)
    {
        return (flag & flagEnumFacing(d)) != 0;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("local", local)
                .add("renderBounds", renderBounds)
                .add("blockPatternHash", definitionHash)
                .add("mirror", mirror)
                .add("orientation", orientation)
                .toString();
    }
}
