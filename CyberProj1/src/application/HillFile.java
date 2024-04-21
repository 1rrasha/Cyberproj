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

// HillFile class extending Stage for file encryption
public class HillFile extends Stage {
	private ExtensionFilter textExtensionFilter = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private TextArea cipherTextArea = new TextArea();
	private TextField seedTextField = new TextField();
	private File seedsFile = new File("seeds.txt");
	private boolean isDuplicateSeed = false;
	private HillCipher hillCipher = new HillCipher();

	// Constructor for HillFile
	public HillFile(Stage stage) {
		setTitle("File Encryption");
		cipherTextArea.setEditable(false);
		cipherTextArea.setPrefHeight(200); // Set the preferred height of the text area
		Label seedLabel = new Label("Seed: ");
		HBox seedHBox = new HBox(5);
		seedHBox.getChildren().addAll(seedLabel, seedTextField);
		VBox topVBox = new VBox(5); // VBox to hold the label and seed input
		topVBox.getChildren().addAll(seedHBox, cipherTextArea);

		// Encrypt Button
		Button encryptButton = new Button("Encrypt");
		encryptButton.setStyle(encryptButton.getStyle() + "-fx-font-weight: bold;");
		encryptButton.setOnAction(e -> {
			// Check for empty fields
			if (seedTextField.getText().trim().isEmpty()) {
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Field");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill in the seed field.");
				emptyFieldAlert.showAndWait();
			} else {
				String seed = seedTextField.getText().trim();
				try (Scanner scanner = new Scanner(seedsFile);
						PrintWriter writer = new PrintWriter(new FileOutputStream(seedsFile, true))) {
					// Check if the seed was used before
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.equalsIgnoreCase(seed)) {
							isDuplicateSeed = true;
							break;
						}
					}
					if (isDuplicateSeed) {
						Alert duplicateSeedAlert = new Alert(AlertType.INFORMATION);
						duplicateSeedAlert.setTitle("Duplicate Seed");
						duplicateSeedAlert.setHeaderText(null);
						duplicateSeedAlert.setContentText("This seed has been used before.");
						duplicateSeedAlert.showAndWait();
					} else {
						// Write the new seed to the file
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
						String plainText = hillCipher.readf().toString();
						hillCipher.setText(plainText);
						String cipherText = hillCipher.encryption(key, hillCipher.getText());
						// Put cipher text in the text area
						cipherTextArea.setText(cipherText);
						// Save cipher text to the file
						hillCipher.setWF(seedsFile);
						hillCipher.writef(cipherText);
					}
				} catch (FileNotFoundException ex) {
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File Not Found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("The seeds file is not found.");
					fileNotFoundError.showAndWait();
				} catch (NullPointerException ex) {
					Alert noFileSelectedError = new Alert(AlertType.ERROR);
					noFileSelectedError.setTitle("File Not Selected");
					noFileSelectedError.setHeaderText(null);
					noFileSelectedError.setContentText("No file selected.");
					noFileSelectedError.showAndWait();
				} catch (MException ex) {
					Alert matrixError = new Alert(AlertType.ERROR);
					matrixError.setTitle("Square Matrix");
					matrixError.setHeaderText(null);
					matrixError.setContentText("The matrix is not square.");
					matrixError.showAndWait();
				}
			}
		});

		// Select File Button
		Button selectFileButton = new Button("Select File");
		selectFileButton.setStyle(selectFileButton.getStyle() + "-fx-font-weight: bold;");
		selectFileButton.setOnAction(e -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file");
				fileChooser.getExtensionFilters().add(textExtensionFilter);
				File selectedFile = fileChooser.showOpenDialog(this);
				hillCipher.setRF(selectedFile);
			} catch (NullPointerException ex) {
				Alert noFileSelectedError = new Alert(AlertType.ERROR);
				noFileSelectedError.setTitle("File Not Selected");
				noFileSelectedError.setHeaderText(null);
				noFileSelectedError.setContentText("No file selected.");
				noFileSelectedError.showAndWait();
			}
		});

		// Back Button
		Button backButton = new Button("Back");
		backButton.setStyle(backButton.getStyle() + "-fx-font-weight: bold;");
		backButton.setOnAction(e -> {
			close();
			stage.show();
		});

		HBox buttonsHBox = new HBox();
		buttonsHBox.setAlignment(Pos.CENTER);
		buttonsHBox.getChildren().addAll(selectFileButton, encryptButton, backButton);

		// Title Label
		Label chooseAlgorithmLabel = new Label("Welcome to Hill file Encryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// Main VBox
		VBox mainVBox = new VBox(10); // VBox to hold all the components
		mainVBox.getChildren().addAll(titleBox, topVBox, buttonsHBox);
		mainVBox.setPadding(new Insets(10));
		mainVBox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Scene setup
		Scene fileScene = new Scene(mainVBox, 700, 300); // Adjusted height
		setScene(fileScene);
	}
}
