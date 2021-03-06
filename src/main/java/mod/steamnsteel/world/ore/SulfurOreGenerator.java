package mod.steamnsteel.world.ore;

import com.google.common.collect.ImmutableSet;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

/**
 * Generates sulfur ore deposits based on original work by @AtomicBlom
 * <p/>
 * Here is how it works:
 * <ol>
 *     <li>
 *         To find a starting place for the vein, take 128 samples. 2/3 of these will be 0 < y < 16 (to find underground
 *         lava) and 1/3 of these are 8 < y < 256 (the range used by Mojang for the rare lava lake)
 *     </li>
 *     <li>
 *         If a sample is under lava, laterally neighbors lava or is a ceiling 0 to 3 blocks above lava, it qualifies
 *         as a vein starting point. </li> <li> A vein is generated by deciding on a length (of 8 to 24 blocks) and
 *         meandering in all 6 directions, stopping if an attempt is made to cross into an unloaded chunk. A vein
 *         ignores everything but stone and dirt, and continues to the next block whether sulfur is actually placed or
 *         not.
 *     </li>
 * </ol>
 * The idea is to bombard a chunk with attempts to start a vein. Restrictions on the qualifications for potential
 * starting points make the vast majority of these attempts fail, but enough attempts are made to statistically ensure
 * the desired quantity of sulfur ore veins. Even though many attempts are made, the performance cost is small because
 * unqualified vein starting points are quickly eliminated.
 * <p/>
 * There are three places that can be adjusted to affect ore frequency:
 * <ol>
 *     <li>
 *         {@code NUM_ITERATIONS} determines how many vein starts arr attempted pre chunk.
 *     </li>
 *     <li>
 *         {@code NUM_BLOCKS_IN_VEIN} is the desired average length of a vein.
 *     </li>
 *     <li>
 *         {@code yGen} in {@code generate()}: The calculation here determines how many of [NUM_ITERATIONS] attempt to
 *         find a common underground lava pool and how many attempt to find a rare (near) surface lava lake.
 *     </li>
 * </ol>
 */
public class SulfurOreGenerator extends OreGenerator
{
    public SulfurOreGenerator() {
        //Most of these properties aren't used.
        super(ModBlock.oreSulfur);
    }

    // Highest "Y" to begin vein
    // 0 - 16 should encompass most naturally occurring lava
    private static final int MAX_HEIGHT = 16;

    // Number of times to attempt new vein (isPotentialVein() insures that most attempts will fail)
    private static final int NUM_ITERATIONS = 128;

    // Average length of vein in blocks (+/- 50%)
    private static final int NUM_BLOCKS_IN_VEIN = 16;
    private static final int NUM_BLOCKS_IN_VEIN_VARIATION = NUM_BLOCKS_IN_VEIN / 2;

    private static final ImmutableSet<IBlockState> TARGET_BLOCKS = ImmutableSet.of(
            Blocks.stone.getDefaultState(),
            Blocks.dirt.getDefaultState());

    private static final ImmutableSet<EnumFacing> BRANCH_DIRECTIONS = ImmutableSet.copyOf(EnumSet.of(
            EnumFacing.UP,
            EnumFacing.NORTH,
            EnumFacing.SOUTH,
            EnumFacing.WEST,
            EnumFacing.EAST));

    private static void genOreVein(World world, Random rand, BlockPos coord)
    {
        final int veinSize = NUM_BLOCKS_IN_VEIN + rand.nextInt(NUM_BLOCKS_IN_VEIN) - NUM_BLOCKS_IN_VEIN_VARIATION;

        BlockPos target = coord;
        for (int blockCount = 0; blockCount < veinSize; blockCount++)
        {
            if (isBlockReplaceable(world, target, TARGET_BLOCKS))
                placeSulfurOre(world, target);

            final EnumFacing offsetToNext = EnumFacing.random(rand);
            target = target.offset(offsetToNext);

            // Has vein strayed into an unloaded chunk? If so, STOP!
            if (!ChunkCoord.of(target).exists(world)) return;
        }
    }

    private static boolean isBlockAirJustAboveLava(World world, BlockPos coord)
    {
        BlockPos target = coord;
        for (int i = 0; i < 4; i++)
        {
            if (target.getY() >= 0)
            {
                final Block block = world.getBlockState(coord).getBlock();
                if (block.getMaterial().equals(Material.lava))
                    return true;

                if (!world.isAirBlock(coord))
                {
                    return false;
                }

                target = target.offset(EnumFacing.DOWN);
            }
        }

        return false;
    }

    private static boolean isBlockLavaNeighbor(World world, BlockPos coord)
    {
        for (final EnumFacing offset : BRANCH_DIRECTIONS)
        {
            final BlockPos target = coord.offset(offset);

            if (ChunkCoord.of(target).exists(world))

                if (world.getBlockState(target).getBlock().getMaterial().equals(Material.lava))
                    return true;
        }
        return false;
    }

    private static boolean isPotentialVein(World world, BlockPos coord)
    {
        return isBlockLavaNeighbor(world, coord) || isBlockAirJustAboveLava(world, coord);
    }

    private static void placeSulfurOre(World world, BlockPos pos)
    {
        world.setBlockState(pos, ModBlock.oreSulfur.getDefaultState(), 2);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        if (ModBlock.oreSulfur.isGenEnabled())
        {
            for (int iteration = 0; iteration < NUM_ITERATIONS; iteration++)
            {
                // Pick a "Y"
                // 66% will try to place in underground lava range and 33% will try range for lava lakes
                final int yGen = rand.nextInt(3) < 2 ? rand.nextInt(MAX_HEIGHT + 1) : rand.nextInt(248) + 8;


                final BlockPos coordBlock = pos.add(rand.nextInt(16), yGen, rand.nextInt(16));
                if (isPotentialVein(world, coordBlock)) {
                    genOreVein(world, rand, coordBlock);
                }
            }



            RetroGenHandler.markChunk(ChunkCoord.of(pos.getX() >> 4, pos.getZ() >> 4));
            return true;
        }
        return false;
    }
}
