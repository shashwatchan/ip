package noga;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    private Button sendButton;  // Button to send messages

    private Noga noga;  // The chatbot instance

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.jpeg"));
    private Image nogaImage = new Image(this.getClass().getResourceAsStream("/images/Noga.jpeg"));

    @FXML
    public void initialize() {
        // Bind scroll pane to dialog container height for auto-scrolling
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
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getNogaDialog(response, nogaImage)
        );
        userInput.clear();
    }
}
