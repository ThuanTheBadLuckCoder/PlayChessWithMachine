package src.com.chess.engine.classic.player.ai;

import src.com.chess.engine.classic.board.Board;
import src.com.chess.engine.classic.board.Move;

public interface MoveStrategy {

    long getNumBoardsEvaluated();

    Move execute(Board board);

}
