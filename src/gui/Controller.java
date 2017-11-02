package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import smithWatermanAlgorithmus.SmithWaterman;

public class Controller {

    private View view;


    public Controller(View view) {
        this.view = view;

        view.getStart().setOnAction(new StartAlgEventHandler());
    }

    //Anzeigen GUI
    public void showView(Stage primaryStage) {
        this.view.show(primaryStage);
    }

    //Event Handler des Start Buttons
    class StartAlgEventHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            boolean computeSmithWaterman = true;
            if (view.getSequence1().getText().isEmpty()) {
                view.Alarm("Bitte Sequenz 1 eingeben");
                computeSmithWaterman = false;
            }

            if (view.getSequence2().getText().isEmpty()) {
                view.Alarm("Bitte Sequenz 2 eingeben");
                computeSmithWaterman = false;
            }

            if (view.getSequence1().getText().contains(" ")) {
                view.Alarm("Bitte Leerzeichen in Sequenz 1 entfernen");
                computeSmithWaterman = false;
            }

            if (view.getSequence2().getText().contains(" ")) {
                view.Alarm("Bitte Leerzeichen in Sequenz 2 entfernen");
                computeSmithWaterman = false;
            }

            if (computeSmithWaterman) {
                SmithWaterman sw = new SmithWaterman(view.getSequence1().getText(), view.getSequence2().getText());
                try {
                    String alignments = sw.printAlignments();
                    String dpMatrix = sw.printDPMatrix();

                    view.getDpMatrix().setText(alignments + "\n\n" + dpMatrix);
                } catch (EmptyAlignmentException | BackTrackException e1) {
                    e1.printStackTrace();
                    view.Alarm(e1.getMessage());
                }
            }
        }
    }
}
