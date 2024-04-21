package application;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// Main class extending Application for JavaFX application
public class Main extends Application {
	@Override
	public void start(Stage stage) {
		// Set the title of the stage
		stage.setTitle("My project");

		// Creating buttons for different encryption algorithms
		Button hillCipherBtn = new Button("Hill Cipher");
		hillCipherBtn.setStyle(hillCipherBtn.getStyle() + "-fx-font-weight: bold;");
		hillCipherBtn.setOnAction(e -> {
			// Show HillCipherStage when clicked
			HillCiph hillStage = new HillCiph(stage);
			hillStage.show();
		});

		Button playFairBtn = new Button("Play Fair Cipher");
		playFairBtn.setStyle(playFairBtn.getStyle() + "-fx-font-weight: bold;");
		playFairBtn.setOnAction(e -> {
			// Show PlayFairStage when clicked
			PFair playStage = new PFair(stage);
			playStage.show();
		});

		Button oneTimePadBtn = new Button("One Time Pad Cipher");
		oneTimePadBtn.setStyle(oneTimePadBtn.getStyle() + "-fx-font-weight: bold;");
		oneTimePadBtn.setOnAction(e -> {
			// Show OneTimePadStage when clicked
			OneTPad oneTimeStage = new OneTPad(stage);
			oneTimeStage.show();
		});

		Button sendMailBtn = new Button("Send Email");
		sendMailBtn.setStyle(sendMailBtn.getStyle() + "-fx-font-weight: bold;");
		sendMailBtn.setOnAction(e -> {
			// Show SendingEmailStage when clicked
			SEmail emailStage = new SEmail();
			emailStage.show();
		});

		// Label for user instruction
		Label chooseAlgorithmLabel = new Label("Please Choose Encryption Algorithm :");
		chooseAlgorithmLabel.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

		// HBox for title label
		HBox titleBox = new HBox(chooseAlgorithmLabel);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		// VBox to hold buttons
		VBox buttonsVBox = new VBox(10);
		buttonsVBox.getChildren().addAll(titleBox, hillCipherBtn, sendMailBtn, playFairBtn, oneTimePadBtn);
		buttonsVBox.setSpacing(20);
		buttonsVBox.setAlignment(javafx.geometry.Pos.CENTER);

		// BorderPane to hold VBox
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(buttonsVBox);
		borderPane.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
		Image icon = new Image(
				new File("cyber.png").toURI().toString());

		// Set the icon for the stage
		stage.getIcons().add(icon);

		// Create scene with BorderPane
		Scene scene = new Scene(borderPane, 500, 400);
		stage.setScene(scene);
		stage.show();
	}

	// Main method to launch JavaFX application
	public static void main(String[] args) {
		launch(args);
	}
}
