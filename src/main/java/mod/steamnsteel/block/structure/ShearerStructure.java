package mod.steamnsteel.block.structure;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.structure.StructureDefinitionBuilder;
import mod.steamnsteel.structure.coordinates.TripleCoord;
import mod.steamnsteel.tileentity.structure.ShearerTE;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ShearerStructure extends SteamNSteelStructureBlock implements ITileEntityProvider
{
    public static final String NAME = "Shearer";

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
                's', "steamnsteel:blockSteel",
                'g', "minecraft:glass_pane",
                'f', "minecraft:fire",
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "pps"
                },
                new String[]{
                        "pps"
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

       /* builder.setCollisionBoxes(
                new float[]{0.7f,0.0f,0.7f, 2.3f,0.5f,2.3f}
        );*/

        return builder;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new ShearerTE();
    }
}
