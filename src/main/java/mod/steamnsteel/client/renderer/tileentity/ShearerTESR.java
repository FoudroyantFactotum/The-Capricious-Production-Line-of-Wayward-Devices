package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.structure.BoilerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutableTriple;

public class ShearerTESR extends SteamNSteelTESR
{
    public static final ResourceLocation TEXTURE = getResourceLocation(BoilerBlock.NAME);
    private static ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static  ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(1.0f, 0.0f, 1.0f);


    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {

    }
}
