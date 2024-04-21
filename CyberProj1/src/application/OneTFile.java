package application;

import Nrmeen.OneTimePad;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

// OneTFile class extending Stage for file input encryption
public class OneTFile extends Stage {
	private ExtensionFilter txtExF = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private TextArea txtArea = new TextArea();
	private TextField ATextField = new TextField();
	private TextField ZTextField = new TextField();
	private boolean flag = false;
	private File file = new File("seeds.txt");

	// Constructor for OneTFile
	public OneTFile(OneTimePad oneTimePad, Stage stage) {
		setTitle("On File");
		txtArea.setEditable(false);
		Label ALabel = new Label("A: ");
		Label ZLabel = new Label("Z: ");

		HBox keyHB = new HBox(5);
		keyHB.getChildren().addAll(ALabel, ATextField, ZLabel, ZTextField);

		Button encryptionBt = new Button("Encrypt");
		encryptionBt.setOnAction(e -> {
			if (ATextField.getText().trim().isEmpty() || ATextField.getText().trim().isEmpty()) {
				// Alert if any fields are empty
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill in all fields.");
				emptyFieldAlert.showAndWait();
			} else {
				try (Scanner reader = new Scanner(file);
						PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
					String plainTxt = oneTimePad.readEncrypt().toString();
					String withoutSpaces = oneTimePad.noSpaces(plainTxt);

					double A = Double.valueOf(ATextField.getText().trim());
					double Z = Double.valueOf(ATextField.getText().trim());
					int length = withoutSpaces.length();
					String seed = A + "," + Z + "," + length;

					// Checking for duplicate seeds
					while (reader.hasNextLine()) {
						String line = reader.nextLine();
						if (line.equalsIgnoreCase(seed)) {
							flag = true;
						}
					}
					if (flag) {
						// Alert if duplicate seed found
						Alert redunduntSeedAlert = new Alert(AlertType.INFORMATION);
						redunduntSeedAlert.setTitle("Redundant Seed");
						redunduntSeedAlert.setHeaderText(null);
						redunduntSeedAlert.setContentText("This seed has been used before.");
						redunduntSeedAlert.showAndWait();
					} else {
						if (A >= 0 && A <= 1 && Z >= 0 && Z <= 4) {
							writer.println(seed);
							String key = oneTimePad.keyGeneration(A, Z, length);
							oneTimePad.setKey(key);
							oneTimePad.setText(withoutSpaces);

							// Encrypting the message
							String cipherTxt = oneTimePad.encryption(oneTimePad.getText(), key);
							txtArea.setText(oneTimePad.encodeToBase64(cipherTxt));

							// Choosing file to save the encrypted message
							FileChooser fileChooser = new FileChooser();
							fileChooser.setTitle("Choose a file");
							File selectedFile = fileChooser.showOpenDialog(this);

							oneTimePad.setWrittingFile(selectedFile);
							oneTimePad.writeEncryption(cipherTxt);
						} else {
							// Alert for values out of range
							Alert valuesError = new Alert(AlertType.ERROR);
							valuesError.setTitle("Values Out of Range");
							valuesError.setHeaderText(null);
							valuesError
									.setContentText("Please choose values in the correct range: 0<=A<=1 and 0<=Z<=4");
							valuesError.showAndWait();
						}
					}
				} catch (FileNotFoundException e1) {
					// Alert for file not found
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File Not Found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("The file is not found.");
					fileNotFoundError.showAndWait();
				} catch (IOException e1) {
					// Alert for IO exception
					Alert ioError = new Alert(AlertType.ERROR);
					ioError.setTitle("IO Exception");
					ioError.setHeaderText(null);
					ioError.setContentText("An IO exception occurred.");
					ioError.showAndWait();
				} catch (NullPointerException ex) {
					// Alert for no file selected
					Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
					noFileWasSelectedError.setTitle("File Not Selected");
					noFileWasSelectedError.setHeaderText(null);
					noFileWasSelectedError.setContentText("No file selected.");
					noFileWasSelectedError.showAndWait();
				} catch (NumberFormatException ex) {
					// Alert for number format error
					Alert numberFormatError = new Alert(AlertType.ERROR);
					numberFormatError.setTitle("Number Format Error");
					numberFormatError.setHeaderText(null);
					numberFormatError.setContentText("The fields A and Z must be numbers within the valid range.");
					numberFormatError.showAndWait();
				}
			}
		});

		Button selectBt = new Button("Select File");
		selectBt.setOnAction(e -> {
			try {
				// Choosing file for input
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file");
				File selectedFile = fileChooser.showOpenDialog(this);
				oneTimePad.setReadingFile(selectedFile);
			} catch (NullPointerException ex) {
				// Alert for no file selected
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("File Not Selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("No file selected.");
				noFileWasSelectedError.showAndWait();
			}
		});

		Button backBt = new Button("Back");
		backBt.setOnAction(e -> {
			// Go back to the main window
			close();
			stage.show();
		});

		HBox buttonsHB = new HBox();
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(selectBt, encryptionBt, backBt);

		VBox withKeyVB = new VBox(5);
		withKeyVB.getChildren().addAll(keyHB, buttonsHB);

		// Title Label
		Label chooseAlgorithmLabel = new Label("Welcome to One-Time Pad File Encryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// Main VBox to hold all components
		VBox mainVBox = new VBox(10);
		mainVBox.getChildren().addAll(titleBox, txtArea, withKeyVB);
		mainVBox.setPadding(new Insets(10));
		mainVBox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Scene setup
		Scene fileScene = new Scene(mainVBox, 700, 300);
		setScene(fileScene);
	}
}
