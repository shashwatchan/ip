package noga;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Noga noga;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Noga instance */
    public void setNoga(Noga d) {
        noga = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Noga's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = noga.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, null),
                DialogBox.getNogaDialog(response, null)
        );
        userInput.clear();
    }
}
