package application;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import Nrmeen.HillCipher;
import Nrmeen.MException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

// HillDirectEnc class extending Stage for direct encryption using Hill Cipher
public class HillDirectEnc extends Stage {

	// GUI components
	private Label Text = new Label("Text: ");
	private TextField TextField = new TextField();

	private Label slabel = new Label("Seed: ");
	private TextField sTextField = new TextField();

	private Label cipherL = new Label("Cipher: ");
	private TextField cipherTF = new TextField();

	private GridPane gridPane = new GridPane();
	private File file = new File("seeds.txt"); // seeds file
	private boolean isDuplicateS = false; // to indicate a duplicated seed
	private ExtensionFilter txt = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private HillCipher hillCipher = new HillCipher();

	// Constructor for HillDirectEnc
	public HillDirectEnc(Stage stage) {
		setTitle("Encryption");
		cipherTF.setEditable(false);
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.add(Text, 0, 0);
		gridPane.add(TextField, 1, 0);
		gridPane.add(slabel, 0, 1);
		gridPane.add(sTextField, 1, 1);
		gridPane.add(cipherL, 0, 2);
		gridPane.add(cipherTF, 1, 2);
		gridPane.setAlignment(Pos.CENTER);

		VBox encryptionVBox = new VBox(10);
		encryptionVBox.setAlignment(Pos.CENTER);
		Button encryptButton = new Button("Encrypt");
		encryptButton.setStyle("-fx-font-weight: bold;");
		encryptButton.setOnAction(e -> {
			// Check for empty fields
			String plainText = TextField.getText().trim();
			String seed = sTextField.getText().trim();
			if (plainText.isEmpty() || seed.isEmpty()) {
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill in all fields.");
				emptyFieldAlert.showAndWait();
				return;
			}

			try (Scanner reader = new Scanner(file);
					PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
				// Check if the seed was used before
				while (reader.hasNextLine()) {
					String line = reader.nextLine();
					if (line.equalsIgnoreCase(seed)) {
						isDuplicateS = true; // The seed was used before
						break;
					}
				}
				if (isDuplicateS) {
					Alert redundantSeedAlert = new Alert(AlertType.INFORMATION);
					redundantSeedAlert.setTitle("error");
					redundantSeedAlert.setHeaderText(null);
					redundantSeedAlert.setContentText("the seed was used before");
					redundantSeedAlert.showAndWait();
				} else {
					// Print the new seed to the file
					writer.println(seed);
					// Generate a key with inverse
					char i = 'a';
					int[][] key = new int[3][3];
					do {
						seed += i;
						key = hillCipher.Generation(seed);
						hillCipher.setKey(key);
						i++;
					} while (hillCipher.inverseOfDeterminant() == 0);
					// Encryption
					hillCipher.setText(plainText);
					String cipherText = hillCipher.encryption(key, hillCipher.getText());
					// Put cipher text in the text field
					cipherTF.setText(cipherText);
					// Save cipher text to the file
					hillCipher.setWF(file);
					hillCipher.writef(cipherText);
				}
			} catch (FileNotFoundException e1) {
				Alert fileNotFoundError = new Alert(AlertType.ERROR);
				fileNotFoundError.setTitle("File Not Found");
				fileNotFoundError.setHeaderText(null);
				fileNotFoundError.setContentText("File is not found");
				fileNotFoundError.showAndWait();
			} catch (NullPointerException ex) {
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("File Not Selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("You did not select any file");
				noFileWasSelectedError.showAndWait();
			} catch (MException e1) {
				Alert matrixError = new Alert(AlertType.ERROR);
				matrixError.setTitle("Square Matrix");
				matrixError.setHeaderText(null);
				matrixError.setContentText("The matrix is not square");
				matrixError.showAndWait();
			}
		});

		Button backButton = new Button("Back");
		backButton.setStyle("-fx-font-weight: bold;");
		backButton.setOnAction(e -> {
			close();
			stage.show();
		});
		HBox buttonsHBox = new HBox(10);
		buttonsHBox.getChildren().addAll(encryptButton, backButton);
		buttonsHBox.setAlignment(Pos.CENTER);
		encryptionVBox.getChildren().addAll(gridPane, buttonsHBox);

		Label welcomeLabel = new Label("Welcome to Direct Encryption :)");
		welcomeLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(welcomeLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		BorderPane root = new BorderPane();
		root.setTop(titleBox);
		root.setCenter(encryptionVBox);
		root.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene encryptionScene = new Scene(root, 500, 500);
		setScene(encryptionScene);
	}
}
