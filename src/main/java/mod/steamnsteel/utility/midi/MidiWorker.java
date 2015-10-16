package mod.steamnsteel.utility.midi;

import mod.steamnsteel.tileentity.structure.PlayerPianoTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.client.Minecraft;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MidiWorker implements Runnable
{
    public static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private WeakReference<PlayerPianoTE> te;

    public MidiWorker(PlayerPianoTE te)
    {
        this.te = new WeakReference<PlayerPianoTE>(te);
    }

    @Override
    public void run()
    {
        PlayerPianoTE ste = te.get();
        if (ste != null)
        {
            try
            {
                final Soundbank sb = MidiSystem.getSoundbank(new File("/usr/share/soundfonts/FluidR3_GM2-2.sf2"));
                final InputStream sis = Minecraft.getMinecraft().getResourceManager().getResource(SongList.getSongMidiPath(ste.songLoaded)).getInputStream();
                final Sequencer sqr = MidiSystem.getSequencer();
                final Synthesizer sz = MidiSystem.getSynthesizer();
                ste = null;

                sz.loadAllInstruments(sb);

                Logger.info("SoundBank: " + sb + "\tSynth: " + sz);

                sqr.setSequence(sis);
                sqr.getTransmitter().setReceiver(new EventSieve(sqr.getReceiver()));
                sqr.open();

                //play music loop
                //device.open();
                {
                    sqr.start();
                    while (sqr.isRunning())
                    {
                        Thread.sleep(1);
                        PlayerPianoTE sste = te.get();
                        if (sste == null || sste.isInvalid())
                        {
                            sqr.stop();
                        } else
                        {
                            sste.songReadHeadPos = (double)sqr.getTickPosition()/sqr.getTickLength();
                            for (int i=0;i<sste.keyPosY.length; ++i)
                            {
                                if (!sste.keyIsDown[i])
                                {
                                    if (sste.keyPosY[i] < 0)
                                        sste.keyPosY[i] += 0.0005;
                                    else
                                        sste.keyPosY[i] = 0;
                                }
                            }
                        }

                        //do a thing
                        //sqr.stop();

                    }
                    sqr.stop();
                }
                sqr.close();


            } catch (InvalidMidiDataException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (MidiUnavailableException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Logger.info("MidiThread End");
        }
    }

    private class EventSieve implements Receiver
    {
        private Receiver rec;

        public EventSieve(Receiver rec)
        {
            this.rec = rec;
        }

        @Override
        public void send(MidiMessage midiMessage, long l)
        {
            rec.send(midiMessage, l);

            if (midiMessage instanceof ShortMessage)
            {
                final ShortMessage sm = (ShortMessage) midiMessage;

                PlayerPianoTE ite = te.get();
                if (ite != null)
                {
                    if (sm.getData1()-21 > -1 && sm.getData1()-21 < ite.keyPosY.length)
                    {
                        ite.keyIsDown[sm.getData1() - 21] = sm.getData2() != 0;
                        if (sm.getData2() != 0)
                            ite.keyPosY[sm.getData1() - 21] = -0.03f;
                    }

                }
            }
        }

        @Override
        public void close()
        {
            rec.close();
        }
    }
}
