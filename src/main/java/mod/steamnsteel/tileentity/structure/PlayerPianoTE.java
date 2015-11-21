package mod.steamnsteel.tileentity.structure;

import mod.steamnsteel.structure.registry.StructureDefinition;
import mod.steamnsteel.utility.midi.MidiWorker;
import mod.steamnsteel.utility.midi.SongList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class PlayerPianoTE extends StructureTemplate
{
    public volatile float[] keyPosY = new float[88];
    public volatile boolean[] keyIsDown = new boolean[88];
    public String songLoaded = "TemptationRag(1909)";
    public ResourceLocation texturePath = SongList.getSongTexturePath(songLoaded);
    public volatile double songReadHeadPos = 0.0;
    public MidiWorker midiWorker;

    public PlayerPianoTE()
    {
        initKeys();
    }

    public PlayerPianoTE(StructureDefinition sd, EnumFacing orientation, boolean mirror)
    {
        super(sd, orientation, mirror);
        initKeys();
    }

    private void initKeys()
    {
        for (int i=0; i< keyPosY.length; ++i)
        {
            keyPosY[i] = 0;
        }
        midiWorker = new MidiWorker(this);
    }
}
