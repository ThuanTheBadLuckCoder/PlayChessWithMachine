package src.com.chess.engine.pieces;

import src.com.chess.engine.Alliance;
import src.com.chess.engine.board.Board;
import src.com.chess.engine.board.BoardUtils;
import src.com.chess.engine.board.Move;
import src.com.chess.engine.board.Move.AttackMove;
import src.com.chess.engine.board.Move.MajorMove;
import src.com.chess.engine.board.MoveUtils;

import java.util.*;

public final class Bishop extends Piece {

    // Possible move offsets for a bishop (diagonal directions)
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    // Precomputed lines of attack for each tile on the board
    private final static Map<Integer, MoveUtils.Line[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    public Bishop(final Alliance alliance,
                  final int piecePosition) {
        super(PieceType.BISHOP, alliance, piecePosition, true);
    }

    public Bishop(final Alliance alliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }

    /**
     * Computes and returns a mapping of each tile to the lines of attack that a bishop on that tile can make.
     *
     */
    private static Map<Integer, MoveUtils.Line[]> computeCandidates() {
        Map<Integer, MoveUtils.Line[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            List<MoveUtils.Line> lines = new ArrayList<>();
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                int destination = position;
                MoveUtils.Line line = new MoveUtils.Line();
                while (BoardUtils.isValidTileCoordinate(destination)) {
                    // Check if the destination is on the first or eighth column (bishop can't move further in that direction)
                    if (isFirstColumnExclusion(destination, offset) || isEighthColumnExclusion(destination, offset)) {
                        break;
                    }
                    // Move the destination along the line of attack
                    destination += offset;
                    if (BoardUtils.isValidTileCoordinate(destination)) {
                        line.addCoordinate(destination);
                    }
                }
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
            if (!lines.isEmpty()) {
                candidates.put(position, lines.toArray(new MoveUtils.Line[0]));
            }
        }
        return Collections.unmodifiableMap(candidates);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final MoveUtils.Line line : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {
            for (final int candidateDestinationCoordinate : line.getLineCoordinates()) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                if (pieceAtDestination == null) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAllegiance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                    break;
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.bishopBonus(this.piecePosition);
    }
    /**
     * The locationBonus() method overrides the locationBonus() method in the Piece class. 
     * It calculates a bonus for the bishop based on its current position on the board. 
     * The calculation is delegated to the bishopBonus() method of the Alliance class 
     * (which is an enum that represents the two sides of the chess game, white and black).
     * The bonus returned by bishopBonus() is added to the bishop's overall score for evaluation purposes.
     */
    @Override
    public Bishop movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedBishop(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }
    /**
     * The locationBonus() method overrides the locationBonus() method in the Piece class. 
     * It calculates a bonus for the bishop based on its current position on the board. 
     * The calculation is delegated to the bishopBonus() method of the Alliance class 
     * (which is an enum that represents the two sides of the chess game, white and black). 
     * The bonus returned by bishopBonus() is added to the bishop's overall score for evaluation purposes.
     * PieceUtils.getMovedBishop() method, which creates a new Bishop object with the correct state based on the move.
     */
    @Override
    public String toString() {
        return this.pieceType.toString();
    }
    /**
     * The toString() method overrides the toString() method in the Object class. 
     * It returns a string representation of the bishop, which is just its pieceType ("BISHOP").
     */
    private static boolean isFirstColumnExclusion(final int position, /*methods are helper methods used by the computeCandidates() method to exclude moves that would take the bishop off the board */
                                                  final int offset) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(position) && /*They take a position and an offset as parameters and return true if moving in that direction from the given position would take the bishop off the board */
                ((offset == -9) || (offset == 7)));
    }

    private static boolean isEighthColumnExclusion(final int position,/*methods are helper methods used by the computeCandidates() method to exclude moves that would take the bishop off the board */
                                                   final int offset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(position) &&/*They take a position and an offset as parameters and return true if moving in that direction from the given position would take the bishop off the board */
                ((offset == -7) || (offset == 9));
    }

}