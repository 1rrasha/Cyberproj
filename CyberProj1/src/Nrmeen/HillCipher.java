package Nrmeen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.security.SecureRandom;

/**
 * The HillCipher class implements the Hill cipher encryption and decryption
 * algorithm. It provides methods for generating random keys, encrypting and
 * decrypting messages, reading from and writing to files, and performing
 * various matrix operations required for the encryption and decryption
 * processes.
 */

public class HillCipher {
	private int[][] key = new int[3][3];
	private String Text;
	private char[] chaArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private int M = 3;
	private File readingFile;
	private File writtingFile;

	public HillCipher() {
	}

	public int getM() {
		return M;
	}

	public void setM(int n) {
		this.M = M;
	}

	public HillCipher(String Text, int[][] key) {
		this.key = key;
		this.Text = Text;
	}

	public int[][] getKey() {
		return key;
	}

	public void setKey(int[][] key) {
		this.key = key;
	}

	public String getText() {
		return Text;
	}

	public void setText(String Text) {
		this.Text = Text;
	}

	public File getRF() {
		return readingFile;
	}

	public void setRF(File readingFile) {
		this.readingFile = readingFile;
	}

	public File getWF() {
		return writtingFile;
	}

	public void setWF(File writtingFile) {
		this.writtingFile = writtingFile;
	}

	private String[] devideGroupsOfM(String Text) {
		while (Text.length() % 3 != 0) {
			Text += 'x';
		}
		String[] g = new String[Text.length() / 3];
		int counter = 0;
		for (int i = 0; i < g.length; i++) {
			g[i] = Text.substring(counter, counter + 3);
			counter += 3;
		}
		return g;
	}

	public String encryption(int[][] key, String Text) {
		Text = Text.replaceAll("\\s", "");
		Text = Text.toLowerCase();
		String cipherTxt = "";
		String[] groups = devideGroupsOfM(Text);

		for (int i = 0; i < groups.length; i++) {
			String cipherGrp = "";
			for (int k = 0; k < groups[i].length(); k++) {
				char cipherchr = ' ';
				int cipherchrNumric = 0;
				for (int j = 0; j < groups[i].length(); j++) {
					cipherchrNumric += (numricChracter(groups[i].charAt(j)) * key[k][j]) % 26;
				}
				cipherchr = toCharacter(cipherchrNumric % 26);
				cipherGrp += cipherchr;
			}
			cipherTxt += cipherGrp;
		}
		return cipherTxt;
	}

	public String decryption(String cipherTxt, int[][] key) throws MException {
		cipherTxt = cipherTxt.toLowerCase();
		int[][] keyInverse = Inverse(key);
		if (keyInverse == null) {
			return null;
		}
		String plainTxt = "";
		String[] groups = devideGroupsOfM(cipherTxt);

		for (int i = 0; i < groups.length; i++) {
			String plainGrp = "";
			for (int k = 0; k < groups[i].length(); k++) {
				char plainchr = ' ';
				int plainchrNumric = 0;
				for (int j = 0; j < groups[i].length(); j++) {
					plainchrNumric += (numricChracter(groups[i].charAt(j)) * keyInverse[k][j]) % 26;
					if (plainchrNumric < 0) {
						plainchrNumric += 26;
					}
				}
				plainchr = toCharacter(plainchrNumric % 26);
				plainGrp += plainchr;
			}
			plainTxt += plainGrp;
		}
		return plainTxt;
	}

	private int keyDeterminant(int key[][]) throws MException {
		if (key.length != key[0].length) {
			throw new MException();
		} else if (key.length == 1) {
			return key[0][0];
		} else if (key.length == 2) {
			return key[0][0] * key[1][1] - key[0][1] * key[1][0];
		} else {
			int determinant = 0;
			for (int j = 0; j < key.length; j++) {
				determinant += Math.pow(-1, j) * key[0][j] * keyDeterminant(getSubMatrix(key, 0, j));
			}
			return determinant;
		}
	}

	private int[][] getSubMatrix(int[][] matrix, int rowExcluded, int columnExcluded) {
		int[][] subMatrix = new int[matrix.length - 1][matrix.length - 1];
		int subMatrixRow = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (i == rowExcluded) {
				continue;
			}
			int subMatrixColumn = 0;
			for (int j = 0; j < matrix.length; j++) {
				if (j == columnExcluded) {
					continue;
				}
				subMatrix[subMatrixRow][subMatrixColumn] = matrix[i][j];
				subMatrixColumn++;
			}
			subMatrixRow++;
		}
		return subMatrix;
	}

	public int inverseOfDeterminant() throws MException {
		int determinant = keyDeterminant(this.key);
		int theMod = 26;
		int originalM = theMod;
		int x0 = 0;
		int x1 = 1;
		while (determinant > 1) {
			try {
				int qoutient = determinant / theMod;
				int temp = theMod;
				theMod = determinant % theMod;
				determinant = temp;
				temp = x0;
				x0 = x1 - qoutient * x0;
				x1 = temp;
			} catch (ArithmeticException e) {
				return 0;
			}
		}
		if (x1 < 0) {
			x1 += originalM;
		}

		if ((keyDeterminant(this.key) * x1) % originalM == 1) {
			return x1;
		} else {
			return 0;
		}
	}

	private int[][] Adj(int key[][]) throws MException {
		int adj[][] = new int[key.length][key[0].length];
		for (int i = 0; i < key.length; i++) {
			for (int j = 0; j < key[0].length; j++) {
				adj[i][j] = (int) (Math.pow(-1, i + j) * keyDeterminant(getSubMatrix(key, i, j)));
			}
		}
		int[][] changePos = new int[adj.length][adj[0].length];
		for (int i = 0; i < adj.length; i++) {
			for (int j = 0; j < adj[0].length; j++) {
				changePos[i][j] = adj[j][i];
			}
		}
		return changePos;
	}

	private int[][] Inverse(int[][] key) throws MException {
		int determinantInverse = inverseOfDeterminant();
		if (determinantInverse == 0) {
			return null;
		}
		int[][] adj = Adj(this.key);
		int[][] keyInverse = new int[adj.length][adj[0].length];
		for (int i = 0; i < keyInverse.length; i++) {
			for (int j = 0; j < keyInverse[0].length; j++) {
				keyInverse[i][j] = determinantInverse * adj[i][j] % 26;
			}
		}
		return keyInverse;
	}

	public int[][] Generation(String seedString) {
		byte[] seed = seedString.getBytes();
		SecureRandom random = new SecureRandom(seed);
		int[][] theKey = new int[3][3];
		for (int i = 0; i < theKey.length; i++) {
			for (int j = 0; j < theKey[i].length; j++) {
				theKey[i][j] = random.nextInt(26);
			}
		}
		return theKey;
	}

	private int numricChracter(char character) {
		for (int i = 0; i < chaArray.length; i++) {
			if (chaArray[i] == character) {
				return i;
			}
		}
		return 0;
	}

	private char toCharacter(int index) {
		return chaArray[index];
	}

	public StringBuilder readf() throws FileNotFoundException {
		StringBuilder text = new StringBuilder();
		try (Scanner read = new Scanner(readingFile);) {
			while (read.hasNextLine()) {
				String line = read.nextLine();
				text.append(line);
			}
		}
		return text;
	}

	public void writef(String text) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(writtingFile);) {
			writer.print(text);
			writer.flush();
		}
	}

}
