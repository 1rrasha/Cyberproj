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

// HillEnc class extending Stage for hill encryption
public class HillEnc extends Stage {

	// Constructor for HillEnc
	public HillEnc(Stage hillStage) {
		setTitle("Encryption");

		// Direct Input Button
		Button dirBtn = new Button("Direct Input");
		dirBtn.setStyle(dirBtn.getStyle() + "-fx-font-weight: bold;");
		dirBtn.setOnAction(e -> {
			HillDirectEnc direct = new HillDirectEnc(this);
			direct.show();
			close();
		});

		// File Input Button
		Button fileInBtn = new Button("File Input");
		fileInBtn.setStyle(fileInBtn.getStyle() + "-fx-font-weight: bold;");
		fileInBtn.setOnAction(e -> {
			HillFile file = new HillFile(this);
			file.show();
			close();
		});

		// Back Button
		Button backBtn = new Button("Back");
		backBtn.setStyle(backBtn.getStyle() + "-fx-font-weight: bold;");
		backBtn.setOnAction(e -> {
			close();
			hillStage.show();
		});

		// Title Label
		Label chooseAlgorithmLabel = new Label("Welcome to hill encryption :)");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// Buttons VBox
		VBox buttonsVBox = new VBox(10);
		buttonsVBox.getChildren().addAll(titleBox, dirBtn, fileInBtn, backBtn);
		buttonsVBox.setSpacing(20);
		buttonsVBox.setAlignment(javafx.geometry.Pos.CENTER);

		// BorderPane for layout
		BorderPane operationsBP = new BorderPane();
		operationsBP.setCenter(buttonsVBox);
		operationsBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));

		// Scene setup
		Scene scene = new Scene(operationsBP, 500, 400);
		setScene(scene);
	}
}
