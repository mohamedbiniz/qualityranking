/**
 * 
 */
package trab02;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeSet;

import trab01.MainProcess01;

/**
 * @author Fabricio
 * 
 */
public class StopWords {

	private static TreeSet<String> stopWords;

	static {
		String[] words = {};
		try {
			words = loadStopWords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stopWords = new TreeSet<String>();
		for (String str : words) {
			stopWords.add(str);
		}

	}

	public static boolean contains(String str) {
		return stopWords.contains(str);
	}

	private static String[] loadStopWords() throws IOException {
		File f = new File(MainProcess01.PATH_DOCS + "stopwords");
		FileInputStream in = new FileInputStream(f);
		String str = "";
		while (true) {
			int ch = in.read();
			if (ch < 0)
				break;
			str = str + ((char) ch);
		}
		str = str.replaceAll("\r", "").replaceAll("\n\n", ",").replaceAll("\n",
				",");
		str = str.toUpperCase();
		return str.split(",");
	}
}
