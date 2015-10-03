package mod.steamnsteel.midi;

import org.lwjgl.opengl.GL11;

import javax.sound.midi.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class Jmidttex
{
    private final boolean[] keydown = new boolean[128];
    private Sequence sequencer;
    private BufferedImage mdbf;
    private String songName = "NoName";

    public Jmidttex(InputStream addr) throws InvalidMidiDataException, IOException
    {
        sequencer = MidiSystem.getSequence(addr);
    }

    public BufferedImage buildImage() throws InvalidMidiDataException, IOException
    {
        for (int i = 0; i < keydown.length; ++i)
        {
            keydown[i] = false;
        }

        songName = "NoName";
        mdbf = new BufferedImage(128, (int) sequencer.getTickLength() + 1, BufferedImage.TYPE_BYTE_GRAY);

        final Track[] midiTrack = sequencer.getTracks();
        long lstTimeStp = 0;

        for (int i = 0; i < midiTrack[0].size(); i++)
        {
            final MidiEvent event = midiTrack[0].get(i);
            final MidiMessage midimsg = event.getMessage();

            if (midimsg instanceof MetaMessage)
            {
                final MetaMessage metamsg = (MetaMessage) midimsg;
                final String data = new String(metamsg.getData());

                if (data.startsWith("/title:"))
                {
                    songName = data.substring(8, data.length() - 1);
                }

                //print(data, "\n");
            } else if (midimsg instanceof ShortMessage)
            {
                final ShortMessage shortmsg = (ShortMessage) midimsg;

                for (long timeWrite = lstTimeStp; timeWrite < event.getTick(); timeWrite++)
                {
                    for (int l = 0; l < keydown.length; ++l)
                    {
                        mdbf.setRGB(l, (int) timeWrite, keydown[l] ? Integer.MAX_VALUE : 0);
                    }
                }
                lstTimeStp = event.getTick();

                /*print("@", event.getTick(), " ");
                print("key: ", shortmsg.getData1(), " Vel:", shortmsg.getData2(), "\n");*/

                keydown[shortmsg.getData1()] = shortmsg.getData2() != 0;

                for (int l = 0; l < keydown.length; ++l)
                {
                    mdbf.setRGB(l, (int) event.getTick(), keydown[l] ? Integer.MAX_VALUE : 0);
                }
            }
        }

        int maxTexutreSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);

        return scaleImage(mdbf, ((double)maxTexutreSize-1)/sequencer.getTickLength(), -((127 - 88) / 2), 88);
    }

    public String getSongName()
    {
        return songName;
    }

    private static BufferedImage scaleImage(BufferedImage in, double y, int xtrans, int xwidth)
    {
        final BufferedImage out = new BufferedImage(xwidth, (int) Math.ceil(in.getHeight() * y), in.getType());
        final Graphics2D grph = (Graphics2D) out.getGraphics();

        grph.scale(1.0, y);
        grph.drawImage(in, xtrans, 0, null);

        grph.dispose();

        return out;
    }
}


