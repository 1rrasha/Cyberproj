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

// PFairFile class extending Stage for Play Fair File Encryption window
public class PFairFile extends Stage {
	private TextArea textArea = new TextArea();
	private boolean flag = false; // to indicate a duplicated seed
	private File file = new File("seeds.txt");

	// Constructor for PFairFile class
	public PFairFile(PlayFairCipher playFair, Stage stage) {
		setTitle("On File");
		textArea.setEditable(false);
		Label AL = new Label("A: ");
		TextField ATextField = new TextField();
		Label ZL = new Label("Z: ");
		TextField ZTextField = new TextField();
		Label lengthL = new Label("Length: ");
		TextField lengthTF = new TextField();

		HBox keyHB = new HBox(5);
		keyHB.getChildren().addAll(AL, ATextField, ZL, ZTextField, lengthL, lengthTF);
		Button encryptionBt = new Button("Encrypt");

		// Action when encryption button is clicked
		encryptionBt.setOnAction(e -> {
			if (ATextField.getText().trim().isEmpty() || ZTextField.getText().trim().isEmpty()
					|| lengthTF.getText().trim().isEmpty()) {
				// Show alert if any field is empty
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill the Fields");
				emptyFieldAlert.showAndWait();
			} else {
				try (Scanner reader = new Scanner(file);
						PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
					// get the message
					String plainTxt = playFair.read().toString().toLowerCase();
					playFair.setText(plainTxt);
					// generate the key
					double A = Double.valueOf(ATextField.getText().trim());
					double Z = Double.valueOf(ZTextField.getText().trim());
					int length = Integer.valueOf(lengthTF.getText().trim());
					String seed = A + "," + Z + "," + length;
					// check for redundant seed
					while (reader.hasNextLine()) {
						String line = reader.nextLine();
						if (line.equalsIgnoreCase(seed)) {
							flag = true; // the seed was used before
						}
					}
					if (flag) {
						// Show alert if seed is redundant
						Alert redundantSeedAlert = new Alert(AlertType.INFORMATION);
						redundantSeedAlert.setTitle("Redundant Seed");
						redundantSeedAlert.setHeaderText(null);
						redundantSeedAlert.setContentText("This seed was used before");
						redundantSeedAlert.showAndWait();
					} else {
						if (A >= 0 && A <= 1 && Z >= 0 && Z <= 4) {
							String key = playFair.Generation(A, Z, length);
							writer.println(seed);
							// encrypt the text
							playFair.setKey(key);
							playFair.setMatrixKey();
							playFair.setMatrix();
							String cipherTxt = playFair.encryption(playFair.getText());
							// put the result in the text field
							textArea.setText(cipherTxt);
							// open a file chooser
							FileChooser fileChooser = new FileChooser();
							fileChooser.setTitle("Choose a file");
							File selectedFile = fileChooser.showOpenDialog(this);
							// save cipher text to the file
							playFair.setWrittingFile(selectedFile);
							playFair.write(cipherTxt);

						} else {
							// Show alert if values are not in range
							Alert valuesError = new Alert(AlertType.ERROR);
							valuesError.setTitle("Values are not in the range");
							valuesError.setHeaderText(null);
							valuesError.setContentText(
									"Please choose the values to be in the correct range. \n 0<=A<=1 and 0<=Z<=4");
							valuesError.showAndWait();
						}
					}

				} catch (NumberFormatException ex) {
					// Show alert if number format error occurs
					Alert numberFormatError = new Alert(AlertType.ERROR);
					numberFormatError.setTitle("Number format");
					numberFormatError.setHeaderText(null);
					numberFormatError.setContentText("The fields A and Z must be numbers and in the valid range");
					numberFormatError.showAndWait();
				} catch (FileNotFoundException e1) {
					// Show alert if file not found error occurs
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File not found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("File is not found");
					fileNotFoundError.showAndWait();
				} catch (NullPointerException ex) {
					// Show alert if no file selected error occurs
					Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
					noFileWasSelectedError.setTitle("File not selected");
					noFileWasSelectedError.setHeaderText(null);
					noFileWasSelectedError.setContentText("You did not select any file");
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
				// Show alert if no file selected error occurs
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("File not selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("You did not select any file");
				noFileWasSelectedError.showAndWait();
			}
		});

		Button backBt = new Button("Back");
		backBt.setOnAction(e -> {
			close(); // Close current window
			stage.show(); // Show previous stage
		});

		HBox buttonsHB = new HBox();
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(selectBt, encryptionBt, backBt);

		VBox withKeyVB = new VBox(5);
		withKeyVB.getChildren().addAll(keyHB, buttonsHB);

		// Label for welcome message
		Label chooseAlgorithmLabel = new Label("Welcome to Play Fair File Encryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		VBox mainVBox = new VBox(10); // VBox to hold all the components
		mainVBox.getChildren().addAll(titleBox, textArea, withKeyVB);
		mainVBox.setPadding(new Insets(10));
		mainVBox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		Scene fileScene = new Scene(mainVBox, 700, 300); // Adjusted height

		setScene(fileScene);
	}
}
