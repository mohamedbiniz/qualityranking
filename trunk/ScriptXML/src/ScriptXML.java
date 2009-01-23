import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ScriptXML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		
		// Formato XML - E-mail
		try{
			BufferedReader email = new BufferedReader(new FileReader("C:/Documents and Settings/Bernardo Pacheco/email.txt"));
			String srtEmail;
			while (email.ready()){ 
				srtEmail = email.readLine();
				System.out.println("<evaluator email=\"" + srtEmail + "\" />");
			}
			email.close();
		}
		catch(IOException e){}
		
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
		
		
		// Formato XML - URL
		try{
			BufferedReader url = new BufferedReader(new FileReader("C:/Documents and Settings/Bernardo Pacheco/url.txt"));
			String srtUrl;
			while (url.ready()){
				srtUrl = url.readLine();
				System.out.println("<document url=\"" + srtUrl + "\" />");
			}
			url.close();
		}
		
		catch(IOException e){}
		
	}
}
