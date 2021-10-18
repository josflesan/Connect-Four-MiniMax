/**
 * This file is to be completed by you.
 *
 * @author s2088605
 */
public final class Controller {
	private final Model model;
	private final TextView view;

	private final MinimaxAI ai;

	public Controller(Model model, TextView view, boolean loading)
	{
		this.model = model;
		this.view = view;
		this.ai = new MinimaxAI(model);  // Initialise ai

		// If not loading, then start a new game with new settings
		if (!loading) {
			// Get Game settings
			int[] settings = inputSettings();
			// Start new game with settings
			this.model.startNewGame(settings);
		}

	}

	private int selectGameMode()
	{
		view.displayGameModeMessage();

		int gameMode = InputUtil.readIntFromUser();
		while (gameMode > 2 || gameMode < 1) {
			view.displayTryAgainGameModeMessage();
			gameMode = InputUtil.readIntFromUser();
		}

		return gameMode;
	}

	public int[] inputSettings()
	{
		// Output new game message
		view.displayNewGameMessage();

		int rowSize, colSize, connectX, gameMode;
		int[] settings = new int[4];
		do {

			view.displayGameInstructions();

			// Read row size from user
			view.displaySettingInputMessage("Row size: ");
			rowSize = InputUtil.readIntFromUser();

			// Read column size from user
			view.displaySettingInputMessage("Column size: ");
			colSize = InputUtil.readIntFromUser();

			// If no input for rowSize and colSize, default size
			if (colSize == 0 && rowSize == 0) {
				rowSize = Model.DEFAULT_NR_ROWS;
				colSize = Model.DEFAULT_NR_COLS;
			}

			// Read connectX from user
			view.displaySettingInputMessage("How many pieces in a row to win?: ");
			connectX = InputUtil.readIntFromUser();

			// Print empty line
			view.spacer();

			gameMode = selectGameMode();

			// Populate settings array
			settings[0] = rowSize;
			settings[1] = colSize;
			settings[2] = connectX;
			settings[3] = gameMode;

		} while ((connectX > rowSize && connectX > colSize)
				|| colSize == 1
				|| rowSize == 1
				|| connectX == 1);

		return settings;
	}

	public void startSession ()
	{
		// Game Loop - Infinite for now
		boolean endGame = false;

		// Display empty board
		view.displayBoard(model);
		while (!endGame) {

			// If player 1's turn...
			if (model.getCurrentPlayer() == 1) {

				// Display player's turn
				if (model.getGameMode() == 1) view.displayPlayerTurn();
				else view.displayPlayerTurn(model.getCurrentPlayer());

				endGame = askForMove();  // Exit game loop if true
			}
			// If other player's turn...
			else {

				// If AI's turn...
				if (model.getGameMode() == 1) {
					// Make AI's move
					int[] colPlusScore;  // Stores the column in index 0 and score in index 1
					char[][] deepCopyBoard = new char[model.getNrRows()][model.getNrCols()];

					// Deep Copy Board Array
					for (int i = 0; i <= model.getNrRows() - 1; i++) {
						for (int j = 0; j <= model.getNrCols() - 1; j++) {
							deepCopyBoard[i][j] = model.getBoard()[i][j];
						}
					}

					// If board size is large, use large depth, otherwise, small depth
					colPlusScore = ai.MiniMax(deepCopyBoard, 10, -1000000000, 1000000000, true);
					int col = colPlusScore[0] + 1;
					if (col == 0) col++;  // Recursion depth may be too big for smaller sized boards

					endGame = checkAndMove(col);
				}
				// If Player 2's turn...
				else {
					// Display player's turn
					view.displayPlayerTurn(model.getCurrentPlayer());

					endGame = askForMove();
				}
			}

			// Switch Player
			model.switchPlayer();

		}
	}

	// Handle message output when a player wins
	public boolean win()
	{
		// Get current token
		char currentToken = Model.PLAYER_TOKENS[model.getCurrentPlayer() -1];

		// Check win
		if (model.checkWin(model.getBoard(), currentToken)) {

			// If AI won, display ai win message
			if (model.getCurrentPlayer() == 2 && model.getGameMode() == 1) {
				view.displayGameOverAIWinMessage();
			}
			// Otherwise, display appropriate message
			else {
				view.displayGameOverWinMessage(model.getCurrentPlayer());
			}

		}

		return model.checkWin(model.getBoard(), currentToken);
	}

	// Handle message output if a move is not valid
	public boolean isMoveValid(int move)
	{
		/* Check if the last position in the array is empty for
		 * the specified column. If not, the move is not valid (ie. no space) */

		// If the first array has a space in the column inputted, valid
		// NB1. First array is the top-most row in the representation
		// NB2. Subtract 1 to the column number in move because array indices starts at 0 in Java
		try
		{
			if (model.getBoard()[0][move-1] == ' ')
			{
				return true;
			} else {
				// If there is no space in the column, invalid move
				view.isMoveValidMessageNoSpace(move);
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e)
		{
			// column does not exist
			view.isMoveValidMessageInvalidColumn(move);
			return false;
		}

	}

	public void resetGame()
	{
		view.displayNewGameConfirmationMessage();
		// Turn character to upper case to facilitate condition
		char ans = Character.toUpperCase(InputUtil.readCharFromUser());
		while (ans != 'Y' && ans != 'N') {
			view.displayInvalidNewGameConfirmationInput();
			ans = Character.toUpperCase(InputUtil.readCharFromUser());
		}

		if (Character.toUpperCase(ans) == 'Y') {
			int[] settings = inputSettings();  // new settings
			model.startNewGame(settings);  // reset game state
			startSession();
		} else {
			// Program ends
			view.displayEndOfSessionMessage();
		}
	}

	public boolean checkAndMove(int move)
	{

		boolean endGame;

		// If player gives up or someone won, output game over message and end program
		if (move == Model.GIVE_UP) {
			// If 2 Player mode...
			if (model.getGameMode() == 1) {
				// Switch player to winner
				model.switchPlayer();
				view.displayGameOverWinMessage(model.getCurrentPlayer());
			} else {
				// Display AI wins message
				view.displayGameOverAIWinMessage();
			}
			resetGame();
			endGame = true;
		}
		else {

			// Check move is valid, otherwise ask again
			while (!isMoveValid(move)) {
				view.displayTryAgainMessage();
				move = view.askForMove();
			}

			// Make Move
			model.makeMove(move);

			// Display Board
			view.displayBoard(model);

			// Check win
			endGame = win();

			// If no win, ceck if board is full...
			if (model.isBoardFull(model.getBoard()) && !endGame) {
				// If no more moves available, also game over, but draw
				view.displayGameOverDrawMessage();
				resetGame();
				endGame = true;
			}
		}

		return endGame;

	}

	public boolean askForMove()
	{
		int move;  // By default, move is -1
		boolean endGame = false;  // By default, game doesn't end

		// If player's turn...
		if (model.getCurrentPlayer() == 1 || (model.getCurrentPlayer() == 2 && model.getGameMode() != 1)) {
			// Ask for move
			move = view.askForMove();

			if (move == Model.SAVE) {
				model.saveSession();  // If the player inputs a 0, save the session
				resetGame();
				return true;  // Exit the loop
			}

			endGame = checkAndMove(move);  // Check if move ends the game and make move
		}

		return endGame;
	}
}