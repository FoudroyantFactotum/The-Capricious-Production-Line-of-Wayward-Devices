package mod.steamnsteel.utility.midi;

import mod.steamnsteel.tileentity.structure.DanseMacabreTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.client.Minecraft;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class MidiHW implements Runnable
{
    Boolean keepAlive;
    private Synthesizer sz;
    private DanseMacabreTE te;

    Instrument[] chanelInstrament = new Instrument[16];

    final LinkedList<MetaDataFuture> midiEventBell = new LinkedList<MetaDataFuture>();

    public MidiHW(Boolean keepAlive, DanseMacabreTE te)
    {
        this.te = te;
        this.keepAlive = keepAlive;
    }

    private byte[] buildMetaMsg(ShortMessage sm, int i)
    {
        final int r = sm.getData1();
        final int k = sm.getData2();

        return new byte[]{(byte) (i & 0xFF000000), (byte) (i & 0x00FF0000), (byte) (i & 0x0000FF00), (byte) (i & 0x000000FF),
                (byte) (r & 0xFF000000), (byte) (r & 0x00FF0000), (byte) (r & 0x0000FF00), (byte) (r & 0x000000FF),
                (byte) (k & 0xFF000000), (byte) (k & 0x00FF0000), (byte) (k & 0x0000FF00), (byte) (k & 0x000000FF)};
    }

    private MetaDataFuture unbuildMetaMsg(MetaMessage mm)
    {
        final byte[] d = mm.getData();
        final MetaDataFuture m = new MetaDataFuture();
        int i = 0;

        m.instrament = d[i++] | d[i++] | d[i++] | d[i++];
        m.note = d[i++] | d[i++] | d[i++] | d[i++];
        m.vel = d[i++] | d[i++] | d[i++] | d[i++];

        return m;
    }

    @Override
    public void run()
    {
        try
        {
            final Soundbank sb = MidiSystem.getSoundbank(new File("/usr/share/soundfonts/FluidR3_GM2-2.sf2"));
            final InputStream sis = Minecraft.getMinecraft().getResourceManager().getResource(SongList.getSongMidiPath("dansemacabre")).getInputStream();
            final Sequencer sqr = MidiSystem.getSequencer();
            sz = MidiSystem.getSynthesizer();
            EventSieve e = new EventSieve(sz.getReceiver());

            sz.open();
            {
                sz.loadAllInstruments(sb);
                Logger.info("SoundBank: " + sb + "\tSynth: " + sz);

                //sz.remapInstrument(sz.getAvailableInstruments()[GmInstrament.AcousticGrandPiano.getNo()], sz.getAvailableInstruments()[GmInstrament.ChurchOrgan.getNo()]);

                sqr.setSequence(sis);
                sqr.getTransmitter().setReceiver(e);

                //add Notification of future note
                Logger.info("Tracks: " + sqr.getSequence().getTracks().length);

                for (int i = 0; i < sqr.getSequence().getTracks().length; ++i)
                {
                    final Track t = sqr.getSequence().getTracks()[i];

                    for (int s = 0; s < t.size(); ++s)
                    {
                        final MidiEvent me = t.get(s);

                        if (me.getMessage() instanceof ShortMessage)
                        {
                            final ShortMessage sm = (ShortMessage) me.getMessage();

                            switch (sm.getCommand())
                            {
                                case ShortMessage.PROGRAM_CHANGE:
                                    //changing the instrument
                                    chanelInstrament[sm.getChannel()] = sz.getAvailableInstruments()[sm.getData1()];
                                    break;
                                case ShortMessage.NOTE_ON:
                                    if (hasCChannelInstrament(sm.getChannel(), GmInstrament.TubularBells))
                                    {
                                        if (sm.getData2() != 0 && me.getTick() - 50 > 0)
                                        {
                                            final MetaDataFuture mf = new MetaDataFuture();

                                            mf.timeStamp = me.getTick() - 50;
                                            mf.instrament = GmInstrament.TubularBells.getNo();
                                            mf.note = sm.getData1();
                                            mf.vel = sm.getData2();

                                            midiEventBell.add(mf);
                                        }
                                    } else if (hasCChannelInstrament(sm.getChannel(), GmInstrament.AcousticGrandPiano))
                                    {
                                        //Logger.info("Piano at time : " + me.getTick());
                                    }
                            }
                        }
                    }
                }

                Collections.sort(midiEventBell, new MetaDataFuture());
                te.bellRingers1isActive = true;

                //play music loop
                sqr.open();
                {
                    sqr.setTickPosition(62110);
                    sqr.start();
                    while (sqr.isRunning() && Minecraft.getMinecraft().theWorld != null && keepAlive)
                    {
                        //=========================================================================
                        //             T o p   B e l l   a n d   S k e l e n t o n
                        //=========================================================================
                        if (!te.bellRingers1isActive && !midiEventBell.isEmpty() && midiEventBell.getFirst().timeStamp - sqr.getTickPosition() < 0)
                        {
                            midiEventBell.removeFirst();
                            if (te.whichOneTwoMove)
                                te.bellRingers1isActive = true;
                            else
                                te.bellRingers2isActive = true;

                            te.whichOneTwoMove ^= true;
                        }

                        if (te.bellRingers1isActive)
                        {
                            if (te.bellRingersPos1 < 1)
                                te.bellRingersPos1 += 0.02;
                            else
                            {
                                te.bellAmplitude -= 1;
                                te.bellRingers1isActive = false;
                            }
                        } else
                        {
                            if (te.bellRingersPos1 > 0)
                                te.bellRingersPos1 -= 0.02;
                        }

                        if (te.bellRingers2isActive)
                        {
                            if (te.bellRingersPos2 < 1)
                                te.bellRingersPos2 += 0.02;
                            else
                            {
                                te.bellAmplitude += 1;
                                te.bellRingers2isActive = false;
                            }
                        } else
                        {
                            if (te.bellRingersPos2 > 0)
                                te.bellRingersPos2 -= 0.02;
                        }

                        if (te.bellAmplitude > 0.0001 || te.bellAmplitude < -0.0001)
                        {
                            te.bellAmplitude *= 0.976;

                            te.bellAngle = Math.sin(te.bellAmplitude) * 15;
                        }

                        //=========================================================================
                        //                              O r g a n
                        //=========================================================================


                        Thread.sleep(4);

                    }
                    sqr.stop();
                }
                sqr.close();
            }
            sz.close();
            //sqr.close();


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

        Logger.info("Midi End");
    }

    private boolean hasCChannelInstrament(int channel, GmInstrament i)
    {
        return chanelInstrament[channel] == sz.getAvailableInstruments()[i.getNo()];
    }


    private class EventSieve implements Receiver
    {
        Instrument[] chanelInstrament = new Instrument[16];
        private Receiver rec;

        public EventSieve(Receiver rec)
        {
            this.rec = rec;
        }

        @Override
        public void send(MidiMessage midiMessage, long timeStamp)
        {
            if (midiMessage instanceof ShortMessage)
            {
                final ShortMessage sm = (ShortMessage) midiMessage;

                switch (sm.getCommand())
                {
                    case ShortMessage.PROGRAM_CHANGE:
                    {
                        try
                        {
                            if (sm.getData1() == GmInstrament.AcousticGrandPiano.getNo())
                                sm.setMessage(sm.getCommand(), sm.getChannel(), 0, sm.getData2());
                        } catch (InvalidMidiDataException e)
                        {
                            e.printStackTrace();
                        }
                        chanelInstrament[sm.getChannel()] = sz.getAvailableInstruments()[sm.getData1()];
                        Logger.info("ProgramChange: " + sm.getChannel() + " : " + sm.getData1() + " : " + sm.getData2() +
                                " : " + sz.getAvailableInstruments()[sm.getData1()]);
                        break;
                    }
                    case ShortMessage.NOTE_ON:
                    {
                        if (hasChannelInstrament(sm.getChannel(), GmInstrament.TubularBells))
                        {
                            //Logger.info("Bell Off");
                            //te.bellRingers1isActive = false;
                        }
                    }
                }
            }

            rec.send(midiMessage, timeStamp);
        }

        private boolean hasChannelInstrament(int channel, GmInstrament i)
        {
            return chanelInstrament[channel] == sz.getAvailableInstruments()[i.getNo()];
        }

        @Override
        public void close()
        {
            rec.close();
        }
    }

    private static final class MetaDataFuture implements Comparator<MetaDataFuture>
    {
        public long timeStamp;
        public int instrament;
        public int note;
        public int vel;

        @Override
        public int compare(MetaDataFuture t1, MetaDataFuture t2)
        {
            return Long.compare(t1.timeStamp, t2.timeStamp);
        }

        @Override
        public String toString()
        {
            return "{" + timeStamp + ',' + instrament + ',' + note + ',' + vel + '}';
        }
    }

    private enum GmInstrament
    {
        TubularBells(14),
        StringEnsemble(48),
        Xylophone(13),
        Violin(40),
        Timpani(47),
        OrchestralHarp(46),
        Fiddle(110),
        AcousticBass(32),
        Lead6(85),
        FrenchHorn(60),
        OrchestraHit(55),
        PizzicatoStrings(45),
        Applause(126),
        AcousticGrandPiano(0),
        ChurchOrgan(20);

        int no;

        GmInstrament(int no)
        {
            this.no = no;
        }

        public int getNo()
        {
            return no;
        }
    }
}
