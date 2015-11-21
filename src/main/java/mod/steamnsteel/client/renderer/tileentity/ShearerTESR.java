package mod.steamnsteel.client.renderer.tileentity;

public class ShearerTESR //extends SteamNSteelTESR
{
    /*public static final ResourceLocation TEXTURE = getResourceLocation(ShearerStructure.NAME);
    private static ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static  ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.0f, -2.0f, 1.0f);

    private final ShearerModel model = new ShearerModel();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof ShearerTE)
        {
            final ShearerTE te = (ShearerTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) x, (float) y, (float) z);

            renderShearer(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderShearer(ShearerTE te)
    {
        final int x = te.xCoord;
        final int y = te.yCoord;
        final int z = te.zCoord;
        final World world = te.getWorldObj();

        GL11.glPushMatrix();

        // Inherent adjustments to model
        GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(0.5f,0,0.5f);

        // Orient the model to match the placement
        final int metadata = world.getBlockMetadata(x, y, z);
        final Orientation orientation = Orientation.getdecodedOrientation(metadata);

        GL11.glRotatef(getAngleFromOrientation(orientation), 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(OFFSET.left, OFFSET.middle, OFFSET.right);

        // If block is mirrored, flip faces and scale along -Z
        if ((metadata & SteamNSteelStructureBlock.flagMirrored) != 0) {
            GL11.glFrontFace(GL11.GL_CW);
            GL11.glScalef(1, 1, -1);
        }

        // Bind the texture
        bindTexture(TEXTURE);

        // Render
        //model.render();
        model.renderBody();

        GL11.glPushMatrix();
        {
            float tx = -0.36f;
            float ty = 2.22f;
            float tz = 1.0f;
            float rotval = te.armPos;
            GL11.glTranslatef(tx, ty, tz);
            GL11.glRotatef(rotval, 0.0f, 0.0f, -1.0f);
            GL11.glTranslatef(-tx, -ty, -tz);

            model.renderArms();
        }
        GL11.glPopMatrix();


        float bobbyTY = (float) Math.abs(Math.sin(Math.toRadians(te.wheelRotation*3))) * 0.2f;
        GL11.glTranslatef(0.0f, bobbyTY, 0.0f);
        model.renderBobby();
        GL11.glTranslatef(0.0f, -bobbyTY, 0.0f);

        GL11.glPushMatrix();
        {
            float tx = 1.516f;
            float ty = 3.0f;
            float tz = 0.0f;
            GL11.glTranslatef(tx, ty, tz);
            GL11.glRotatef(te.wheelRotation * 6, 0.0f, 0.0f, -1.0f);
            GL11.glTranslatef(-tx, -ty, -tz);

            model.renderWheel();
        }
        GL11.glPopMatrix();


        // Flip faces back to default
        GL11.glFrontFace(GL11.GL_CCW);

        // Close Render Buffer
        GL11.glPopMatrix();
    }*/
}
