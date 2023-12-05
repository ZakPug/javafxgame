import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class JavaFXGame extends Application {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = new PrintStream(outputStream);
    private final TextArea textArea = new TextArea();
    private final TextField inputField = new TextField();
    private final Game game = new Game("Player");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the JavaFX UI
        setupUI(primaryStage);

        // Redirect the system output to capture game output
        redirectSystemOutput();

        // Start the game in a separate thread
        startGameThread();
    }

    private void setupUI(Stage primaryStage) {
        // Set up UI components
        textArea.setEditable(false);

        // Use VBox layout for vertical arrangement of UI elements
        VBox root = new VBox(10, textArea, inputField);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("JavaFX Adventure Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set up event handling for user input
        inputField.setOnAction(event -> {
            processInput(inputField.getText());
            inputField.clear();
        });
    }

    private void redirectSystemOutput() {
        // Redirect standard output and error streams
        System.setOut(printStream);
        System.setErr(printStream);
    }

    private void startGameThread() {
        // Start a separate thread for the game
        new Thread(() -> {
            game.start();

            // Update the JavaFX UI with the captured game output
            Platform.runLater(() -> textArea.setText(outputStream.toString()));
        }).start();
    }

    private void processInput(String input) {
        // Process user input and update the UI
        game.handleInput(input);
        textArea.setText(outputStream.toString());
    }
}
