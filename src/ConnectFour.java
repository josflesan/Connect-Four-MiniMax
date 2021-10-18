import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * The main class of the Connect Four game.
 * You should not have to touch this code (except maybe for advanced features).
 *
 * @author David Symons
 */
public final class ConnectFour
{

	// Define file path
	public static final String FILEPATH = ".\\src\\save_file";

	/**
	 * The code provided for this assignment follows a design pattern called
	 * Model-View-Controller (MVC). The main method instantiates each of these
	 * components and then starts the game loop.
	 *
	 * @param args No arguments expected.
	 */
	public static void main(String[] args)
	{

		// This text-based view is used to communicate with the user.
		// It can print the state of the board and handles user input.
		TextView view = new TextView();

		// Ask user if they want to load the save file or not
		boolean load = userLoad(view);
		if (load) {
			// Load saved model
			Model loadedModel = loadSession(view);

			// Create a new model with same data
			// loadedFields format: {connectX, gameMode, currentPlayer, nrRows, nrCols}
			int[] loadedFields = new int[5];
			loadedFields[0] = loadedModel.getConnectX();
			loadedFields[1] = loadedModel.getGameMode();
			loadedFields[2] = loadedModel.getCurrentPlayer();
			loadedFields[3] = loadedModel.getNrRows();
			loadedFields[4] = loadedModel.getNrCols();

			char[][] loadedBoard = loadedModel.getBoard();
			TextView loadedView = loadedModel.getView();

			// Create new model using the alternative, overloaded constructor to pass in settings
			Model newModel = new Model(loadedFields, loadedBoard, loadedView);

			// Create a new controller with the new model and view
			Controller controller = new Controller(newModel, view, true);

			// Start game loop
			controller.startSession();

		}
		// Start a new game
		else {
			// Creates a model representing the state of the game.
			Model model = new Model();

			// The controller facilitates communication between model and view.
			// It also contains the main loop that controls the sequence of events.
			Controller controller = new Controller(model, view, false);

			// Start a new session.
			controller.startSession();
		}
	}

	// --------------------- GAME IO --------------------------
	private static boolean userLoad(TextView view)
	{
		view.displayLoadOrNewMessage();
		// Ask user for input
		int selection = InputUtil.readIntFromUser();
		while (selection > 2 || selection < 1) {
			// Output error message and ask for selection again
			view.displayInvalidLoadOrNewSelection();
			selection = InputUtil.readIntFromUser();
		}

		return (selection == 2);  // If selection is 2, then true, otherwise false
	}

	public static Model loadSession(TextView view)
	{

		Model model = null;  // By default, model is null

		// Load the game state object from a file
		try {
			FileInputStream fileIn = new FileInputStream(ConnectFour.FILEPATH);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			model = (Model) objectIn.readObject();

			view.displayLoadSuccessMessage();
			objectIn.close();
			return model;

		} catch (Exception ex) {
			ex.printStackTrace();  // Output stack trace for any exception
		}

		return model;
	}
}
