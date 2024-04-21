package application;

import Nrmeen.PlayFairCipher;
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

// PFair class extending Stage for the Play Fair Cipher main window
public class PFair extends Stage {
	private PlayFairCipher playFair = new PlayFairCipher();

	// Constructor for PFair class
	public PFair(Stage mainStage) {
		setTitle("Play Fair Cipher");

		// Button for encryption
		Button encryptionBtn = new Button("Encryption");
		encryptionBtn.setStyle(encryptionBtn.getStyle() + "-fx-font-weight: bold;");
		encryptionBtn.setOnAction(e -> {
			// Open encryption stage
			PFairEnc encryptionStage = new PFairEnc(playFair, this);
			encryptionStage.show();
			close(); // Close current window
		});

		// Button for decryption
		Button decryptionBtn = new Button("Decryption");
		decryptionBtn.setStyle(decryptionBtn.getStyle() + "-fx-font-weight: bold;");
		decryptionBtn.setOnAction(e -> {
			// Open decryption stage
			PFairDec decryptionStage = new PFairDec(playFair, mainStage);
			decryptionStage.show();
			close(); // Close current window
		});

		// Button for going back to the main stage
		Button backBtn = new Button("Back");
		backBtn.setStyle(backBtn.getStyle() + "-fx-font-weight: bold;");
		backBtn.setOnAction(e -> {
			close(); // Close current window
			mainStage.show(); // Show main stage
		});

		// Label for welcome message
		Label welcomeLabel = new Label("Welcome to Play Fair Cipher :)");
		welcomeLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(welcomeLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// VBox to hold buttons and welcome message
		VBox buttonsVBox = new VBox(10);
		buttonsVBox.getChildren().addAll(titleBox, encryptionBtn, decryptionBtn, backBtn);
		buttonsVBox.setSpacing(20);
		buttonsVBox.setAlignment(Pos.CENTER);

		// BorderPane to hold the main content
		BorderPane operationsBP = new BorderPane();
		operationsBP.setCenter(buttonsVBox);
		operationsBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Scene setup
		Scene scene = new Scene(operationsBP, 500, 400);
		setScene(scene);
	}
}
