package src.com.chess.engine.pieces;

import src.com.chess.engine.Alliance;
import src.com.chess.engine.board.Board;
import src.com.chess.engine.board.BoardUtils;
import src.com.chess.engine.board.Move;
import src.com.chess.engine.board.Move.AttackMove;
import src.com.chess.engine.board.Move.MajorMove;

import java.util.*;

public final class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };
    /*This array contains the relative coordinates of all the squares that a King can move to from its current position*/

    private final static Map<Integer, int[]> PRECOMPUTED_CANDIDATES = computeCandidates();
    /*This map associates each square on the board with an array of all the squares that a King can move to from that square*/
    private final boolean isCastled;/*This field indicates whether the King has been castled or not */
    private final boolean kingSideCastleCapable;/*This field indicates whether the King is capable of performing a King-side castle.*/
    private final boolean queenSideCastleCapable;/*This field indicates whether the King is capable of performing a Queen-side castle*/

    public King(final Alliance alliance, /*defines a public constructor for the King class, Alliance object representing the color of the King*/
                final int piecePosition,/*integer representing the initial position of the King */
                final boolean kingSideCastleCapable,/*kingSideCastleCapable and queenSideCastleCapable-King is capable of performing a King-side castle and a Queen-side castle */
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, alliance, piecePosition, true);
        this.isCastled = false; /*This is done in the first constructor because a King cannot be castled during its first move */
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final Alliance alliance,/*nhu tren */
                final int piecePosition,/*nhu tren */
                final boolean isFirstMove,/*indicating whether this is the first move of the King */
                final boolean isCastled,/*nhu tren */
                final boolean kingSideCastleCapable,/*nhu tren */
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, alliance, piecePosition, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    private static Map<Integer, int[]> computeCandidates() { /*purpose of this method is to precompute and store all the possible move coordinates for a King on each of the 64 squares of the chessboard */
        final Map<Integer, int[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATES.length];/*iterates through each of the 64 positions on the chessboard, represented by integers from 0 to 63
            and for each position, it calculates all possible move offsets from that position. The possible move offsets are stored in the CANDIDATE_MOVE_COORDINATES array, 
            which contains the relative positions of all squares that a King can move to from its current position.
            */
            int numLegalOffsets = 0;
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                if (isFirstColumnExclusion(position, offset) || /*check if the King is on the first or eighth column of the board and if the move offset would lead it off the board. 
                                                                If the move offset would lead the King off the board, the method skips that offset and moves on to the next one*/
                        isEighthColumnExclusion(position, offset)) {
                    continue;
                }
                int destination = position + offset; /*For each valid move offset, the method calculates the destination square on the board and checks if it is a valid square. 
                                                    If the destination square is valid, the move offset is added to an array of legal move offsets for that square */
                if (BoardUtils.isValidTileCoordinate(destination)) {
                    legalOffsets[numLegalOffsets++] = offset;
                }
            }
            if (numLegalOffsets > 0) {
                candidates.put(position, Arrays.copyOf(legalOffsets, numLegalOffsets));/*If there are any legal move offsets for that square, 
                                                                                        the array is added to the candidates map with the current position as the key.*/
            }
        }
        return Collections.unmodifiableMap(candidates);
    }


    public boolean isCastled() {
        return this.isCastled;/*returns a boolean indicating whether the King has already performed a castle move.*/
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;/*returns a boolean indicating whether the King is currently capable of performing a kingside castle move */
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;/*returns a boolean indicating whether the King is currently capable of performing a queenside castle move */
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { 
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) { 
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);/*For each candidate offset, the method calculates the candidate destination 
                                                                                            coordinate by adding the offset to the king's current position */
            if (pieceAtDestination == null) {
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));/*If there is no piece at the destination coordinate, the move is considered a legal MajorMove and is added to the list of legal moves*/
            } else {
                final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();/*the method checks whether the piece at the destination coordinate belongs to the same alliance as the king or not*/
                if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                    legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate,
                            pieceAtDestination));/*If the piece belongs to the opposing alliance, the move is considered a legal MajorAttackMove and is added to the list of legal moves*/
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);/*returns a collection of all the legal moves that the king can make on the given Board object*/
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.kingBonus(this.piecePosition);/* This method is used to calculate the bonus score for the King based on its position on the board */
    }

    @Override
    public King movePiece(final Move move) {
        return new King(this.pieceAlliance, move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false);/*takes a Move object as a parameter and returns a new King object with the updated position and castling information */
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;/*equal by first checking if they refer to the same object in memory */
        }
        if (!(other instanceof King)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final King king = (King) other;
        return isCastled == king.isCastled;
    }

    @Override
    public int hashCode() {
        return (31 * super.hashCode()) + (isCastled ? 1 : 0);/*using 31 is a good default choice that can provide good performance and hash code quality in many cases*/
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentCandidate)
                && ((candidateDestinationCoordinate == -9) || (candidateDestinationCoordinate == -1) ||
                (candidateDestinationCoordinate == 7));/*checks whether the candidate destination coordinate is one of the three squares that would move the king to a different column (i.e. squares A8, A1, or B1)*/
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentCandidate)
                && ((candidateDestinationCoordinate == -7) || (candidateDestinationCoordinate == 1) ||
                (candidateDestinationCoordinate == 9));/*checks for squares H8, H1, and G1*/
    }
}   