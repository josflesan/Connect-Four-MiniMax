import java.util.Random;

/**
 * This file implements the Minimax AI algorithm
 *
 * @author s2088605
 */

public final class MinimaxAI {

    // Variable that holds which token this player controls
    private final char aiToken;
    // Variable that holds the current game state
    private final Model state;

    // ----------------------- CONSTRUCTOR -----------------------

    public MinimaxAI(Model gameState)
    {
        aiToken = 'O';  // AI player will always be O if not specified
        this.state = gameState;
    }

    // ----------------------- BEHAVIOUR -----------------------

    // MiniMax algorithm
    public int[] MiniMax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer)
    {
        // Get valid locations
        int[] validLocations = getValidColumns(board);
        // Determine whether the node is a terminal node
        boolean isTerminal = isTerminalNode(board);

        if (isTerminal || depth == 0) {
            if (isTerminal) {
                // If the current player is the AI...
                if (state.checkWin(board, aiToken)) {
                    return new int[]{-1, 1000000000};
                }
                // If it's the opposite player...
                else if (state.checkWin(board, 'X')) {
                    return new int[]{-1, -1000000000};
                }
                // If it's a draw...
                else {
                    return new int[]{-1, 0};
                }
            } else {
                // Depth is zero
                return new int[]{-1, scoreBoard(board, this.aiToken)};
            }
        }

        // If it's the maximizing player...
        int value;
        int column;
        if (maximizingPlayer) {
            // Check against -infinity
            value = -1000000000;
            // Pick a random column
            column = validLocations[new Random().nextInt(validLocations.length - 1)];

            // Cycle through all of the columns in valid columns
            for (int col : validLocations) {

                // If column is not full...
                if (col != -1) {
                    int row = getNextRow(board, col);
                    char[][] boardCopy = deepCopyBoard(board);
                    boardCopy[row][col] = aiToken;  // Insert piece at that point
                    // Calculate new score and call function recursively
                    int newScore = MiniMax(boardCopy, depth-1, alpha, beta, false)[1];
                    if (newScore > value) {
                        // Update reward and selected column
                        value = newScore;
                        column = col;
                    }
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) break;
                }
            }

        }
        // If it's the minimising player, similar procedure...
        else {
            // Initial comparison value is +infinity
            value = 1000000000;
            // Pick random column
            column = validLocations[new Random().nextInt(validLocations.length)];

            // Cycle through all of the valid columns...
            for (int col : validLocations) {

                // If column is not full...
                if (col != -1) {
                    int row = getNextRow(board, col);
                    char[][] boardCopy = deepCopyBoard(board);
                    boardCopy[row][col] = state.getOpponentPlayerToken();
                    int newScore = MiniMax(boardCopy, depth-1, alpha, beta, true)[1];

                    if (newScore < value) {
                        value = newScore;
                        column = col;
                    }
                    beta = Math.min(beta, value);
                    if (alpha >= beta) break;
                }

            }

        }
        return new int[]{column, value};

    }


    /*
    * Part 2 of the heuristic function for the AI
    * Counts the number of spaces, player and ai pieces
    * and returns a score for the given sub-array.
    * Score is totalled in scorePosition function to yield score for given board.
    * */
    private int evaluateSubArray(char[] subArrayX, char piece)
    {
        int score = 0;
        char oppPiece = 'X';

        // If piece is first player, switch
        if (piece == 'X') {
            oppPiece = this.aiToken;
        }

        int countAI = 0;  // Number of pieces AI
        int countPlayer = 0;  // Number of pieces Player
        int countSpace = 0;  // Space count

        for (int i = 0; i <= subArrayX.length - 1; i++) {

            if (subArrayX[i] == piece) {
                countAI++;
            }
            else if (subArrayX[i] == oppPiece) {
                countPlayer++;
            }
            else if (subArrayX[i] == ' ') {
                countSpace++;
            }

        }

        if (countAI == this.state.getConnectX()) score += 100;
        else if (countAI == this.state.getConnectX() - 1 && countSpace == 1) score += 5;
        else if (countAI == this.state.getConnectX() - 2 && countSpace == 2) score += 2;
        if (countPlayer == this.state.getConnectX() - 1 && countSpace == 1) score -= 4;


        return score;
    }

    /*
    * Part 1 of the heuristic function for the AI.
    * Cycles every row and column of a given board
    * and extracts sub-arrays of length CONNECT_X to evaluate.
    * Bias introduced for center column. (Strategic move)
    * */
    private int scoreBoard(char[][] board, char token)
    {
        int score = 0;

        // Score centre column (introduce bias for center column moves)
        char[] centerColumn = extractCol(board, this.state.getNrCols() / 2);
        int centerCount = 0;
        for (int i = 0; i <= centerColumn.length - 1; i++) {
            if (centerColumn[i] == token) {
                centerCount++;
            }
        }
        score += centerCount * 3;

        // Score horizontal
        for (int r = 0; r <= this.state.getNrRows() - 1; r++) {
            char[] row = extractRow(board, r);

            // Extract sub-arrays of length X to check
            for (int startingPos = 0; startingPos <= row.length - this.state.getConnectX(); startingPos++) {
                char[] xSection = new char[this.state.getConnectX()];  // Make a section of length x to check
                for (int j = 0; j <= this.state.getConnectX() - 1; j++) {
                    xSection[j] = row[startingPos + j];
                }
                score += evaluateSubArray(xSection, token);
            }
        }

        // Score vertical
        for (int c = 0; c <= this.state.getNrCols() - 1; c++) {
            char[] col = extractCol(board, c);

            // Extract sub-arrays of length X to check
            for (int startingPos = 0; startingPos <= col.length - this.state.getConnectX(); startingPos++) {
                char[] xSection = new char[this.state.getConnectX()];  // Make a section of length x to check
                for (int j = 0; j <= this.state.getConnectX() - 1; j++) {
                    xSection[j] = col[startingPos+j];
                }
                score += evaluateSubArray(xSection, token);
            }

        }

        // Improvement: Implement Diagonal Checks for AI

        return score;

    }


    /*
    * Determine whether or not the node is terminal (either player won or board is full)
    */
    private boolean isTerminalNode(char[][] board)
    {
        int[] noColumnsAvailable = new int[this.state.getNrCols()];
        for (int i = 0; i <= this.state.getNrCols() - 1; i++) { noColumnsAvailable[i] = -1; }
        return this.state.checkWin(board, 'X')
                || this.state.checkWin(board, 'O')
                || getValidColumns(board) == noColumnsAvailable;
    }


    // ---------------------- UTILITIES ----------------------

    // Deep Copy Board
    public char[][] deepCopyBoard(char[][] board)
    {
        char[][] copiedBoard = new char[state.getNrRows()][state.getNrCols()];

        for (int i = 0; i <= state.getNrRows() - 1; i++) {
            for (int j = 0; j <= state.getNrCols() - 1; j++) {
                copiedBoard[i][j] = board[i][j];
            }
        }

        return copiedBoard;
    }

    /*
     * Utility function that returns the next open row given a column position
     * */
    private int getNextRow(char[][] board, int col)
    {

        int res = -1;

        // Cycle through the rows backwards...
        for (int row = this.state.getNrRows() - 1; row >= 0; row--) {
            // If the value of the row at that column is empty, return row
            if (board[row][col] == ' ') {
                res = row;
                break;
            }
        }

        return res;
    }

    /*
     * Utility function that returns the list of valid columns in the current board
     */
    private int[] getValidColumns(char[][] board)
    {
        int numCols = this.state.getNrCols();

        int[] validCols = new int[numCols];

        // Initialise validCols with -1 (No columns free)
        for (int i = 0; i <= validCols.length - 1; i++) { validCols[i] = -1; }

        for (int col = 0; col <= numCols - 1; col++) {
            if (board[0][col] == ' ') validCols[col] = col;  // If not full, valid
        }

        return validCols;
    }

    /*
     * Utility function that extracts a row from the board given row index
     * */
    private char[] extractRow(char[][] board, int pos)
    {
        char[] row = new char[this.state.getNrCols()];
        for (int i = 0; i <= this.state.getNrCols() - 1; i++) {
            row[i] = board[pos][i];
        }
        return row;
    }

    /*
     * Utility function that extracts a column from the board given col index
     * */
    private char[] extractCol(char[][] board, int pos)
    {
        char[] col = new char[this.state.getNrRows()];
        for (int i = 0; i <= this.state.getNrRows() - 1; i++) {
            col[i] = board[i][pos];
        }

        return col;
    }

}
