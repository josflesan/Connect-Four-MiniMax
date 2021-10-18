import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

/**
 * This file is to be completed by you.
 *
 * @author s2088605
 */
public final class Model implements Serializable
{

	private static final long serialVersionUID = 1;  // serialVersionUID for reading from file

	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================
	// The most common version of Connect Four has 6 rows and 7 columns.
	public static final int DEFAULT_NR_ROWS = 6;
	public static final int DEFAULT_NR_COLS = 7;

	// Player tokens will be represented with 'X's or 'O's
	// Index 0 = PLAYER_1
	// Index 1 = PLAYER_2
	public static final char[] PLAYER_TOKENS = {'X', 'O'};

	// Give up move
	public static final int GIVE_UP = -1;

	// Save
	public static final int SAVE = 0;

	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================
	// The size of the board.
	private int nrRows;
	private int nrCols;

	// Text View
	private final TextView view;

	// Number of pieces in a row to win
	private int connectX;

	// Game Mode (1 - vs AI, 2 - 2 Player)
	private int gameMode;

	// Meta-Variables relating to state of game
	private int currentPlayer;  // Can be either 1 or 2

	// Define the board as a 2 dimensional array
	private char[][] board;

	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================

	// New Game Model
	public Model()
	{
		view = new TextView();
	}

	public Model(int[] fields, char[][] board, TextView view)
	{
		// Fields structure...
		// {connectX, gameMode, currentPlayer, nrRows, nrCols}
		this.connectX = fields[0];
		gameMode = fields[1];
		currentPlayer = fields[2];
		this.board = board;
		this.view = view;
		this.nrRows = fields[3];
		this.nrCols = fields[4];
	}

	// ---------------------- GAME IO ---------------------------

	public void saveSession()
	{
		// Save the game state object into a file
		try {
			FileOutputStream fileOut = new FileOutputStream(ConnectFour.FILEPATH);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(this);
			objectOut.close();
			view.displaySaveSuccessMessage();
		} catch (Exception ex) {
			ex.printStackTrace();  // Output stack trace for any exception
		}
	}
	
	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================

	public void startNewGame(int[] settings)
	{
		// Settings Array Format...
		// [rowNum, colNum, connectX, gameMode]

		// Initialise the board size to its default values.
		nrRows = settings[0];
		nrCols = settings[1];

		connectX = settings[2];

		// Initialise the state of the game
		currentPlayer = 1;
		gameMode = settings[3];

		// Initialise the board - empty by default
		board = new char[nrRows][nrCols];
		for (int i = 0; i < nrRows; i++)
		{
			for (int j = 0; j < nrCols; j++)
			{
				board[i][j] = ' ';  // space for empty slot
			}
		}
	}

	public boolean isBoardFull(char[][] board)
	{
		/* Check to see if there are no more possible moves
		* to be made, return true if so */

		// If the first array (top-most row) is full, then board is full
		int freeSpacesRow0 = 0;
		for (int col = 0; col < nrCols; col++)
		{
			if (board[0][col] == ' ')
			{
				// If space, then there are possible moves
				freeSpacesRow0++;
			}
		}

		return (freeSpacesRow0 == 0);  // If true, then no free spaces, else false

	}

	public boolean checkWin(char[][] board, char token)
	{
		boolean win = false;  // By default, no one has won

		// Horizontal Check

		boolean horizontal = GameRules.horizontalCheck(nrRows, nrCols, board, connectX, token);
		boolean vertical = GameRules.verticalCheck(nrRows, nrCols, board, connectX, token);
		// By default, diagonal checks are false
		boolean diagonalAscending = false;
		boolean diagonalDescending = false;

		// Only check diagonals if diagonal wins are possible
		// ie. connectX is greater than or equal to both dimensions
		if (nrCols >= connectX && nrRows >= connectX) {
			diagonalAscending = GameRules.diagonalAscCheck(nrRows, nrCols, board, connectX, token);
			diagonalDescending = GameRules.diagonalDescCheck(nrRows, nrCols, board, connectX, token);
		}

		// If any of the tests return true, then there's a winner
		if (horizontal || vertical || diagonalAscending || diagonalDescending) {
			win = true;
		}

		return win;

	}
	
	public void makeMove(int move)
	{
		// Find free row index, start at bottom
		int rowNum = nrRows - 1;
		while (board[rowNum][move-1] != ' ')
		{
			rowNum--;  // Move up one row each iteration
		}

		// Play piece in the selected position found
		// Subtract 1 from currentPlayer to get appropriate index
		board[rowNum][move-1] = PLAYER_TOKENS[currentPlayer -1];
	}

	public void switchPlayer()
	{
		// If player 1... (1 % 2) + 1 = 2
		// If player 2... (2 % 2) + 1 = 1
		currentPlayer = (currentPlayer % 2) + 1;
	}
	
	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================
	public int getNrRows() { return this.nrRows; }
	
	public int getNrCols() { return this.nrCols; }

	public int getConnectX() { return this.connectX; }

	public int getGameMode() { return this.gameMode; }

	public int getCurrentPlayer() { return this.currentPlayer; }

	public char[][] getBoard() { return this.board; }

	public char getOpponentPlayerToken() {
		if (currentPlayer == 1) return PLAYER_TOKENS[1];
		else return PLAYER_TOKENS[0];
	}

	public TextView getView() { return this.view; }
}
