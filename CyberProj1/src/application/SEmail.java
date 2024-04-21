package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.util.Properties;

//SEmail class represents a JavaFX application for sending emails with attachments using SMTP protocol.
public class SEmail extends Stage {
	private ExtensionFilter ext = new ExtensionFilter("Text Files (*.txt)", "*.txt");
	private String attachmentPath = null;

	public SEmail() {
		setTitle("Sending Emails");
		BorderPane emailBP = new BorderPane();
		Label labell = new Label("Welcome to sent email:)");
		labell.setStyle(
				"-fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

		HBox titleBox = new HBox(labell);
		titleBox.setAlignment(Pos.TOP_CENTER);
		titleBox.setBackground(new Background(
				new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));
		GridPane mailFieldsGP = new GridPane();
		mailFieldsGP.setPadding(new Insets(10, 10, 10, 10));
		mailFieldsGP.setVgap(5);
		mailFieldsGP.setHgap(5);

		TextField senderTF = new TextField();
		Label senderL = new Label("sender E-Mail: ");
		mailFieldsGP.add(senderL, 0, 0);
		mailFieldsGP.add(senderTF, 1, 0);

		TextField recipientTF = new TextField();
		Label recipientL = new Label("recipient E-Mail: ");
		mailFieldsGP.add(recipientL, 0, 1);
		mailFieldsGP.add(recipientTF, 1, 1);

		TextField subjectTF = new TextField();
		Label subjectL = new Label("Subject: ");
		mailFieldsGP.add(subjectL, 0, 2);
		mailFieldsGP.add(subjectTF, 1, 2);

		TextArea messageTF = new TextArea();
		Label messageL = new Label("seed: ");
		mailFieldsGP.add(messageL, 0, 3);
		mailFieldsGP.add(messageTF, 1, 3);

		Button sendButton = new Button("Send");
		mailFieldsGP.add(sendButton, 0, 4);
		GridPane.setColumnSpan(sendButton, 2);
		sendButton.setAlignment(Pos.CENTER_RIGHT);
		sendButton.setOnAction(e -> {
			sendEmail(senderTF.getText(), recipientTF.getText(), subjectTF.getText(), messageTF.getText());
		});

		Button chooseFileBT = new Button("choose file");
		mailFieldsGP.add(chooseFileBT, 0, 5);
		GridPane.setColumnSpan(chooseFileBT, 2);
		chooseFileBT.setAlignment(Pos.CENTER_RIGHT);
		chooseFileBT.setOnAction(e -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file");
				File selectedFile = fileChooser.showOpenDialog(this);
				if (selectedFile != null) {
					attachmentPath = selectedFile.getAbsolutePath();
				}

			} catch (NullPointerException ex) {
				Alert noFileWasSelectedError = new Alert(AlertType.ERROR);
				noFileWasSelectedError.setTitle("file not selected");
				noFileWasSelectedError.setHeaderText(null);
				noFileWasSelectedError.setContentText("you did not select any file");
				noFileWasSelectedError.showAndWait();
			}
		});

		mailFieldsGP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
		emailBP.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
		emailBP.setTop(titleBox);
		titleBox.setAlignment(Pos.CENTER);
		emailBP.setCenter(mailFieldsGP);
		mailFieldsGP.setAlignment(Pos.CENTER);
		emailBP.setAlignment(mailFieldsGP, Pos.CENTER);
		emailBP.setAlignment(titleBox, Pos.CENTER);
		emailBP.setPadding(new Insets(10, 10, 10, 10));
		setScene(new Scene(emailBP, 700, 500));
	}

	private void sendEmail(String from, String to, String subject, String text) {
		String host = "smtp.gmail.com";
		String username = "1220707@student.birzeit.edu";
		String password = "yjuh vmwg gxgx uhan";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(text);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (attachmentPath != null) {
				MimeBodyPart attachmentPart = new MimeBodyPart();
				byte[] fileBytes = readFileToByteArray(new File(attachmentPath));
				attachmentPart.setDataHandler(
						new DataHandler(new ByteArrayDataSource(fileBytes, "application/octet-stream")));
				attachmentPart.setFileName(new File(attachmentPath).getName());
				multipart.addBodyPart(attachmentPart);
			}

			message.setContent(multipart);

			Transport.send(message);
			System.out.println("Email sent successfully");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] readFileToByteArray(File file) {
		try (InputStream is = new FileInputStream(file)) {
			byte[] buffer = new byte[(int) file.length()];
			int bytesRead = is.read(buffer);
			if (bytesRead < buffer.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			return buffer;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
