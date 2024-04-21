package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import Nrmeen.OneTimePad;
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
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

//OneTDirect class extending Stage for direct encryption

public class OneTDirect extends Stage {

	private ExtensionFilter txtExtensionFilter = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private Label plainTextLabel = new Label("Plain Text: ");
	private TextField plainTextField = new TextField();
	private boolean isDuplicateSeed = false; // to indicate a duplicated seed
	private Label xLabel = new Label("A: ");
	private TextField xTextField = new TextField();
	private Label rLabel = new Label("Z: ");
	private TextField rTextField = new TextField();

	private Label cipherTextLabel = new Label("Cipher Text: ");
	private TextField cipherTextField = new TextField();
	private GridPane encryptionGridPane = new GridPane();
	private File seedsFile = new File("seeds.txt");

	public OneTDirect(OneTimePad oneTimePad, Stage stage) {

		setTitle("Encryption");
		encryptionGridPane.setVgap(10);
		encryptionGridPane.setHgap(10);
		encryptionGridPane.add(plainTextLabel, 0, 0);
		encryptionGridPane.add(plainTextField, 1, 0);
		encryptionGridPane.add(xLabel, 0, 1);
		encryptionGridPane.add(xTextField, 1, 1);
		encryptionGridPane.add(rLabel, 2, 1);
		encryptionGridPane.add(rTextField, 3, 1);
		encryptionGridPane.add(cipherTextLabel, 0, 2);
		encryptionGridPane.add(cipherTextField, 1, 2);
		cipherTextField.setEditable(false);
		encryptionGridPane.setAlignment(Pos.CENTER);

		VBox encryptionVBox = new VBox(10);
		encryptionVBox.setAlignment(Pos.CENTER);
		Button encryptButton = new Button("Encrypt");
		encryptButton.setStyle("-fx-font-weight: bold;");

		encryptButton.setOnAction(e -> {
			// check for empty fields
			if (plainTextField.getText().trim().isEmpty() || xTextField.getText().trim().isEmpty()
					|| rTextField.getText().trim().isEmpty()) {
				Alert emptyFieldAlert = new Alert(AlertType.INFORMATION);
				emptyFieldAlert.setTitle("Empty Fields");
				emptyFieldAlert.setHeaderText(null);
				emptyFieldAlert.setContentText("Please fill in all fields.");
				emptyFieldAlert.showAndWait();
			} else {
				String withoutSpaces = oneTimePad.noSpaces(plainTextField.getText().trim());
				String seed = xTextField.getText().trim() + "," + rTextField.getText().trim() + ","
						+ withoutSpaces.length();
				try (Scanner reader = new Scanner(seedsFile);
						PrintWriter writer = new PrintWriter(new FileOutputStream(seedsFile, true))) {
					while (reader.hasNextLine()) {
						String line = reader.nextLine();
						if (line.equalsIgnoreCase(seed)) {
							isDuplicateSeed = true;
							break;
						}
					}
					if (isDuplicateSeed) {
						Alert redundantSeedAlert = new Alert(AlertType.INFORMATION);
						redundantSeedAlert.setTitle("Redundant Seed");
						redundantSeedAlert.setHeaderText(null);
						redundantSeedAlert.setContentText("This seed was used before.");
						redundantSeedAlert.showAndWait();
					} else {
						double A = Double.valueOf(xTextField.getText().trim());
						double Z = Double.valueOf(rTextField.getText().trim());
						int length = withoutSpaces.length();
						if (A >= 0 && A <= 1 && Z >= 0 && Z <= 4) {
							writer.println(seed);
							String key = oneTimePad.keyGeneration(A, Z, length);
							oneTimePad.setKey(key);
							oneTimePad.setText(withoutSpaces);
							String cipherText = oneTimePad.encryption(oneTimePad.getText(), key);
							cipherTextField.setText(oneTimePad.encodeToBase64(cipherText));
							FileChooser fileChooser = new FileChooser();
							fileChooser.setTitle("Choose a file");
							fileChooser.getExtensionFilters().add(txtExtensionFilter);
							File selectedFile = fileChooser.showOpenDialog(this);
							oneTimePad.setWrittingFile(selectedFile);
							oneTimePad.writeEncryption(cipherText);
						} else {
							Alert valuesError = new Alert(AlertType.ERROR);
							valuesError.setTitle("Values Out of Range");
							valuesError.setHeaderText(null);
							valuesError.setContentText(
									"Please choose the values to be in the correct range.\n0 <= A <= 1 and 0 <= Z <= 4");
							valuesError.showAndWait();
						}
					}
				} catch (FileNotFoundException ex) {
					Alert fileNotFoundError = new Alert(AlertType.ERROR);
					fileNotFoundError.setTitle("File Not Found");
					fileNotFoundError.setHeaderText(null);
					fileNotFoundError.setContentText("File is not found");
					fileNotFoundError.showAndWait();
				} catch (IOException ex) {
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
					numberFormatError.setContentText("The field x and r must be numbers and in the valid range");
					numberFormatError.showAndWait();
				}

			}
		});

		Button backButton = new Button("Back");
		backButton.setStyle("-fx-font-weight: bold;");
		backButton.setOnAction(e -> {
			close();
			stage.show();
		});
		HBox buttonsHBox = new HBox(10);

		Label welcomeLabel = new Label("Welcome to One Time Pad Direct Encryption:)");
		welcomeLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(welcomeLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));
		buttonsHBox.getChildren().addAll(encryptButton, backButton);
		buttonsHBox.setAlignment(Pos.CENTER);
		encryptionVBox.getChildren().addAll(titleBox, encryptionGridPane, buttonsHBox);
		encryptionVBox.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene encryptionScene = new Scene(encryptionVBox, 700, 400);
		setScene(encryptionScene);
	}
}
