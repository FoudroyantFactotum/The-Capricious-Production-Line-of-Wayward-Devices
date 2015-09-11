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

    public void renderAll() { model.renderAll(); }
    public void render

}
