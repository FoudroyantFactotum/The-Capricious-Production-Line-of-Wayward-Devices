package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.structure.PlayerPiano;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;

import java.io.IOException;

public class PlayerPianoModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(PlayerPiano.NAME));
    private final IModel model;

    public PlayerPianoModel() throws IOException { model = OBJLoader.instance.loadModel(MODEL); }

    public void render() { model.renderAll(); }

    public void renderBlackKey() { model.renderOnly("BlackKey_Cube.004"); }

    public void renderWhite(){ model.renderOnly("WhiteKey_Cube.003");}

    public void renderPlayerPiano() { model.renderOnly("PlayerPiano_Cube.002");}

    public void renderSheet() { model.renderOnly("PianoRoll_Plane");}

    private static final String MODEL_LOCATION = "models/";
    private static final String MODEL_FILE_EXTENSION = ".obj";

    static ResourceLocation getResourceLocation(String path)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), path);
    }

    protected static String getModelPath(String name)
    {
        return MODEL_LOCATION + name + MODEL_FILE_EXTENSION;
    }
}
