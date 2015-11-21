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

package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.container.RemnantRuinChestBlock;
import mod.steamnsteel.tileentity.RemnantRuinChestTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class PlotoniumChestTESR extends SteamNSteelTESR
{
    public static final ResourceLocation TEXTURE = getResourceLocation(RemnantRuinChestBlock.NAME);
    private static final ModelChest vanillaChest = new ModelChest();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float tick, int whatDoesThisDo)
    {
        if (tileEntity instanceof RemnantRuinChestTE)
        {
            final RemnantRuinChestTE te = (RemnantRuinChestTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            //noinspection NumericCastThatLosesPrecision
            GL11.glTranslatef((float) posX, (float) posY + 1.0F, (float) posZ + 1.0F);

            renderPlotoniumChest(te, tick);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderPlotoniumChest(RemnantRuinChestTE te, float tick)
    {
        final World world = te.getWorld();
        final BlockPos pos = te.getPos();
        GL11.glPushMatrix();

        // Position Renderer
        bindTexture(TEXTURE);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(1.0F, -1.0F, -1.0F);  //flip & rotate
        GL11.glTranslatef(0.5F, 0.5F, 0.5F); //translate block pos around fromBLK ORG

        final IBlockState metadata = world.getBlockState(pos);
        final Orientation orientation = Orientation.getdecodedOrientation(metadata);
        GL11.glRotatef(getAngleFromOrientation(orientation), 0.0F, -1.0F, 0.0F);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F); //translate BLK ORG to block pos

        //lid angle.
        float adjLDAngle = te.getPrevLidAngle() + (te.getLidAngle() - te.getPrevLidAngle()) * tick;
        adjLDAngle = 1.0F - adjLDAngle;
        adjLDAngle = 1.0F - adjLDAngle * adjLDAngle * adjLDAngle;
        //noinspection NumericCastThatLosesPrecision
        vanillaChest.chestLid.rotateAngleX = -(adjLDAngle * (float) Math.PI / 2.0F);

        vanillaChest.renderAll();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
