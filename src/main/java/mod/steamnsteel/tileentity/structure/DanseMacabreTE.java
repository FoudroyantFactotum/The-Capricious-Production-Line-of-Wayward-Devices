package mod.steamnsteel.tileentity.structure;

import mod.steamnsteel.block.structure.DanseMacabreStructure;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class DanseMacabreTE extends SteamNSteelStructureTE
{
    public volatile Boolean keepAlive = true;

    public volatile double bellRingersPos1 = 0.0;
    public volatile double bellRingersPos2 = 0.0;
    public volatile boolean bellRingers1isActive = false;
    public volatile boolean bellRingers2isActive = false;
    public volatile boolean whichOneTwoMove = false;

    public volatile double bellAmplitude = 0.0;
    public volatile double bellValue = 0.0;
    public volatile double bellAngle = 0.0;

    public volatile boolean[] smokeOnTimpani = new boolean[88];
    public volatile boolean[] smokeOnStringEnsemble = new boolean[88];
    public volatile boolean[] smokeOnFiddle = new boolean[88];
    public volatile boolean[] smokeOnBass = new boolean[88];

    public DanseMacabreTE()
    {
        //noop
    }

    public DanseMacabreTE (int meta)
    {
        super(meta);
    }

    //================================================================
    //                     I T E M   I N P U T
    //================================================================

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        //noop
    }

    @Override
    public String getInventoryName()
    {
        return SteamNSteelTE.containerName(DanseMacabreStructure.NAME);
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return -1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    public void openInventory()
    {
        //no op
    }

    @Override
    public void closeInventory()
    {
        //no op
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return false;
    }

    @Override
    public boolean canStructureInsertItem(int slot, ItemStack item, int side, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public boolean canStructureExtractItem(int slot, ItemStack item, int side, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromStructureSide(int side, TripleCoord blockID)
    {
        return new int[0];
    }

    //================================================================
    //                  F L U I D   H A N D L E R
    //================================================================

    @Override
    public boolean canStructureFill(ForgeDirection from, Fluid fluid, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public boolean canStructureDrain(ForgeDirection from, Fluid fluid, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public int structureFill(ForgeDirection from, FluidStack resource, boolean doFill, TripleCoord blockID)
    {
        return 0;
    }

    @Override
    public FluidStack structureDrain(ForgeDirection from, FluidStack resource, boolean doDrain, TripleCoord blockID)
    {
        return null;
    }

    @Override
    public FluidStack structureDrain(ForgeDirection from, int maxDrain, boolean doDrain, TripleCoord blockID)
    {
        return null;
    }

    @Override
    public FluidTankInfo[] getStructureTankInfo(ForgeDirection from, TripleCoord blockID)
    {
        return emptyFluidTankInfo;
    }
    //================================================================
    //                 P I P E   C O N E C T I O N
    //================================================================

    @Override
    public boolean isStructureSideConnected(ForgeDirection opposite, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public boolean tryStructureConnect(ForgeDirection opposite, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public boolean canStructureConnect(ForgeDirection opposite, TripleCoord blockID)
    {
        return false;
    }

    @Override
    public void disconnectStructure(ForgeDirection opposite, TripleCoord blockID)
    {

    }

    //================================================================
    //                            N B T
    //================================================================

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

    }

    @Override
    protected void transformDirectionsOnLoad()
    {

    }
}
