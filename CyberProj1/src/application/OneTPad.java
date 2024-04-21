package application;

import Nrmeen.OneTimePad;
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

// OneTPad class extending Stage for main window of the application
public class OneTPad extends Stage {
	private OneTimePad oneTimePad = new OneTimePad();

	// Constructor for OneTPad
	public OneTPad(Stage mainStage) {

		Button encBtn = new Button("Encryption");
		encBtn.setStyle(encBtn.getStyle() + "-fx-font-weight: bold;");

		// Action event for Encryption button
		encBtn.setOnAction(e -> {
			// Open encryption window
			OneTEec encryption = new OneTEec(oneTimePad, this);
			encryption.show();
		});

		Button decBtn = new Button("Decryption");
		decBtn.setStyle(decBtn.getStyle() + "-fx-font-weight: bold;");

		// Action event for Decryption button
		decBtn.setOnAction(e -> {
			// Open decryption window
			OneTDec decryption = new OneTDec(oneTimePad, mainStage);
			decryption.show();
		});

		Button backBtn = new Button("Back");
		backBtn.setStyle(backBtn.getStyle() + "-fx-font-weight: bold;");

		// Action event for Back button
		backBtn.setOnAction(e -> {
			// Close current window and show main stage
			close();
			mainStage.show();
		});
		// Title Label
		Label chooseAlgorithmLabel = new Label("Welcome to One Time Pad :)");
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

		// BorderPane to hold the main content
		BorderPane operationsBP = new BorderPane();
		operationsBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
		operationsBP.setCenter(buttonsVBox);

		// Scene setup
		Scene scene = new Scene(operationsBP, 500, 400);
		setScene(scene);
	}
}
