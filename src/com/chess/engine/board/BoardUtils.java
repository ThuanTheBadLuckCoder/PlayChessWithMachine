package src.com.chess.engine.board;

import src.com.chess.engine.pieces.Piece;

import java.util.*;

public enum BoardUtils {
    INSTANCE;


    public final List<Boolean> FIRST_COLUMN = initColumn(0);
    public final List<Boolean> SECOND_COLUMN = initColumn(1);
    public final List<Boolean> THIRD_COLUMN = initColumn(2);
    public final List<Boolean> FOURTH_COLUMN = initColumn(3);
    public final List<Boolean> FIFTH_COLUMN = initColumn(4);
    public final List<Boolean> SIXTH_COLUMN = initColumn(5);
    public final List<Boolean> SEVENTH_COLUMN = initColumn(6);
    public final List<Boolean> EIGHTH_COLUMN = initColumn(7);
    public final List<Boolean> FIRST_ROW = initRow(0);
    public final List<Boolean> SECOND_ROW = initRow(8);
    public final List<Boolean> THIRD_ROW = initRow(16);
    public final List<Boolean> FOURTH_ROW = initRow(24);
    public final List<Boolean> FIFTH_ROW = initRow(32);
    public final List<Boolean> SIXTH_ROW = initRow(40);
    public final List<Boolean> SEVENTH_ROW = initRow(48);
    public final List<Boolean> EIGHTH_ROW = initRow(56);
    public final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final int NUM_TILES = 64;

    private static List<Boolean> initColumn(int columnNumber) {/*method that initializes a column with the specified column number.
        It uses a Boolean array to represent the column, where the tiles belonging to the column are marked as true.*/
        final Boolean[] column = new Boolean[NUM_TILES];
        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);
        return Collections.unmodifiableList(Arrays.asList((column)));
    }

    private static List<Boolean> initRow(int rowNumber) {/*method that initializes a row with the specified row number.
        it uses a Boolean array to represent the row, with tiles belonging to the row marked as true. */
        final Boolean[] row = new Boolean[NUM_TILES];
        Arrays.fill(row, false);
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % NUM_TILES_PER_ROW != 0);
        return Collections.unmodifiableList(Arrays.asList(row));
    }

    private Map<String, Integer> initializePositionToCoordinateMap() {/*method initializes a map that maps algebraic notation positions
        (e.g., "a1", "b2") to their corresponding coordinates on the board (0 to 63).*/
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static List<String> initializeAlgebraicNotation() {/*method initializes a list of strings representing algebraic notation positions in a standard chess board.*/
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

    public static boolean isValidTileCoordinate(final int coordinate) {/*method checks if a given coordinate is a valid tile coordinate on the chess board.*/
        return coordinate >= START_TILE_INDEX && coordinate < NUM_TILES;
    }

    public int getCoordinateAtPosition(final String position) {/*method retrieves the coordinate on the board corresponding to the given algebraic notation position*/
        return POSITION_TO_COORDINATE.get(position);
    }

    public String getPositionAtCoordinate(final int coordinate) {/*method retrieves the algebraic notation position corresponding to the given coordinate on the board.*/
        return ALGEBRAIC_NOTATION.get(coordinate);
    }

    public static boolean isThreatenedBoardImmediate(final Board board) {/*method checks if the board is in an immediate threatened state, where either the white or black player is in check*/
        return board.whitePlayer().isInCheck() || board.blackPlayer().isInCheck();
    }

    public static boolean kingThreat(final Move move) {/*method checks if a move puts the current player's king under threat.*/
        final Board board = move.getBoard();
        final MoveTransition transition = board.currentPlayer().makeMove(move);
        return transition.getToBoard().currentPlayer().isInCheck();
    }

    public static boolean isKingPawnTrap(final Board board,/*method checks if a king is trapped by an opponent pawn*/
                                         final King king,
                                         final int frontTile) {
        final Piece piece = board.getPiece(frontTile);
        return piece != null &&
                piece.getPieceType() == Piece.PieceType.PAWN &&
                piece.getPieceAllegiance() != king.getPieceAllegiance();
    }

    public static int mvvlva(final Move move) {/*method calculates the Most Valuable Victim / Least Valuable Attacker (MVV/LVA) score for a move. It is a heuristic used in move ordering to prioritize capturing moves*/
        final Piece movingPiece = move.getMovedPiece();
        if(move.isAttack()) {
            final Piece attackedPiece = move.getAttackedPiece();
            return (attackedPiece.getPieceValue() - movingPiece.getPieceValue() +  Piece.PieceType.KING.getPieceValue()) * 100;
        }
        return Piece.PieceType.KING.getPieceValue() - movingPiece.getPieceValue();
    }

    public static List<Move> lastNMoves(final Board board, int N) {/*method retrieves the last N moves from the board's move history*/
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while(currentMove != MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return Collections.unmodifiableList(moveHistory);
    }

    public static boolean isEndGame(final Board board) {/*method checks if the game has reached an end state, either checkmate or stalemate*/
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");

    }

    public static boolean isValidCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }


}