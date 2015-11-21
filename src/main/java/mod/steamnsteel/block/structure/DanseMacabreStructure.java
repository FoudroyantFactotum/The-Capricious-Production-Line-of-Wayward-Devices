package mod.steamnsteel.block.structure;

import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.world.World;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DanseMacabreStructure extends SteamNSteelStructureBlock
{
    public static final String NAME = "dansemacabre";

    public static final ExecutorService e = Executors.newSingleThreadExecutor();

    public DanseMacabreStructure()
    {
        setUnlocalizedName(NAME);
    }

    @Override
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {

    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        return null;
    }

  /*  @Override
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {
        //noop
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        final Builder<Character, String> blocks = new Builder<Character, String>();

        blocks.put('p', "steamnsteel:blockPlotonium");
        blocks.put('s', "minecraft:stonebrick");
        blocks.put('w', "minecraft:planks");
        blocks.put('e', "minecraft:emerald_block");
        blocks.put('d', "minecraft:dirt");
        blocks.put('n', "minecraft:noteblock");

        builder.assignBlockDefinitions(blocks.build());

        builder.assignConstructionBlocks(
                new String[]{
                        "sssss",
                        "sssss",
                        "sssss",
                        "sssss",
                        "sssss"
                },
                new String[]{
                        "swwws",
                        "dpppd",
                        "sppps",
                        "dpppd",
                        "swwws"
                },
                new String[]{
                        "swwws",
                        "dpppd",
                        "speps",
                        "dpppd",
                        "swwws"
                },
                new String[]{
                        "swwws",
                        "sppps",
                        "spnps",
                        "sppps",
                        "swwws"
                },
                new String[]{
                        "wwwww",
                        "wnwnw",
                        "wwnww",
                        "wnwnw",
                        "wwwww"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(0, 0, 0));

        builder.setConfiguration(TripleCoord.of(0, 0, 0),
                new String[]{
                        "M----",
                        "-----",
                        "-----",
                        "-----",
                        "-----"
                },
                new String[]{
                        "-----",
                        "-----",
                        "-----",
                        "-----",
                        "-----"
                },
                new String[]{
                        "-----",
                        "-----",
                        "-----",
                        "-----",
                        "-----"
                },
                new String[]{
                        "-----",
                        "-----",
                        "-----",
                        "-----",
                        "-----"
                },
                new String[]{
                        "-----",
                        "-----",
                        "-----",
                        "-----",
                        "-----"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
        );

        return builder;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new DanseMacabreTE(meta);
    }

    @Override
    public boolean onStructureBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float sx, float sy, float sz, TripleCoord sbID, int sbx, int sby, int sbz)
    {
        if (world.isRemote)
        {
            DanseMacabreTE te = (DanseMacabreTE) world.getTileEntity(x, y, z);
            e.submit(new MidiHW(te.keepAlive, te));
        }

        return false;
    }
*/
}
