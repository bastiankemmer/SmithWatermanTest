package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import smithWatermanAlgorithmus.SmithWaterman;

public class Controller {

	private View view;
	private SmithWaterman sw;


	public Controller(SmithWaterman sw, View view){
		this.sw = sw;
		this.view = view;

		view.getStart().setOnAction(new StartAlgEventHandler());

	}

	//Anzeigen GUI
	public void showView(Stage primaryStage) {
		this.view.show(primaryStage);
	}

	//Event Handler des Start Buttons
	class StartAlgEventHandler implements EventHandler<ActionEvent>{

		public void handle(ActionEvent e){
			if(view.getSequence1().getText().isEmpty()){
				view.Alarm("Bitte Sequenz 1 eingeben");
			}

			if(view.getSequence2().getText().isEmpty()){
				view.Alarm("Bitte Sequenz 2 eingeben");
			}

			if(view.getSequence1().getText().contains(" ")){
				view.Alarm("Bitte Leerzeichen in Sequenz 1 entfernen");
			}

			if(view.getSequence2().getText().contains(" ")){
				view.Alarm("Bitte Leerzeichen in Sequenz 2 entfernen");
			}

			//SmithWaterman sw = new SmithWaterman(view.getSequence1().getText(), view.getSequence2().getText());
			sw.printAlignments();
			sw.printDPMatrix();
		}
	}
}
