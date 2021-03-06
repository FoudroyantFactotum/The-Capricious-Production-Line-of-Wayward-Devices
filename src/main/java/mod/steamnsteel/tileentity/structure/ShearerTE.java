package mod.steamnsteel.tileentity.structure;

public class ShearerTE extends StructureTemplate
{
    /*
    private final TripleCoord pipeConnectLocation = TripleCoord.of(0,0,0);
    public float wheelRotation = 0.0f;
    public boolean isActive = false;
    public EntitySheep darnSheep = null;
    public boolean coolDown = false;
    public float armPos = 0.0f;

    public ShearerTE()
    {
        //noop
    }

    public ShearerTE(int meta)
    {
        super(meta);
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

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {

    }

    @Override
    public String getInventoryName()
    {
        return SteamNSteelTE.containerName(ShearerStructure.NAME);
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return false;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return false;
    }

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
        return opposite == localToGlobal(ForgeDirection.WEST, getdecodedOrientation(getBlockMetadata()), false) && pipeConnectLocation.equals(blockID);
    }

    @Override
    public void disconnectStructure(ForgeDirection opposite, TripleCoord blockID)
    {

    }

    @Override
    public void updateEntity()
    {
        wheelRotation = (wheelRotation > 360)? 0 : wheelRotation + 1;

        if (wheelRotation % 2 == 0) {
            final TripleCoord testLoc
                    = localToGlobal(-2, -1, 0, xCoord, yCoord, zCoord,
                    getdecodedOrientation(getBlockMetadata()), isMirrored(getBlockMetadata()),
                    getMasterBlockInstance().getPattern().getBlockBounds());

            final Block blk = getWorldObj().getBlock(testLoc.x, testLoc.y, testLoc.z);

            if (!isActive && !coolDown && darnSheep == null && getWorldObj().isBlockIndirectlyGettingPowered(testLoc.x, testLoc.y, testLoc.z)) {

                final TripleCoord lb
                        = localToGlobal(0, -2, 1, xCoord, yCoord, zCoord,
                        getdecodedOrientation(getBlockMetadata()), isMirrored(getBlockMetadata()),
                        getMasterBlockInstance().getPattern().getBlockBounds());

                final TripleCoord ub
                        = localToGlobal(2, 0, -1, xCoord, yCoord, zCoord,
                        getdecodedOrientation(getBlockMetadata()), isMirrored(getBlockMetadata()),
                        getMasterBlockInstance().getPattern().getBlockBounds());

                if (lb.x > ub.x)
                {
                    int tmp = lb.x;
                    lb.x = ub.x;
                    ub.x = tmp;
                }

                if (lb.z > ub.z)
                {
                    int tmp = lb.z;
                    lb.z = ub.z;
                    ub.z = tmp;
                }

                final AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(lb.x, lb.y, lb.z, ub.x, ub.y, ub.z);

                final List<EntitySheep> sheepLst
                        = worldObj.getEntitiesWithinAABB(EntitySheep.class, bb);

                //Logger.info("SheepCheckLoc = " + bb + sheepLst);

                if (!sheepLst.isEmpty()) {
                    EntitySheep sheep = sheepLst.get(0);
                    if (sheep.isShearable(null, null, -1, -1, -1)) {
                        darnSheep = sheep;
                        isActive = true;
                    }
                }
            }
        }

        if (darnSheep != null && darnSheep.isDead) {
            darnSheep = null;
            isActive = false;
            coolDown = true;
        }

        if (darnSheep != null) {
            final TripleCoord p
                    = localToGlobal(2, 0, 0, xCoord, yCoord, zCoord,
                    getdecodedOrientation(getBlockMetadata()), isMirrored(getBlockMetadata()),
                    getMasterBlockInstance().getPattern().getBlockBounds());

            darnSheep.setPositionAndRotation(p.x+0.5f, darnSheep.posY, p.z+0.5f, (float) getdecodedOrientation(getBlockMetadata()).getRotationValue(), 0.0f);

            if (armPos == 40 && isActive)
            {
                coolDown = true;
                if (!getWorldObj().isRemote) {
                    ArrayList<ItemStack> drops = darnSheep.onSheared(null, null, -1, -1, -1, -1);

                    Random rand = new Random();
                    for (ItemStack stack : drops) {
                        EntityItem ent = new EntityItem(getWorldObj(), darnSheep.posX, darnSheep.posY, darnSheep.posZ, stack);
                        ent.motionY += rand.nextFloat() * 0.05F;
                        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;

                        getWorldObj().spawnEntityInWorld(ent);
                    }
                }
            }
        }

        if (isActive) {
            if (armPos < 60)
                armPos += 1;
            else if (coolDown) {
                isActiv //================================================================
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
        return SteamNSteelTE.containerName(PlayerPiano.NAME);
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

    }e = false;
            }
        } else {
            if (armPos > 1)
                armPos -= 1;
            else if (coolDown)
                coolDown = false;
            else
                darnSheep = null;
        }

    }

    @Override
    public boolean canStructureFill(ForgeDirection from, Fluid fluid, TripleCoord blockID) {
        return false;
    }

    @Override
    public boolean canStructureDrain(ForgeDirection from, Fluid fluid, TripleCoord blockID) {
        return false;
    }

    @Override
    public int structureFill(ForgeDirection from, FluidStack resource, boolean doFill, TripleCoord blockID) {
        return 0;
    }

    @Override
    public FluidStack structureDrain(ForgeDirection from, FluidStack resource, boolean doDrain, TripleCoord blockID) {
        return null;
    }

    @Override
    public FluidStack structureDrain(ForgeDirection from, int maxDrain, boolean doDrain, TripleCoord blockID) {
        return null;
    }

    @Override
    public FluidTankInfo[] getStructureTankInfo(ForgeDirection from, TripleCoord blockID) {
        return new FluidTankInfo[0];
    }*/
}
