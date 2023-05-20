package src.com.chess.engine.board;

import org.carrot2.shaded.guava.common.collect.Iterables;
import src.com.chess.engine.pieces.Pawn;
import src.com.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.Map;

public class Board {
    private final Map<Integer, Piece> boardConfig;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
    private final Move transitionMove;

    private static final Board STANDARD_BOARD = createStandardBoardImpl();
    public Tile getTile(final int tileCoordinate) {
        return null;
    }
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }
    public void setEnPassantPawn(Pawn enPassantPawn){
        this.enPassantPawn = enPassantPawn;
    }
}