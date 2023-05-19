package src.com.chess.engine.pieces;

import org.carrot2.shaded.guava.common.collect.ImmutableList;

import src.com.chess.engine.Alliance;
import src.com.chess.engine.board.Board;
import src.com.chess.engine.board.BoardUtils;
import src.com.chess.engine.board.Move;
import src.com.chess.engine.board.Tile;

import java.util.*;

public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };/*representing the possible moves a knight can make on the chessboard */
    private final static Map<Integer, int[]> PRECOMPUTED_CANDIDATES = computeCandidates();/*map stores precomputed legal move arrays for each square on the board*/

    public Knight(final Alliance alliance,
                  final int piecePosition) {
        super(PieceType.KNIGHT, alliance, piecePosition, true);
    }

    public Knight(final Alliance alliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT, alliance, piecePosition, isFirstMove);
    }

    private static Map<Integer, int[]> computeCandidates() { /*generates and stores the legal move arrays for each square on the board. It excludes moves that would take the knight off the board or move it to a different column*/
        final Map<Integer, int[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            final int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATES.length];
            int numLegalMoves = 0;
            for (final int offset : CANDIDATE_MOVE_COORDINATES) {
                if(isFirstColumnExclusion(position, offset) ||
                        isSecondColumnExclusion(position, offset) ||
                        isSeventhColumnExclusion(position, offset) ||
                        isEighthColumnExclusion(position, offset)) {
                    continue;
                }
                final int destination = position + offset;
                if (BoardUtils.isValidTileCoordinate(destination)) {
                    legalOffsets[numLegalMoves++] = destination;
                }
            }
            candidates.put(position, Arrays.copyOf(legalOffsets, numLegalMoves));
        }
        return Collections.unmodifiableMap(candidates);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {/*generates and returns a collection of legal moves for the knight on the given board. It checks the destination squares and creates appropriate move objects*/
        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateDestinationCoordinate : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {
            final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
            if (pieceAtDestination == null) {
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else {
                final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                    legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,
                            pieceAtDestination));
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.knightBonus(this.piecePosition);
    }

    @Override
    public Knight movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedKnight(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidateOffset) {
        return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentPosition) && ((candidateOffset == -17) ||
                (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.INSTANCE.SECOND_COLUMN.get(currentPosition) && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition,
                                                    final int candidateOffset) {
        return BoardUtils.INSTANCE.SEVENTH_COLUMN.get(currentPosition) && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentPosition) && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }
}
