package Nrmeen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
/*
PlayFairCipher class implements the Playfair cipher encryption and decryption algorithm. 
It provides functionality for key management, message formatting, encryption, decryption, 
and file I/O operations.
*/

public class PlayFairCipher {
	private String key;
	private String Text;
	private String mKey;
	private char[] characterArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private char[][] matrix = new char[5][5];
	private File rFile;
	private File wFile;

	public PlayFairCipher() {
	}

	public PlayFairCipher(String Text, String key) {
		setKey(key);
		setText(Text);
		setMatrixKey();
		setMatrix();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String k) {
		String adjustedKey = "";
		boolean flag = false;
		for (int i = 0; i < k.length(); i++) {
			for (int j = 0; j < adjustedKey.length(); j++) {
				if (k.charAt(i) == adjustedKey.charAt(j)) {
					flag = true;
				}
			}
			if (flag == false) {
				adjustedKey += k.charAt(i);
			}
			flag = false;
		}
		this.key = adjustedKey;
	}

	public String getMatrixKey() {
		return mKey;
	}

	public void setMatrixKey() {
		boolean f = false;
		String keyWord = key;
		for (int i = 0; i < 26; i++) {
			if (characterArray[i] == 'j') {
				continue;
			}
			for (int j = 0; j < keyWord.length(); j++) {
				if (characterArray[i] == keyWord.charAt(j)) {
					f = true;
				}
			}
			if (f == false) {
				keyWord += characterArray[i];
			}
			f = false;
		}
		this.mKey = keyWord;
	}

	public char[][] getMatrix() {
		return matrix;
	}

	public void setMatrix() {
		int counter = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				matrix[i][j] = mKey.charAt(counter);
				counter++;
			}
		}
	}

	public String getText() {
		return Text;
	}

	public void setText(String Text) {
		String formatedMessage = "";
		for (int i = 0; i < Text.length(); i++) {
			if (Text.charAt(i) == 'j') {
				formatedMessage += 'i';
			} else {
				formatedMessage += Text.charAt(i);
			}
		}
		for (int i = 0; i < formatedMessage.length() - 1; i += 2) {
			if (formatedMessage.charAt(i) == formatedMessage.charAt(i + 1)) {
				formatedMessage = formatedMessage.substring(0, i + 1) + 'x' + formatedMessage.substring(i + 1);
			}
		}
		this.Text = formatedMessage;
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

	private String[] devidePairs(String text) {
		int counter = 0;
		if (text.length() % 2 != 0) {
			text += 'x';
		}
		String[] pairs = new String[text.length() / 2];
		for (int i = 0; i < pairs.length; i++) {
			pairs[i] = text.substring(counter, counter + 2);
			counter += 2;
		}
		return pairs;
	}

	public String encryption(String message) {
		String[] pairs = devidePairs(message);
		char firstChar;
		char secondChar;
		int[] char1Dimention = new int[2];
		int[] char2Dimention = new int[2];
		String cipherTxt = "";
		for (int i = 0; i < pairs.length; i++) {
			firstChar = pairs[i].charAt(0);
			secondChar = pairs[i].charAt(1);
			char1Dimention = getDimention(firstChar);
			char2Dimention = getDimention(secondChar);
			if (char1Dimention[0] == char2Dimention[0]) {
				if (char1Dimention[1] < 4) {
					char1Dimention[1]++;
				} else {
					char1Dimention[1] = 0;
				}
				if (char2Dimention[1] < 4) {
					char2Dimention[1]++;
				} else {
					char2Dimention[1] = 0;
				}
			} else if (char1Dimention[1] == char2Dimention[1]) {
				if (char1Dimention[0] < 4) {
					char1Dimention[0]++;
				} else {
					char1Dimention[0] = 0;
				}
				if (char2Dimention[0] < 4) {
					char2Dimention[0]++;
				} else {
					char2Dimention[0] = 0;
				}
			} else {
				int temp = char1Dimention[1];
				char1Dimention[1] = char2Dimention[1];
				char2Dimention[1] = temp;
			}
			char chr1 = toCharacter(char1Dimention[0], char1Dimention[1]);
			cipherTxt += chr1;
			char chr2 = toCharacter(char2Dimention[0], char2Dimention[1]);
			cipherTxt += chr2;
		}
		return cipherTxt;
	}

	public String decryption(String cipherTxt) {
		String[] pairs = devidePairs(cipherTxt);
		char firstChar;
		char secondChar;
		int[] char1Dimention = new int[2];
		int[] char2Dimention = new int[2];
		String plainTxt = "";
		for (int i = 0; i < pairs.length; i++) {
			firstChar = pairs[i].charAt(0);
			secondChar = pairs[i].charAt(1);
			char1Dimention = getDimention(firstChar);
			char2Dimention = getDimention(secondChar);
			if (char1Dimention[0] == char2Dimention[0]) {
				if (char1Dimention[1] > 0) {
					char1Dimention[1]--;
				} else {
					char1Dimention[1] = 4;
				}
				if (char2Dimention[1] > 0) {
					char2Dimention[1]--;
				} else {
					char2Dimention[1] = 4;
				}
			} else if (char1Dimention[1] == char2Dimention[1]) {
				if (char1Dimention[0] > 0) {
					char1Dimention[0]--;
				} else {
					char1Dimention[0] = 4;
				}
				if (char2Dimention[0] > 0) {
					char2Dimention[0]--;
				} else {
					char2Dimention[0] = 4;
				}
			} else {
				int temp = char1Dimention[1];
				char1Dimention[1] = char2Dimention[1];
				char2Dimention[1] = temp;
			}
			char chr1 = toCharacter(char1Dimention[0], char1Dimention[1]);
			plainTxt += chr1;
			char chr2 = toCharacter(char2Dimention[0], char2Dimention[1]);
			plainTxt += chr2;
		}
		return plainTxt;
	}

	private int[] getDimention(char letter) {
		int[] dimention = new int[2];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == letter) {
					dimention[0] = i;
					dimention[1] = j;
				}
			}
		}
		return dimention;
	}

	public char toCharacter(int row, int column) {
		return matrix[row][column];
	}

	private char toCharacter(int index) {
		return characterArray[index];
	}

	public String Generation(double A, double Z, int length) {
		String theKey = "";
		for (int i = 0; i < length; i++) {
			A = Z * A * (1 - A);
			int letterIndex = (int) (Math.round(A * Math.pow(10, 16)) % 26);
			char theLetter = toCharacter(letterIndex);
			theKey += theLetter;
		}
		return theKey;
	}

	public StringBuilder read() throws FileNotFoundException {
		StringBuilder text = new StringBuilder();
		try (Scanner read = new Scanner(rFile);) {
			while (read.hasNextLine()) {
				String line = read.nextLine();
				text.append(line);
			}
		}
		return text;
	}

	public void write(String text) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(wFile);) {
			writer.print(text);
			writer.flush();
		}
	}
}
