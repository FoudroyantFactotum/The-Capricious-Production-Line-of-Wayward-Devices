package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.world.World;

public class ShearerStructure extends SteamNSteelStructureBlock
{
    public static final String NAME = "shearer";
    public ShearerStructure()
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
        StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignBlockDefinitions(ImmutableMap.of(
                'p', "steamnsteel:blockPlotonium",
                's', "steamnsteel:blockSteel"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "spp"
                },
                new String[]{
                        "spp"
                }
        );

        builder.assignToolFormPosition(TripleCoord.of(0,0,0));

        builder.setConfiguration(TripleCoord.of(0,0,0),
                new String[]{
                        "M--"
                },
                new String[]{
                        "---"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,0.0f, 1.2f,1.3f,1.0f},
                new float[]{1.2f,0.2f,0.0f, 3.0f,1.8f,1.0f}
        );

        return builder;
    }

    /*@Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new ShearerTE(meta);
    }*/
}
