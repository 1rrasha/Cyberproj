package Nrmeen;

import java.io.DataInputStream;
/*
 * The OneTimePad class implements the one-time pad encryption and decryption algorithm.
 * It provides methods for encrypting and decrypting messages using XOR operations with a key,
 * generating random keys, encoding and decoding to/from Base64, reading from and writing to files,
 * and removing spaces from messages. The class also includes methods for reading and writing
 * encrypted and decrypted messages to files using DataInputStream and DataOutputStream.
 */
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Scanner;

public class OneTimePad {

	private String Text;
	private String key;
	private char[] chArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private File rFile;
	private File wFile;

	public OneTimePad() {

	}

	public OneTimePad(String Text, String key) {
		this.Text = Text;
		this.key = key;
	}

	public String getText() {
		return Text;
	}

	public void setText(String Text) {
		String noSpaces = noSpaces(Text);
		this.Text = noSpaces;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public File getReadingFile() {
		return rFile;
	}

	public void setReadingFile(File readingFile) {
		this.rFile = readingFile;
	}

	public File getWrittingFile() {
		return wFile;
	}

	public void setWrittingFile(File writtingFile) {
		this.wFile = writtingFile;
	}

	// encryption
	public String encryption(String text, String key) {
		StringBuilder cipherText = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char encryptedChar = (char) (text.charAt(i) ^ key.charAt(i));

			cipherText.append(encryptedChar);
		}

		return cipherText.toString();
	}

	public String decryption(String cipherText, String key) {
		StringBuilder plainText = new StringBuilder();
		for (int i = 0; i < cipherText.length(); i++) {
			char decryptedChar = (char) (cipherText.charAt(i) ^ key.charAt(i));
			plainText.append(decryptedChar);
		}

		return plainText.toString();
	}

	public String encodeToBase64(String input) {
		return Base64.getEncoder().encodeToString(input.getBytes());
	}

	public String noSpaces(String withSpaces) {
		// constructor
		String noSpaces = "";
		for (int i = 0; i < withSpaces.length(); i++) {
			char ch = withSpaces.charAt(i);
			if (ch != ' ') {
				noSpaces += ch;
			}
		}
		return noSpaces;
	}

	private char toCharacter(int index) {
		return chArray[index];
	}

	public String keyGeneration(double A, double Z, int length) {
		String theKey = "";
		for (int i = 0; i < length; i++) {
			A = Z * A * (1 - A);
			int letterIndex = (int) (Math.round(A * Math.pow(10, 16)) % 26);
			char theLetter = toCharacter(letterIndex);
			theKey += theLetter;
		}
		return theKey;
	}

	// reading from file for encryption
	public StringBuilder readEncrypt() throws FileNotFoundException {

		StringBuilder text = new StringBuilder();
		try (Scanner read = new Scanner(rFile);) {
			while (read.hasNextLine()) {
				String line = read.nextLine();
				text.append(line);
			}
		}
		return text;
	}

	public void writeDecryption(String text) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(wFile);) {
			writer.print(text);
			writer.flush();
		}
	}

	public String readDecreption() throws IOException {
		StringBuilder binary = new StringBuilder();
		try (DataInputStream reader = new DataInputStream(new FileInputStream(rFile))) {
			if (rFile.exists()) {
				while (reader.available() > 0) {
					String line = reader.readUTF();
					binary.append(line).append("\n");
				}
			} else {
				System.exit(0);
			}
		} catch (EOFException e) {

		}
		return binary.toString();
	}

	public void writeEncryption(String text) throws IOException {
		try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(wFile))) {
			if (wFile.exists()) {
				writer.writeUTF(text);
			} else {
				throw new IOException("File does not exist: " + wFile.getAbsolutePath());
			}
		} catch (IOException e) {
			throw e;
		}
	}

}
