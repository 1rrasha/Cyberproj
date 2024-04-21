package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// HillCipher class extending Stage for the hill cipher operations
public class HillCiph extends Stage {
	// Constructor for HillCipher
	public HillCiph(Stage mainStage) {
		// Set the title of the stage
		setTitle("Hill Cipher");

		// Button for encryption
		Button encBtn = new Button("Encryption");
		encBtn.setStyle(encBtn.getStyle() + "-fx-font-weight: bold;");
		encBtn.setOnAction(e -> {
			// Show HillEnc stage for encryption
			HillEnc encryptionStage = new HillEnc(this);
			encryptionStage.show();
			close();
		});

		// Button for decryption
		Button decBtn = new Button("Decryption");
		decBtn.setStyle(decBtn.getStyle() + "-fx-font-weight: bold;");
		decBtn.setOnAction(e -> {
			// Show HillCipherDec stage for decryption
			HillCipherDec decryption = new HillCipherDec(mainStage);
			decryption.show();
			close();
		});

		// Button for going back to main stage
		Button backBtn = new Button("Back");
		backBtn.setStyle(backBtn.getStyle() + "-fx-font-weight: bold;");
		backBtn.setOnAction(e -> {
			// Close current stage and show main stage
			close();
			mainStage.show();
		});

		// Label for title
		Label chooseAlgorithmLabel = new Label("Welcome to hill cipher :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// VBox to hold buttons
		VBox buttonsVBox = new VBox(10);
		buttonsVBox.getChildren().addAll(titleBox, encBtn, decBtn, backBtn);
		buttonsVBox.setSpacing(20);
		buttonsVBox.setAlignment(javafx.geometry.Pos.CENTER);

		// BorderPane to hold VBox
		BorderPane operationsBP = new BorderPane();
		operationsBP.setCenter(buttonsVBox);
		operationsBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Create scene with BorderPane
		Scene scene = new Scene(operationsBP, 500, 400);
		setScene(scene);
	}
}
