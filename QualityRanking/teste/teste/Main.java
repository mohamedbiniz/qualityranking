/**
 * 
 */
package teste;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufrj.cos.bri.WebFile;


/**
 * @author Fabricio
 * 
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws MalformedURLException,
			IOException {
		/*
		 * WebFile wf = new WebFile("http://www.silveiraneto.net/"); String s =
		 * (String) wf.getContent(); System.out.println(wf.getLastModified());
		 * Map<String, Integer> forwardLinks = wf.getForwardLinks(); for
		 * (String url : forwardLinks.keySet()) {
		 * System.out.println(forwardLinks.get(url) + ": " + url); }
		 */
		int start = 0;
		boolean hasNext = false;
		do {
			System.out.println("URLs desde " + start);
			WebFile wf = new WebFile("http://www.google.com/search?q=link:"
					+ URLEncoder.encode("http://www.simao.eti.br/", "utf-8")
					+ "&start=" + start);
			start += 10;
			String content = (String) wf.getContent();
			Pattern pNext = Pattern.compile(Pattern.quote("<span>Next</span>"));
			Matcher mNext = pNext.matcher(content);
			hasNext = mNext.find();

			Pattern p = Pattern.compile(Pattern.quote("<div class=g") + "[^>]*"
					+ Pattern.quote("><a href=\"") + "("
					+ Pattern.quote("http://") + "[^" + Pattern.quote("\"")
					+ "]+)");
			Matcher m = p.matcher(content);
			while (m.find()) {
				String matchedURL = m.group(1);
				System.out.println(matchedURL);
			}
		} while (hasNext);
	}
}
