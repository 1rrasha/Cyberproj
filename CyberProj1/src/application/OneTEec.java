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

// OneTEec class extending Stage for encryption options
public class OneTEec extends Stage {

	// Constructor for OneTEec
	public OneTEec(OneTimePad oneTimePad, Stage stage) {
		setTitle("Encryption");

		// Direct Input Button
		Button dirBtn = new Button("Direct Input");
		dirBtn.setStyle(dirBtn.getStyle() + "-fx-font-weight: bold;");
		dirBtn.setOnAction(e -> {
			// Open direct input encryption window
			OneTDirect direct = new OneTDirect(oneTimePad, this);
			direct.show();
		});

		// File Input Button
		Button fileInBtn = new Button("File Input");
		fileInBtn.setStyle(fileInBtn.getStyle() + "-fx-font-weight: bold;");
		fileInBtn.setOnAction(e -> {
			// Open file input encryption window
			OneTFile file = new OneTFile(oneTimePad, this);
			file.show();
		});

		// Back Button
		Button backBtn = new Button("Back");
		backBtn.setStyle(backBtn.getStyle() + "-fx-font-weight: bold;");
		backBtn.setOnAction(e -> {
			// Return to the main menu
			close();
			stage.show();
		});

		// Title Label
		Label chooseAlgorithmLabel = new Label("Welcome to One Time Pad Encryption:)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// VBox to hold the buttons
		VBox buttonsVBox = new VBox(10);
		buttonsVBox.getChildren().addAll(titleBox, dirBtn, fileInBtn, backBtn);
		buttonsVBox.setSpacing(20);
		buttonsVBox.setAlignment(javafx.geometry.Pos.CENTER);

		// BorderPane to organize layout
		BorderPane operationsBP = new BorderPane();
		operationsBP.setCenter(buttonsVBox);
		operationsBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Scene setup
		Scene scene = new Scene(operationsBP, 500, 400);
		setScene(scene);
	}
}
