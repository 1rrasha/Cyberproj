package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

// OneTDec class extending Stage for file decryption
public class OneTDec extends Stage {
	private ExtensionFilter txtExF = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private TextArea textArea = new TextArea();
	private TextField ATextField = new TextField();
	private TextField ZTextField = new TextField();
	private File file;

	// Constructor for OneTDec
	public OneTDec(OneTimePad oneTimePad, Stage stage) {
		setTitle("File Decryption");
		textArea.setEditable(false);
		textArea.setPrefHeight(200); // Set the preferred height of the text area
		Label AL = new Label("A: ");
		Label ZL = new Label("Z: ");

		HBox keyHB = new HBox(5);
		keyHB.getChildren().addAll(AL, ATextField, ZL, ZTextField);

		// Decrypt Button
		Button decryptionBt = new Button("Decrypt");
		decryptionBt.setOnAction(e -> {
			if (ATextField.getText().trim().isEmpty() || ZTextField.getText().trim().isEmpty()) {
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill the Fields");
				emptyFieldAlert.showAndWait();
			} else {
				if (file == null) {
					Alert noFileError = new Alert(AlertType.ERROR);
					noFileError.setTitle("File Not Chosen");
					noFileError.setHeaderText(null);
					noFileError.setContentText("You did not select any file");
					noFileError.showAndWait();
				} else {
					try {
						String binaryCipherTxt = oneTimePad.readDecreption();
						double A = Double.valueOf(ATextField.getText().trim());
						double Z = Double.valueOf(ZTextField.getText().trim());
						int length = binaryCipherTxt.length();

						if (A >= 0 && A <= 1 && Z >= 0 && Z <= 4) {
							String key = oneTimePad.keyGeneration(A, Z, length);
							oneTimePad.setKey(key);
							String plainTxt = oneTimePad.decryption(binaryCipherTxt, key);
							textArea.setText(plainTxt);

							FileChooser fileChooser = new FileChooser();
							fileChooser.setTitle("Choose a file");
							fileChooser.getExtensionFilters().add(txtExF);
							File selectedFile = fileChooser.showOpenDialog(this);
							oneTimePad.setWrittingFile(selectedFile);
							oneTimePad.writeDecryption(plainTxt);
						} else {
							Alert valuesError = new Alert(AlertType.ERROR);
							valuesError.setTitle("Values are Out of Range");
							valuesError.setHeaderText(null);
							valuesError.setContentText(
									"Please choose the values to be in the correct range:\n0 <= A <= 1 and 0 <= Z <= 4");
							valuesError.showAndWait();
						}

					} catch (FileNotFoundException e1) {
						Alert fileNotFoundError = new Alert(AlertType.ERROR);
						fileNotFoundError.setTitle("File Not Found");
						fileNotFoundError.setHeaderText(null);
						fileNotFoundError.setContentText("File was not found");
						fileNotFoundError.showAndWait();
					} catch (IOException e1) {
						Alert ioError = new Alert(AlertType.ERROR);
						ioError.setTitle("IO Exception");
						ioError.setHeaderText(null);
						ioError.setContentText("IO Exception occurred");
						ioError.showAndWait();
					} catch (NullPointerException ex) {
						Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
						noFileWasSelectedError.setTitle("File Not Selected");
						noFileWasSelectedError.setHeaderText(null);
						noFileWasSelectedError.setContentText("You did not select any file");
						noFileWasSelectedError.showAndWait();
					} catch (NumberFormatException ex) {
						Alert numberFormatError = new Alert(AlertType.ERROR);
						numberFormatError.setTitle("Number Format Error");
						numberFormatError.setHeaderText(null);
						numberFormatError.setContentText("The fields A and Z must be numbers and in the valid range");
						numberFormatError.showAndWait();
					}
				}
			}
		});

		// Select File Button
		Button selectBt = new Button("Select File");
		selectBt.setOnAction(e -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file");
				fileChooser.getExtensionFilters().add(txtExF);
				File selectedFile = fileChooser.showOpenDialog(this);
				file = selectedFile;
				oneTimePad.setReadingFile(file);
			} catch (NullPointerException ex) {
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("File Not Selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("You did not select any file");
				noFileWasSelectedError.showAndWait();
			}
		});

		// Back Button
		Button backBt = new Button("Back");
		backBt.setOnAction(e -> {
			close();
			stage.show();
		});

		// Title Label
		Label chooseAlgorithmLabel = new Label("Welcome to one time pad Decryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		HBox buttonsHB = new HBox();
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(selectBt, decryptionBt, backBt);

		VBox withKeyVB = new VBox(5);
		withKeyVB.getChildren().addAll(keyHB, buttonsHB);

		// Main BorderPane
		BorderPane mainBp = new BorderPane();
		mainBp.setCenter(textArea);
		mainBp.setBottom(withKeyVB);
		mainBp.setPadding(new Insets(10, 10, 10, 10));
		mainBp.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Scene setup
		Scene fileScene = new Scene(mainBp, 700, 300);
		setScene(fileScene);
	}
}
