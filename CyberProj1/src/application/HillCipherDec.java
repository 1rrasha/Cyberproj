package application;

import java.io.File;

import java.io.FileNotFoundException;
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

// HillCipherDec class extending Stage for the hill cipher decryption
public class HillCipherDec extends Stage {
	private ExtensionFilter txtExF = new ExtensionFilter("Text Files (*.txt)", "*.txt"); // extensions for files
	private TextArea textArea = new TextArea();
	private TextField seedTF = new TextField();
	private HillCipher hillCipher = new HillCipher();

	// Constructor for HillCipherDec
	public HillCipherDec(Stage stage) {
		setTitle("File Decryption");
		textArea.setEditable(false);
		textArea.setPrefHeight(200); // Set the preferred height of the text area

		Label seedL = new Label("Seed: ");
		HBox keyHB = new HBox(5);
		keyHB.getChildren().addAll(seedL, seedTF);

		Button decryptionBt = new Button("Decrypt");
		decryptionBt.setOnAction(e -> {
			// check for empty fields
			if (seedTF.getText().trim().isEmpty()) {
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill the Fields");
				emptyFieldAlert.showAndWait();
			} else {
				// validate the input
				String seed = seedTF.getText().trim();
				try {
					// generate a key with inverse
					char i = 'a';
					int[][] key = new int[3][3];

					do {
						seed += i;
						key = hillCipher.Generation(seed);
						hillCipher.setKey(key);
						i++;
					} while (hillCipher.inverseOfDeterminant() == 0);
					// decryption
					String cipherText = hillCipher.readf().toString();
					String plainText = hillCipher.decryption(cipherText, key);
					// put cipher text in the text field
					textArea.setText(plainText);
					// open a file chooser
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Choose a file");
					fileChooser.getExtensionFilters().add(txtExF);
					File selectedFile = fileChooser.showOpenDialog(this);
					// save cipher text to the file
					hillCipher.setWF(selectedFile);
					hillCipher.writef(plainText);
				} catch (FileNotFoundException e1) {
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File Not Found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("The file is not found");
					fileNotFoundError.showAndWait();
				} catch (NullPointerException ex) {
					Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
					noFileWasSelectedError.setTitle("File Not Selected");
					noFileWasSelectedError.setHeaderText(null);
					noFileWasSelectedError.setContentText("No file selected");
					noFileWasSelectedError.showAndWait();
				} catch (MException e1) {
					Alert matrixError = new Alert(AlertType.ERROR);
					matrixError.setTitle("Square Matrix");
					matrixError.setHeaderText(null);
					matrixError.setContentText("The matrix is not square");
					matrixError.showAndWait();
				}
			}

		});

		Button selectBt = new Button("Select File");
		selectBt.setOnAction(e -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file");
				fileChooser.getExtensionFilters().add(txtExF);
				File selectedFile = fileChooser.showOpenDialog(this);
				hillCipher.setRF(selectedFile);
			} catch (NullPointerException ex) {
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("File Not Selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("No file selected");
				noFileWasSelectedError.showAndWait();
			}
		});

		Button backBt = new Button("Back");
		backBt.setOnAction(e -> {
			close();
			stage.show();
		});

		HBox buttonsHB = new HBox(10);
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(selectBt, decryptionBt, backBt);
		Label chooseAlgorithmLabel = new Label("Welcome to Hill  Decryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		VBox withKeyVB = new VBox(10);
		withKeyVB.getChildren().addAll(keyHB, buttonsHB);

		BorderPane mainBp = new BorderPane();
		mainBp.setTop(titleBox);
		mainBp.setCenter(textArea);
		mainBp.setBottom(withKeyVB);
		mainBp.setPadding(new Insets(10));
		mainBp.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		Scene fileScene = new Scene(mainBp, 700, 300);

		setScene(fileScene);
	}
}
