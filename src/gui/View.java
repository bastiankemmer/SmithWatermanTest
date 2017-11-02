package gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class View {

    private TextField sequence1;
    private TextField sequence2;
    private TextArea dpMatrix;
    private Label label1;
    private Label label2;
    private Button start;

    private Scene scene;
    private GridPane gridPane;

    private HBox hBox;

    public View() {
        // Layout
        this.gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        label1 = new Label("Einfügen Sequenz 1");
        gridPane.add(label1, 0, 0);

        sequence1 = new TextField();
        gridPane.add(sequence1, 0, 1);

        label2 = new Label("Einfügen Sequenz 2");
        gridPane.add(label2, 0, 2);

        sequence2 = new TextField();
        gridPane.add(sequence2, 0, 3);

        start = new Button("Start Alignment");
        gridPane.add(start, 0, 4);

        dpMatrix = new TextArea();
        dpMatrix.setFont(Font.font("Courier New"));
        gridPane.add(dpMatrix, 0, 5, 1, 18);

        scene = new Scene(gridPane, 500, 500);


    }

    // Anzeigen GUI
    public void show(Stage primaryStage) {
        primaryStage.setTitle("Smith-Waterman-Algorithmus");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //Getter Methoden
    public TextField getSequence1() {
        return sequence1;
    }

    public TextField getSequence2() {
        return sequence2;
    }

    public Button getStart() {
        return start;
    }

    public TextArea getDpMatrix() {
        return dpMatrix;
    }

    //Alarmmeldung
    public void Alarm(String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Ok drücken und neu versuchen", ButtonType.OK);
        alert.setTitle("Warnung");
        alert.setHeaderText(string);
        alert.showAndWait();
    }

}
