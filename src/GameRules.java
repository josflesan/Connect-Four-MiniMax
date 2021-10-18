/**
 * This file compiles the connect X rules.
 *
 * @author s2088605
 */

public final class GameRules {


    // Horizontal Check
    public static boolean horizontalCheck(int nrRows, int nrCols, char[][] board,
                                          int connectX, char currentToken)
    {

        int count;
        boolean win = false;

        // From the first to the max row...
        for (int row = 0; row <= nrRows - 1; row++)
        {
            count = 0;  // Restart counter every new row

            // From the first to the max column...
            for (int col = 0; col <= nrCols - 1; col++)
            {

                // If x in a row, then return true
                if (board[row][col] == currentToken)
                {
                    count++;
                    if (count >= connectX) win = true;
                } else {
                    count = 0;
                }
            }
        }

        return win;
    }


    // Vertical Check
    public static boolean verticalCheck(int nrRows, int nrCols, char[][] board,
                                        int connectX, char currentToken)
    {

        int count;
        boolean win = false;

        // From the first to the max column...
        for (int col = 0; col <= nrCols - 1; col++)
        {
            count = 0;  // Restart counter every new column

            // From the first to the max row...
            for (int row = 0; row <= nrRows - 1; row++) {

                // If x in a column, then return true
                if (board[row][col] == currentToken)
                {
                    count++;
                    if (count >= connectX) win = true;
                } else {
                    count = 0;
                }
            }
        }

        return win;
    }


    // Ascending Diagonal Check
    public static boolean diagonalAscCheck(int nrRows, int nrCols, char[][] board,
                                           int connectX, char currentToken)
    {
        int count = 0;
        boolean win = false;

        // Check upper triangular half of board (rows)
        // Starting row is the (connectX - 1) row, the maximum row is the last one...
        for (int startRow = (connectX - 1); startRow <= nrRows - 1; startRow++) {
            // From the first col to the max col and from the starting row to the first row...
            for (int col = 0, row = startRow; (col <= nrCols - 1 && row >= 0); col++, row--) {
                // If x in diagonal, return true
                if (board[row][col] == currentToken) {
                    count++;
                    if (count >= connectX) win = true;
                } else {
                    count = 0;
                }
            }
        }

        count = 0;  // reset counter

        // Check lower triangular half of board (cols)
        // Starting column starts at 0, ends at (max cols - connectX)...
        for (int startCol = 0; startCol <= nrCols - connectX; startCol++) {
            // From the last row to the first and from the starting column to the last column...
            for (int row = (nrRows - 1), col = startCol; (col <= nrCols - 1 && row >= 0); col++, row--) {
                // If x in diagonal, return true
                if (board[row][col] == currentToken) {
                    count++;
                    if (count >= connectX) win = true;
                } else {
                    count = 0;
                }
            }
        }

        return win;

    }


    // Descending Diagonal Check
    public static boolean diagonalDescCheck(int nrRows, int nrCols, char[][] board,
                                            int connectX, char currentToken)
    {
        int count = 0;
        boolean win = false;

        // Descending Diagonal Check

        // Check lower triangular half (rows)
        // Starting row is first row, last row is (max row - connectX) row
        for (int startRow = 0; startRow <= nrRows - connectX; startRow++) {
            // From first col to last column and from starting row to last row...
            for (int col = 0, row = startRow; (col <= nrCols - 1 && row <= nrRows - 1); col++, row++) {
                // If x in diagonal, return true
                if (board[row][col] == currentToken) {
                    count++;
                    if (count >= connectX) win = true;
                } else {
                    count = 0;
                }
            }
        }

        count = 0;  // reset counter

        // Check upper triangular half (cols)
        // Starting column is second column, ending column is (max col - connectX)
        for (int startCol = 1; startCol <= nrCols - connectX; startCol++) {
            // From second column to last column and from first row to last row...
            for (int col = startCol, row = 0; (col <= nrCols - 1 && row <= nrRows - 1); col++, row++) {
                // If x in diagonal, return true
                if (board[row][col] == currentToken) {
                    count++;
                    if (count >= connectX) win = true;
                } else {
                    count = 0;
                }
            }
        }

        return win;
    }

}
