module CyberProj1 {
	requires java.mail;
	requires activation;
	requires javafx.controls;
	requires javafx.graphics;

	opens application to javafx.graphics, javafx.fxml;
}
