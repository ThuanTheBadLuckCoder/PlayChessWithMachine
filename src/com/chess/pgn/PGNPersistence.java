package src.com.chess.pgn;

import src.com.chess.engine.classic.board.Board;
import src.com.chess.engine.classic.board.Move;
import src.com.chess.engine.classic.player.Player;

public interface PGNPersistence {

    void persistGame(Game game);

    Move getNextBestMove(Board board, Player player, String gameText);

}
