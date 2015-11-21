package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.PlayerPianoTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.midi.MidiWorker;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class PlayerPiano extends SteamNSteelStructureBlock
{
    public static final String NAME = "playerpiano";
    public PlayerPiano()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
                        .withProperty(propMirror, false)
        );
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new PlayerPianoTE(getPattern(), (EnumFacing)state.getValue(BlockDirectional.FACING), (Boolean)state.getValue(propMirror));
    }

     @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, BlockPos callPos, EnumFacing side, TripleCoord sbID, float sx, float sy, float sz)
    {
        if (world.isRemote)
        {
            final PlayerPianoTE te = (PlayerPianoTE) world.getTileEntity(pos);

            Logger.info("Loading MidiSynth");
            MidiWorker.executor.execute(te.midiWorker);
        }
        return false;
    }
}
