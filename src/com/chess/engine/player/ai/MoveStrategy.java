package src.com.chess.engine.player.ai;

import src.com.chess.engine.board.Board;
import src.com.chess.engine.board.Move;

public interface MoveStrategy {

    long getNumBoardsEvaluated();

    Move execute(Board board);

}
