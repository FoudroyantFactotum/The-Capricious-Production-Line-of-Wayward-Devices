package mod.steamnsteel.utility.boardGame.TurkChess;

import mod.steamnsteel.utility.boardGame.ITurkGame;
import mod.steamnsteel.utility.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameChess implements ITurkGame, Runnable
{

    private Process gnuchess;
    private InputStream chessOut;
    private OutputStream chessIn;

    public GameChess()
    {
        try
        {
            gnuchess = Runtime.getRuntime().exec("/usr/bin/gnuchess --xboard");
            chessOut = gnuchess.getInputStream();
            chessIn = gnuchess.getOutputStream();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        final ExecutorService e = Executors.newSingleThreadExecutor();

        e.execute(new GameChess());
        e.shutdown();

        while (!e.isShutdown())
            Thread.sleep(10);

    }

    public static void writeCmd(OutputStream os, String cmd) throws IOException
    {
        os.write(cmd.getBytes());
        os.flush();
    }

    public static String read(InputStream is) throws IOException
    {
        final byte[] b = new byte[100];
        int i = 0;

        do
        {
            b[i] = (byte) is.read();
        } while (b[i++] != 10 && is.available() != 0);

        return new String(b, 0, i - 1);
    }

    public static void waitForInput(InputStream is) throws IOException, InterruptedException
    {
        while (is.available() == 0)
            Thread.sleep(20);
    }

    public static void flushRead(InputStream is) throws IOException, InterruptedException
    {
        while (is.available() != 0)
        {
            is.skip(is.available());
        }
    }

    @Override
    public void run()
    {
        Logger.info("ITurk - Start");
        try
        {

            //chessOut.skip(chessOut.available());

            writeCmd(chessIn, "xboard\n");
            writeCmd(chessIn, "protover 2\n");
            writeCmd(chessIn, "time 100\n");
            writeCmd(chessIn, "new\n");

            Thread.sleep(1000);

            while (chessOut.available() != 0)
                Logger.info(read(chessOut));

            // Logger.info("gnuChess: " + read(chessOut));
            Thread.sleep(1000);
            flushRead(chessOut);


            Logger.info("Send - a2a4");
            Logger.info("Receive - " + sendAndRecieveCounterMove("a2a4\n"));

            Logger.info("Send - e2e4");
            Logger.info("Receive - " + sendAndRecieveCounterMove("e2e4\n"));


            // Logger.info("gnuChess: " + read(chessOut).split("\n")[3].split(" ")[2]);

            writeCmd(chessIn, "quit\n");

            chessIn.close();
            chessOut.close();
            gnuchess.destroy();
            Logger.info("ITurk - Safe End");

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (BadChessMoveException e)
        {
            e.printStackTrace();
        }

        Logger.info("ITurk - End");
    }

    public String sendAndRecieveCounterMove(String move) throws IOException, InterruptedException, BadChessMoveException
    {
        writeCmd(chessIn, move);                         //e2e4
        waitForInput(chessOut);

        read(chessOut);                                  //TimeLimit[0] = 0
        read(chessOut);                                  //TimeLimit[1] = 0.1
        if (read(chessOut).startsWith("Invalid"))        //1. e2e4       or          Invalid move: e2e10
            throw new BadChessMoveException(move);

        read(chessOut);                                  //1. ... b8c6

        return read(chessOut).split(":")[1].substring(1); //My move is : b8c6
    }
}

