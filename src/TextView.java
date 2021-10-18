import java.io.Serializable;  // Used for save game feature (ADVANCED)

/**
 * This file is to be completed by you.
 *
 * @author s2088605
 */
public final class TextView implements Serializable
{

	private static final long serialVersionUID = 2;  // serialVersionUID for reading from file

	public TextView()
	{
	
	}

	// --------------------------------------- GAME IO MESSAGES --------------------------------------------------------

	public final void displayLoadOrNewMessage()
	{
		System.out.println("Would you like to start a new game (1) or load the last game (2)?");
	}

	public final void displayInvalidLoadOrNewSelection()
	{
		System.out.println("Would you like to start a new game (1) or load the last game (2)?");
	}

	public final void displayLoadSuccessMessage()
	{
		System.out.println("\nThe Game State has been successfully read from the file");
	}

	public final void displaySaveSuccessMessage()
	{
		System.out.println("\nThe Game State was successfully written to a file");
	}

	// --------------------------------------- GAME START MESSAGES -----------------------------------------------------
	
	public final void displayNewGameMessage()
	{
		System.out.println("---- NEW GAME STARTED ----");
		System.out.println("Title: Connect X");
		System.out.println("Players: 2");
		System.out.println("Instructions: To insert pieces into the board, insert \n" +
				"a valid column number (1-column size). To save the game, input 0. \n" +
				"To give up, input -1. May the best player win!");
		System.out.println("-".repeat(26));
		System.out.println();
	}

	// Function to output game instructions
	public final void displayGameInstructions()
	{
		// Print out instructions
		System.out.println("Please enter valid game settings...\n"
				+ "-".repeat(60));
		System.out.println("A valid Connect X Satisfies the following...\n"
				+ "\t·Both column size and row size must be greater than 1\n"
				+ "\t·Number of pieces in a row to win must be less than or equal to one of the 2 sizes\n"
				+ "\t·At least two pieces in a row to win\n"
				+ "\n\t(If you want to play the classic board game, enter 0 for both dimensions)\n"
				+ "-".repeat(60));
	}

	// Function to output each setting option message
	public final void displaySettingInputMessage(String msg) { System.out.print(msg); }

	// --------------------------------------- PLAYER TURN MSGS --------------------------------------------------------

	// Function to output player's turn (AI game mode)
	public final void displayPlayerTurn() { System.out.println("\n---- PLAYER'S TURN ----"); }

	// Function to output current player (Overload displayPlayerTurn)
	public final void displayPlayerTurn(int playerNum) { System.out.printf("\n---- PLAYER %d's TURN ----\n", playerNum); }

	// --------------------------------------- GAME OVER MESSAGES ------------------------------------------------------

	// Function to output game over win message
	public final void displayGameOverWinMessage(int playerNum)
	{
		System.out.printf("\n---- GAME OVER, PLAYER %d WINS ----\n\n", playerNum);
	}

	// Function to output game over win message (AI)
	public final void displayGameOverAIWinMessage() { System.out.println("\n---- GAME OVER, AI WINS ----"); }

	// Function to output game over draw message
	public final void displayGameOverDrawMessage() { System.out.println("\n---- GAME OVER, DRAW ----"); }

	// Function to output end of session message
	public final void displayEndOfSessionMessage() { System.out.println("See you next time then!"); }

	// -------------------------------------- GAME MESSAGES ------------------------------------------------------------

	public final void displayBoard(Model model)
	{
		int nrRows = model.getNrRows();
		int nrCols = model.getNrCols();
		// Get your board representation.

		// This can be used to print a line between each row.
		// You may need to vary the length to fit your representation.
		String rowDivider = "-".repeat(4 * nrCols + 1);

		// A StringBuilder is used to assemble longer Strings more efficiently.
		StringBuilder sb = new StringBuilder();

		// You can add to the String using its append method.
		// Use a for loop to print out rowDividers and pieces
		for (int i = 0; i < nrRows; i++) {
			sb.append(rowDivider);
			sb.append("\n");
			for (int j = 0; j < nrCols; j++) {
				sb.append("| ");
				sb.append(model.getBoard()[i][j]);
				sb.append(" ");
			}
			sb.append("|\n");
		}
		sb.append(rowDivider);  // Print out last rowDivider

		// Then print out the assembled String.
		System.out.println(sb);
	}

	public final int askForMove()
	{
		System.out.print("Select a free column: ");
		return InputUtil.readIntFromUser();
	}

	public final void isMoveValidMessageNoSpace (int move)
	{
		System.out.printf("ERROR: Cannot insert piece in column %d because it is full\n", move);
	}

	public final void isMoveValidMessageInvalidColumn (int move)
	{
		System.out.printf("ERROR: Cannot insert piece in column %d because it is full\n", move);
	}

	// --------------------------------------- OTHER MESSAGES ----------------------------------------------------------

	// Game mode messages...
	public final void displayGameModeMessage()
	{
		// Enter 2 player or AI
		System.out.println("-".repeat(30) + "\n"
				+ "1 - Versus AI\n"
				+ "2 - 2 Player\n"
				+ "-".repeat(30));
	}

	public final void displayTryAgainGameModeMessage() { System.out.println("\nInvalid game mode, please try again..."); }

	// New Game Messages...
	public final void displayNewGameConfirmationMessage()
	{
		System.out.println("Do you want to start a new game? (y/n): ");
	}

	public final void displayInvalidNewGameConfirmationInput() {
		System.out.println("Please input 'y' or 'n' ONLY...");
	}

	// Input validation messages...
	public final void displayTryAgainMessage() { System.out.println("Please try again..."); }

	// Outputs an empty line
	public final void spacer() { System.out.println(); }

}
