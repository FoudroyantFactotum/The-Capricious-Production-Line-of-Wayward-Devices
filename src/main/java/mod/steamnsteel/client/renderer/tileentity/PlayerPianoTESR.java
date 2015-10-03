package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.block.structure.PlayerPiano;
import mod.steamnsteel.client.renderer.model.PlayerPianoModel;
import mod.steamnsteel.tileentity.structure.PlayerPianoTE;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

import java.util.BitSet;

public class PlayerPianoTESR extends SteamNSteelTESR
{
    public static final ResourceLocation TEXTURE = getResourceLocation(PlayerPiano.NAME);
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(-0.5f, 0.0f, 0.5f);
    private static final BitSet blackKeyNo = new BitSet(88);

    private final PlayerPianoModel model = new PlayerPianoModel();

    public PlayerPianoTESR()
    {
        int[] bk = {1,4,6,9,11,13,16,18,21,23,25,28,30,33,35,37,40,42,45,47,49,52,54,57,59,61,64,66,69,71,73,76,78,81,83,85};

        for (int v: bk)
        {
            blackKeyNo.set(v, true);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof PlayerPianoTE)
        {
            final PlayerPianoTE te = (PlayerPianoTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) x, (float) y, (float) z);

            renderPlayerPiano(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderPlayerPiano(PlayerPianoTE te)
    {
        final int x = te.xCoord;
        final int y = te.yCoord;
        final int z = te.zCoord;
        final World world = te.getWorldObj();

        GL11.glPushMatrix();

        // Inherent adjustments to model, center.
        GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(0.5f,0,0.5f);

        // Orient the model to match the placement
        final int metadata = world.getBlockMetadata(x, y, z);
        final Orientation orientation = Orientation.getdecodedOrientation(metadata);

        // If block is mirrored, flip faces and scale along -Z
        if ((metadata & SteamNSteelStructureBlock.flagMirrored) != 0) {
            GL11.glFrontFace(GL11.GL_CW);
            GL11.glScalef(1, 1, -1);

            //rotate then translate offset
            GL11.glRotatef(getAngleFromOrientation(orientation), 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(OFFSET.left, OFFSET.middle, OFFSET.right*3);

        } else {
            //rotate then translate offset
            GL11.glRotatef(getAngleFromOrientation(orientation), 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(OFFSET.left, OFFSET.middle, OFFSET.right);
        }

        // Bind the texture
        bindTexture(TEXTURE);

        // Render
        model.renderPlayerPiano();

        //lets play a game.... 88 keys on the piano. take one down and burn it. 87 keys on the piano.....
        int bkc = 0;
        for(int i=0; i<te.keyPosY.length; ++i)
        {
            float bkOfset = bkc*0.036f;
            if (blackKeyNo.get(i))
            {
                ++bkc;
                GL11.glTranslatef(0.036f* i -0.018f-bkOfset-0.054f-0.036f, te.keyPosY[i], 0.0f);
                model.renderBlackKey();
                GL11.glTranslatef(-0.036f * i + 0.018f + bkOfset+0.054f+0.036f , -te.keyPosY[i], 0.0f);
            } else
            {
                GL11.glTranslatef(-1.836f+0.036f*i-bkOfset, te.keyPosY[i], 0.0f);
                model.renderWhite();
                GL11.glTranslatef(1.836f - 0.036f * i + bkOfset, -te.keyPosY[i], 0.0f);
            }
        }


        final TextureManager texturemanager = field_147501_a.field_147553_e;
        final int tex = texturemanager.getTexture(te.texturePath).getGlTextureId();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        //GL11.glTexParameter(tex,GL11.GL_);
        //SheetMusic!!!!
        //model.renderSheet();
        final double displayAmount = 500/8048.0;
        final double shift = te.songReadHeadPos;
        final Tessellator tess = Tessellator.instance;
        GL11.glTranslatef(0.0f, 0.8f, -1.8f);
        tess.startDrawingQuads();
        {
            tess.addVertexWithUV(1.33031, 0.66802,1.350171,1,0.0+shift);
            tess.addVertexWithUV(0.7103, 0.66802, 1.350171,0,0.0+shift);
            tess.addVertexWithUV(0.67103, 0.46830, 1.25589,0,-0.5*displayAmount+shift);
            tess.addVertexWithUV(1.33031, 0.46830,1.25589,1,-0.5*displayAmount+shift);

            tess.addVertexWithUV(0.7103, 0.66802, 1.350171,0,0.0*displayAmount+shift);
            tess.addVertexWithUV(1.33031, 0.66802,1.350171,1,0.0*displayAmount+shift);
            tess.addVertexWithUV(1.33031, 0.46830+0.41802,1.25589, 1,0.5*displayAmount+shift);
            tess.addVertexWithUV(0.7103, 0.46830+0.41802, 1.25589,0,0.5*displayAmount+shift);
        }
        tess.draw();
        GL11.glTranslatef(0.0f, -0.8f, 1.8f);

        //I think It needs some torches.

        // Flip faces back to default
        GL11.glFrontFace(GL11.GL_CCW);

        // Close Render Buffer
        GL11.glPopMatrix();
    }
}
