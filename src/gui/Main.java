package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import smithWatermanAlgorithmus.SmithWaterman;

public class Main extends Application{

	private View view;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Controller controller = new Controller(new SmithWaterman(null), new View());
		controller.showView(primaryStage);
	}

}
