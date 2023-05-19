package src.com.chess.engine.board;

import src.com.chess.engine.pieces.Tile;

public class Board {
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