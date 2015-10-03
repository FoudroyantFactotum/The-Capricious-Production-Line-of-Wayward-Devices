package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.midi.MidiWorker;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.PlayerPianoTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PlayerPiano extends SteamNSteelStructureBlock
{
    public static final String NAME = "playerpiano";
    public PlayerPiano()
    {
        setBlockName(NAME);
    }
    @Override
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {
        // noop
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'w', "minecraft:planks",
                'n', "minecraft:noteblock"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "ww"
                },
                new String[]{
                        "nn"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(1,1,0));

        builder.setConfiguration(TripleCoord.of(0,0,0),
                new String[]{
                        "M-"
                },
                new String[]{
                        "--"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,0.0f, 2.0f,2.0f,0.5f}
        );

        return builder;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new PlayerPianoTE(meta);
    }

    @Override
    public boolean onStructureBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float sx, float sy, float sz, TripleCoord sbID, int sbx, int sby, int sbz)
    {
        if (world.isRemote)
        {
            final PlayerPianoTE te = (PlayerPianoTE) world.getTileEntity(x, y, z);

            Logger.info("Loading MidiSynth");
            MidiWorker.executor.execute(te.midiWorker);
        }
        return false;
    }
}
