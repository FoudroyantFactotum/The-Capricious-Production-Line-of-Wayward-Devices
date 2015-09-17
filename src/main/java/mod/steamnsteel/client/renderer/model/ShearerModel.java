package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.structure.ShearerStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ShearerModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(ShearerStructure.NAME));
    private final IModelCustom model;

    public ShearerModel() { model = AdvancedModelLoader.loadModel(MODEL); }

    public void render() { model.renderAll(); }

    public void renderArms() {model.renderPart("arms_Cube.006");}
    public void renderBody() {model.renderPart("Sides_Cube.005");}
    public void renderWheel() {model.renderPart("wheel_Circle");}
    public void renderBobby() {model.renderPart("topBlock_Cube.007");}
}
