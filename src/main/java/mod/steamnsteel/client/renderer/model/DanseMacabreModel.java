package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.structure.DanseMacabreStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class DanseMacabreModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(DanseMacabreStructure.NAME));
    private final IModelCustom model;

    public DanseMacabreModel()
    {
        model = AdvancedModelLoader.loadModel(MODEL);
    }

    public void renderAll()
    {
        model.renderAll();
    }

    public void renderStage()
    {
        model.renderOnly("Stage");
    }

    public void renderBell()
    {
        model.renderOnly("Bell");
    }
}
