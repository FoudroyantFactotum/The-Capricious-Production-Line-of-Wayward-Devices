package mod.steamnsteel.client.renderer.tileentity;

public class DanseMacabreTESR //extends SteamNSteelTESR
{
    /*private ModelTwoJointSkeleton skOne = new ModelTwoJointSkeleton();
    private ModelTwoJointSkeleton skTwo = new ModelTwoJointSkeleton();
    private DanseMacabreModel strucure = new DanseMacabreModel();

    private static final ResourceLocation TEX_STAGE = getResourceLocation(DanseMacabreStructure.NAME + "stage");
    private static final ResourceLocation TEX_BELL = getResourceLocation(DanseMacabreStructure.NAME + "bell");
    private int value = 0;

    public DanseMacabreTESR()
    {

    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float tick)
    {
        GL11.glPushMatrix();

        // Position Renderer
        GL11.glTranslatef((float) x+4, (float) y, (float) z+5);

        if (!(te instanceof DanseMacabreTE)) return;
        final DanseMacabreTE dmte = (DanseMacabreTE) te;

        GL11.glPushMatrix();
        {
            GL11.glTranslatef(-4.0f, 0.0f, 1.0f);
            GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
            bindTexture(TEX_STAGE);
            strucure.renderStage();
        }
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(-1.5f, 5.25f, -1.5f);
            GL11.glRotated(dmte.bellAngle, 0, 0, 1);
            bindTexture(TEX_BELL);
            strucure.renderBell();
        }
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        {
            GL11.glRotatef(90.0f, 0.0f, -1.0f, 0.0f);
            GL11.glTranslatef(-3.5f, 3.5f, 0.0f);
            skOne.myRender(x, y, z);
            skOne.doMotion(dmte.bellRingersPos1);
        }
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        {
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(-0.5f, 3.5f, -3.0f);
            skTwo.myRender(x, y, z);
            skTwo.doMotion(dmte.bellRingersPos2);
        }
        GL11.glPopMatrix();
        // Close Render Buffer
        GL11.glPopMatrix();
    }

    private class ModelTwoJointSkeleton extends ModelBase
    {
        private ResourceLocation texture = new ResourceLocation("textures/entity/skeleton/skeleton.png");

        private ModelRenderer upperRightArm;
        private ModelRenderer lowerRightArm;

        private ModelRenderer upperLeftArm;
        private ModelRenderer lowerLeftArm;

        private ModelRenderer upperRightLeg;
        private ModelRenderer lowerRightLeg;

        private ModelRenderer upperLeftLeg;
        private ModelRenderer lowerLeftLeg;
        private ModelRenderer head;
        private ModelRenderer body;

        private ModelRenderer stickBase;

        public ModelTwoJointSkeleton()
        {
            final float scaleFactor = 0.0f;
            //super(1.0f, 0.0F, 64, 32);
            upperRightArm = new ModelRenderer(this, 40, 16);
            upperRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 6, 2, scaleFactor);
            upperRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

            lowerRightArm = new ModelRenderer(this, 40, 16);
            lowerRightArm.addBox(4.0F, -0.0F, -1.0F, 2, 6, 2, scaleFactor);
            lowerRightArm.setRotationPoint(-5.0F, 4.0F, 0.0F);
            lowerRightArm.rotateAngleX = -90;

            upperRightArm.addChild(lowerRightArm);

            //stick
            stickBase = new ModelRenderer(this, 40, 16);
            lowerRightArm.addChild(stickBase);
            stickBase.addBox(0.0f, 0.0f, 0.0f, 1, 7, 1, 0.0f);
            stickBase.setRotationPoint(4.0f, 6.0f, 0.0f);
            stickBase.rotateAngleX = -90;

            final ModelRenderer headOfStick = new ModelRenderer(this, 40, 16);
            headOfStick.addBox(0.0f, 0.0f, 0.0f, 3, 2, 3, 0.0f);
            headOfStick.setRotationPoint(-1.0f, 5.0f, 0.0f);
            stickBase.addChild(headOfStick);

            upperLeftArm = new ModelRenderer(this, 40, 16);
            upperLeftArm.mirror = true;
            upperLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, scaleFactor);
            upperLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

            upperRightLeg = new ModelRenderer(this, 0, 16);
            upperRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, scaleFactor);
            upperRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);

            upperLeftLeg = new ModelRenderer(this, 0, 16);
            upperLeftLeg.mirror = true;
            upperLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, scaleFactor);
            upperLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);

            head = new ModelRenderer(this, 0, 0);
            head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scaleFactor);
            head.setRotationPoint(0.0F, 0.0F, 0.0F);

            body = new ModelRenderer(this, 16, 16);
            body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scaleFactor);
            body.setRotationPoint(0.0F, 0.0F, 0.0F);
        }

        public void myRender(double x, double y, double z)
        {
            GL11.glDisable(GL11.GL_CULL_FACE);

            GL11.glTranslated(2.0, 1.50, 0.0);
            final double sclBy = 1 / 16.0;
            GL11.glScaled(sclBy, sclBy, sclBy);

            GL11.glRotated(180.0, 1.0, 0.0, 0.0);

            bindTexture(texture);

            final float scaleFactor = 1.0f;
            upperRightArm.render(scaleFactor);
            //lowerRightArm.render(scaleFactor);
            upperLeftArm.render(scaleFactor);
            upperLeftLeg.render(scaleFactor);
            upperRightLeg.render(scaleFactor);

            head.render(scaleFactor);
            body.render(scaleFactor);

            GL11.glEnable(GL11.GL_CULL_FACE);
        }

        public void doMotion(double pos)
        {
            upperRightArm.rotateAngleY = (float) (-0.28 * pos);
            upperRightArm.rotateAngleX = (float) (-1 * pos);
            lowerRightArm.rotateAngleX = -90 + (float) (2 * pos);
            stickBase.rotateAngleX = -90 + (float) (0.8 * pos);
        }

    }*/
}
