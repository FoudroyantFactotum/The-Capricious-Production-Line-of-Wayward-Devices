package mod.steamnsteel.utility.boardGame.TurkChess;

public class BadChessMoveException extends Exception
{
    public BadChessMoveException(String s)
    {
        super(s.replace('\n', ' ') + " : Bad Move");
    }
}
