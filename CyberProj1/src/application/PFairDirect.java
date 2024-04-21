package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import Nrmeen.PlayFairCipher;
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

// PFairDirect class extending Stage for direct encryption window
public class PFairDirect extends Stage {

	private Label plainTxtLabel = new Label("Plain Text: ");
	private TextField plainTxtTF = new TextField();
	private ExtensionFilter txtExF = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private Label xLabel = new Label("A: ");
	private TextField xTextField = new TextField();
	private Label rLabel = new Label("Z: ");
	private TextField rTextField = new TextField();
	private Label lengthLabel = new Label("Length: ");
	private TextField lengthTF = new TextField();
	private File file = new File("seeds.txt");
	private Label cipherTxtLabel = new Label("Cipher Text: ");
	private TextField cipherTxtTF = new TextField();
	private GridPane encGP = new GridPane();
	private boolean flag = false; // to indicate a duplicated seed

	// Constructor for PFairDirect class
	public PFairDirect(PlayFairCipher playFair, Stage mainStage) {
		setTitle("Encryption");
		cipherTxtTF.setEditable(false);
		encGP.setVgap(10);
		encGP.setHgap(10);
		encGP.add(plainTxtLabel, 0, 0);
		encGP.add(plainTxtTF, 1, 0);
		encGP.add(xLabel, 0, 1);
		encGP.add(xTextField, 1, 1);
		encGP.add(rLabel, 2, 1);
		encGP.add(rTextField, 3, 1);
		encGP.add(lengthLabel, 4, 1);
		encGP.add(lengthTF, 5, 1);
		encGP.add(cipherTxtLabel, 0, 2);
		encGP.add(cipherTxtTF, 1, 2);
		encGP.setAlignment(Pos.CENTER);

		VBox encryptionVB = new VBox(10);
		encryptionVB.setAlignment(Pos.CENTER);
		Button encryptButton = new Button("Encrypt");
		encryptButton.setOnAction(e -> {
			if (plainTxtTF.getText().trim().isEmpty() || xTextField.getText().trim().isEmpty()
					|| rTextField.getText().trim().isEmpty() || lengthTF.getText().trim().isEmpty()) {
				// Alert if any field is empty
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill in all fields.");
				emptyFieldAlert.showAndWait();
			} else {
				try (Scanner reader = new Scanner(file);
						PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
					// Set the message
					playFair.setText(getFullScreenExitHint());
					double A = Double.valueOf(xTextField.getText().trim());
					double Z = Double.valueOf(rTextField.getText().trim());
					int length = Integer.valueOf(lengthTF.getText().trim());
					String seed = "A:" + A + ", Z:" + Z + ", Length:" + length;
					// Check for redundant seed
					while (reader.hasNextLine()) {
						String line = reader.nextLine();
						if (line.equalsIgnoreCase(seed)) {
							flag = true; // The seed was used before
						}
					}
					if (flag) {
						// Alert if redundant seed found
						Alert redundantSeedAlert = new Alert(AlertType.INFORMATION);
						redundantSeedAlert.setTitle("Redundant Seed");
						redundantSeedAlert.setHeaderText(null);
						redundantSeedAlert.setContentText("This seed was used before.");
						redundantSeedAlert.showAndWait();
					} else {
						if (A >= 0 && A <= 1 && Z >= 0 && Z <= 4) {
							writer.println(seed);
							String key = playFair.Generation(A, Z, length); // Generate key
							playFair.setKey(key);
							String cipherTxt = playFair.encryption(playFair.getText()); // Encrypt text
							cipherTxtTF.setText(cipherTxt); // Display encrypted text
							FileChooser fileChooser = new FileChooser(); // Open file chooser
							fileChooser.setTitle("Choose a file");
							fileChooser.getExtensionFilters().add(txtExF);
							File selectedFile = fileChooser.showOpenDialog(this);
							playFair.setWrittingFile(selectedFile); // Set file for writing
							playFair.write(cipherTxt); // Write encrypted text to file
						} else {
							// Alert if values are not in range
							Alert valuesError = new Alert(AlertType.ERROR);
							valuesError.setTitle("Values are Not in Range");
							valuesError.setHeaderText(null);
							valuesError.setContentText(
									"Please choose the values to be in the correct range. \n 0 <= A <= 1 and 0 <= Z <= 4");
							valuesError.showAndWait();
						}
					}
				} catch (NumberFormatException ex) {
					// Alert if number format error occurs
					Alert numberFormatError = new Alert(AlertType.ERROR);
					numberFormatError.setTitle("Number Format");
					numberFormatError.setHeaderText(null);
					numberFormatError.setContentText("The fields A, Z, Length must be numbers and in the valid range.");
					numberFormatError.showAndWait();
				} catch (FileNotFoundException e1) {
					// Alert if file not found error occurs
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File Not Found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("File is not found.");
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

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			close(); // Close current window
			mainStage.show(); // Show main stage
		});

		Label welcomeLabel = new Label("Welcome to Direct Encryption :)");
		welcomeLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(welcomeLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		HBox buttonsHB = new HBox(10);
		buttonsHB.getChildren().addAll(encryptButton, backButton);
		buttonsHB.setAlignment(Pos.CENTER);
		encryptionVB.getChildren().addAll(titleBox, encGP, buttonsHB);
		encryptionVB.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
		Scene encryptionScene = new Scene(encryptionVB, 700, 300);
		setScene(encryptionScene);
	}

}
