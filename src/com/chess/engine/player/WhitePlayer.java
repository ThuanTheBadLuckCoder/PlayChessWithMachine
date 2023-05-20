package src.com.chess.engine.player;

import org.carrot2.shaded.guava.common.collect.ImmutableList;
import src.com.chess.engine.board.Move;
import src.com.chess.engine.board.Tile;
import src.com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer {
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals){
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove()&& !this.isInCheck()){
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62
                                , (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 61 ));
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied()&&
                    !this.board.getTile(58).isTileOccupied()&&
                    !this.board.getTile(57).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty()&&
                        Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()&&
                        rookTile.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58,
                            (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 69));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
