package src.com.chess.engine.player.ai;

import src.com.chess.engine.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);

}
