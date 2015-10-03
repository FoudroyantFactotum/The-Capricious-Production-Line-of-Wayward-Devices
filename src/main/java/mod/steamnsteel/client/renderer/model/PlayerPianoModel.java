package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.structure.PlayerPiano;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class PlayerPianoModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(PlayerPiano.NAME));
    private final IModelCustom model;

    public PlayerPianoModel() { model = AdvancedModelLoader.loadModel(MODEL); }

    public void render() { model.renderAll(); }

    public void renderBlackKey() { model.renderOnly("BlackKey_Cube.004"); }

    public void renderWhite(){ model.renderOnly("WhiteKey_Cube.003");}

    public void renderPlayerPiano() { model.renderOnly("PlayerPiano_Cube.002");}

    public void renderSheet() { model.renderOnly("PianoRoll_Plane");}
}
