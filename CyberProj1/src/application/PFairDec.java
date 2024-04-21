package application;

import java.io.File;
import java.io.FileNotFoundException;

import Nrmeen.PlayFairCipher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

// PFairDec class extending Stage for Play Fair Cipher decryption window
public class PFairDec extends Stage {
	private ExtensionFilter txtExF = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private TextArea textArea = new TextArea();

	// Constructor for PFairDec class
	public PFairDec(PlayFairCipher playFair, Stage stage) {
		setTitle("On File");
		textArea.setEditable(false); // TextArea for displaying decrypted text
		Label AL = new Label("A: ");
		TextField ATextField = new TextField();
		Label ZL = new Label("Z: ");
		TextField ZTextField = new TextField();
		Label lengthL = new Label("Length: ");
		TextField lengthTF = new TextField();

		HBox keyHB = new HBox(5); // HBox to hold key-related components
		keyHB.getChildren().addAll(AL, ATextField, ZL, ZTextField, lengthL, lengthTF);

		Button decryptionBt = new Button("Decrypt"); // Button for decryption
		decryptionBt.setOnAction(e -> {
			if (ATextField.getText().trim().isEmpty() || ZTextField.getText().trim().isEmpty()
					|| lengthTF.getText().trim().isEmpty()) {
				// Alert if any field is empty
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill in all fields.");
				emptyFieldAlert.showAndWait();
			} else {
				try {
					String cipherTxt = playFair.read().toString().toLowerCase(); // Read cipher text
					double A = Double.valueOf(ATextField.getText().trim());
					double Z = Double.valueOf(ZTextField.getText().trim());
					int length = Integer.valueOf(lengthTF.getText().trim());
					if (A >= 0 && A <= 1 && Z >= 0 && Z <= 4) {
						String key = playFair.Generation(A, Z, length); // Generate key
						playFair.setKey(key);
						playFair.setMatrixKey();
						playFair.setMatrix();
						String plainTxt = playFair.decryption(cipherTxt); // Decrypt text
						textArea.setText(plainTxt); // Display decrypted text
						FileChooser fileChooser = new FileChooser(); // Open file chooser
						fileChooser.setTitle("Choose a file");
						File selectedFile = fileChooser.showOpenDialog(this); // Show dialog
						playFair.setWrittingFile(selectedFile); // Set file for writing
						playFair.write(plainTxt); // Write decrypted text to file
					} else {
						// Alert if values are out of range
						Alert valuesError = new Alert(AlertType.ERROR);
						valuesError.setTitle("Values are Out of Range");
						valuesError.setHeaderText(null);
						valuesError.setContentText(
								"Please choose the values to be in the correct range:\n0 <= A <= 1 and 0 <= Z <= 4");
						valuesError.showAndWait();
					}
				} catch (NumberFormatException ex) {
					// Alert if number format error occurs
					Alert numberFormatError = new Alert(AlertType.ERROR);
					numberFormatError.setTitle("Number Format Error");
					numberFormatError.setHeaderText(null);
					numberFormatError.setContentText("The fields A and Z must be numbers and in the valid range.");
					numberFormatError.showAndWait();
				} catch (FileNotFoundException e1) {
					// Alert if file not found error occurs
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File Not Found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("File was not found.");
					fileNotFoundError.showAndWait();
				} catch (NullPointerException ex) {
					// Alert if no file selected error occurs
					Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
					noFileWasSelectedError.setTitle("File Not Selected");
					noFileWasSelectedError.setHeaderText(null);
					noFileWasSelectedError.setContentText("You did not select any file.");
					noFileWasSelectedError.showAndWait();
				}
			}
		});

		Button selectBt = new Button("Select File");
		selectBt.setOnAction(e -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file");
				File selectedFile = fileChooser.showOpenDialog(this);
				playFair.setReadingFile(selectedFile);
			} catch (NullPointerException ex) {
				// Alert if no file selected error occurs
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("File Not Selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("You did not select any file.");
				noFileWasSelectedError.showAndWait();
			}
		});

		Button backBt = new Button("Back"); // Button for going back
		backBt.setOnAction(e -> {
			close(); // Close current window
			stage.show(); // Show main stage
		});

		Label chooseAlgorithmLabel = new Label("Welcome to one time pad Decryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		HBox buttonsHB = new HBox();
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(selectBt, decryptionBt, backBt); // Add buttons to HBox

		VBox withKeyVB = new VBox(5); // VBox to hold components related to key
		withKeyVB.getChildren().addAll(keyHB, buttonsHB); // Add key components and buttons to VBox

		// BorderPane to hold TextArea and VBox
		BorderPane mainBp = new BorderPane();
		mainBp.setCenter(textArea); // TextArea in center
		mainBp.setBottom(withKeyVB); // VBox at bottom
		mainBp.setPadding(new Insets(10, 10, 10, 10)); // Set padding
		mainBp.setBackground(new Background(new BackgroundFill(Color.PINK, null, null))); // Set background color

		// Scene setup
		Scene fileScene = new Scene(mainBp, 700, 300); // Adjusted height
		setScene(fileScene);
	}
}
