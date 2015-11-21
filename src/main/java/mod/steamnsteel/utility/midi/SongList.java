package mod.steamnsteel.utility.midi;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.sound.midi.InvalidMidiDataException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SongList
{
    public static void loadSongSheets()
    {
        final TextureManager tm =Minecraft.getMinecraft().getTextureManager();
        loadTextureSafe(tm, "TemptationRag(1909)");
        //loadTextureSafe(tm, "TheSheikOfAraby(1921)");
    }

    private static void loadTextureSafe(TextureManager tm, String name)
    {
        final ResourceLocation rl = getSongTexturePath(name);
        tm.loadTexture(rl, new Mtt(name));
    }

    public static ResourceLocation getSongTexturePath(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), "sound/" + name + ".sheet");
    }

    public static ResourceLocation getSongMidiPath(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), "sound/" + name + ".mid");
    }

    private static class Mtt implements ITextureObject
    {
        private String name = "";
        private int glTexID = -1;

        public Mtt(String name)
        {
            this.name = name;
        }

        @Override
        public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_)
        {

        }

        @Override
        public void restoreLastBlurMipmap()
        {

        }

        @Override
        public void loadTexture(IResourceManager rm) throws IOException
        {
            BufferedImage bf;
            try
            {
                bf = new Jmidttex(rm.getResource(getSongMidiPath(name))
                        .getInputStream())
                        .buildImage();

                Logger.info("Loaded Midi: " + name);
                TextureUtil.uploadTextureImageAllocate(getGlTextureId(), bf, false, false);

            } catch (InvalidMidiDataException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getGlTextureId()
        {
            if (glTexID == -1)
            {
                glTexID = TextureUtil.glGenTextures();
            }

            return glTexID;
        }
    }
}
