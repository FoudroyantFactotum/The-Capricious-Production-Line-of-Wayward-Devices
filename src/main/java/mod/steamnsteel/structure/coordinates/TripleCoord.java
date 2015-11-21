package mod.steamnsteel.structure.coordinates;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class TripleCoord
{
    public int x;
    public int y;
    public int z;

    private TripleCoord()
    {
        //noop
    }

    private TripleCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static TripleCoord of(int x, int y, int z)
    {
        return new TripleCoord(x,y,z);
    }

    public static TripleCoord of (TripleCoord tc)
    {
        return new TripleCoord(tc.x, tc.y, tc.z);
    }

    public static TripleCoord of (BlockPos pos)
    {
        return new TripleCoord(pos.getX(), pos.getY(), pos.getZ());
    }

    public static TripleCoord of (TripleCoord tc, EnumFacing d)
    {
        return new TripleCoord(tc.x + d.getFrontOffsetX(), tc.y + d.getFrontOffsetY(), tc.z + d.getFrontOffsetZ());
    }

    public BlockPos getBlockPos()
    {
        return new BlockPos(x,y,z);
    }

    @Override
    public int hashCode()
    {
        return hashLoc(x,y,z);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (!(obj instanceof TripleCoord)) return false;

        final TripleCoord coord = (TripleCoord) obj;

        return this.x == coord.x &&
                this.y == coord.y &&
                this.z == coord.z;
    }

    @Override
    public String toString()
    {
        return "(" + x + ',' + y + ',' + z + ')';
    }

    public static int hashLoc(int x, int y, int z)
    {
        return  (((byte) x) << 16) +
                (((byte) y) << 8)  +
                 ((byte) z);
    }

    public static TripleCoord dehashLoc(int val)
    {
        //byte used as a mask on vals.
        return TripleCoord.of(
                (int) (byte) (val >> 16),
                (int) (byte) (val >> 8),
                (int) (byte)  val
        );
    }
}
