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

// PFairEnc class extending Stage for Play Fair Encryption window
public class PFairEnc extends Stage {

	// Constructor for PFairEnc class
	public PFairEnc(PlayFairCipher playFair, Stage mainStage) {
		setTitle("Encryption");

		// Button for direct input encryption
		Button directInputBtn = new Button("Direct Input");
		directInputBtn.setStyle(directInputBtn.getStyle() + "-fx-font-weight: bold;");
		directInputBtn.setOnAction(e -> {
			PFairDirect direct = new PFairDirect(playFair, this); // Open direct input window
			direct.show();
			close(); // Close current window
		});

		// Button for file input encryption
		Button fileInputBtn = new Button("File Input");
		fileInputBtn.setStyle(fileInputBtn.getStyle() + "-fx-font-weight: bold;");
		fileInputBtn.setOnAction(e -> {
			PFairFile file = new PFairFile(playFair, this); // Open file input window
			file.show();
			close(); // Close current window
		});

		// Button to go back to the main stage
		Button backBtn = new Button("Back");
		backBtn.setStyle(backBtn.getStyle() + "-fx-font-weight: bold;");
		backBtn.setOnAction(e -> {
			close(); // Close current window
			mainStage.show(); // Show main stage
		});

		// Label for welcome message
		Label welcomeLabel = new Label("Welcome to Play Fair Encryption :)");
		welcomeLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(welcomeLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(
				new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// Vertical box to hold buttons
		VBox buttonsVBox = new VBox(10);
		buttonsVBox.getChildren().addAll(titleBox, directInputBtn, fileInputBtn, backBtn);
		buttonsVBox.setSpacing(20);
		buttonsVBox.setAlignment(Pos.CENTER);

		// Border pane for layout
		BorderPane operationsBP = new BorderPane();
		operationsBP.setCenter(buttonsVBox);
		operationsBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Set the scene
		Scene scene = new Scene(operationsBP, 500, 400);
		setScene(scene);
	}
}
