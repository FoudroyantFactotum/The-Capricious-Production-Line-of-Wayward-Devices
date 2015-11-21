package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.world.World;

public class TurkStructure extends SteamNSteelStructureBlock
{
    public static final String NAME = "themechanicalturk";

    public TurkStructure()
    {
        /*setBlockName(NAME);*/
    }

    @Override
    public void spawnBreakParticle(World world, SteamNSteelStructureTE te, TripleCoord coord, float sx, float sy, float sz)
    {

    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'w', "minecraft:planks",
                'b', "steamnsteel:blockBrass",
                'n', "minecraft:noteblock"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "wb",
                        "nw"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(1,0,0));

        builder.setConfiguration(TripleCoord.of(0,0,0),
                new String[]{
                        "M-",
                        "--"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,0.0f, 0.0f,0.0f,0.0f}
        );

        return builder;
    }

    /*@Override
    public boolean onStructureBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float sx, float sy, float sz, TripleCoord sbID, int sbx, int sby, int sbz)
    {
        if (!world.isRemote)
        {
            final TileEntity te = world.getTileEntity(x,y,z);
            if (te instanceof TurkTE)
            {
                ((TurkTE) te).e.execute(((TurkTE) te).chess);
            }

        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TurkTE(meta);
    }*/
}
