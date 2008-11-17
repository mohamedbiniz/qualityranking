/**
 * 
 */
package teste;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseEntry;
import com.google.api.gbase.client.GoogleBaseFeed;
import com.google.api.gbase.client.GoogleBaseQuery;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.client.Service;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.ServiceException;

/**
 * @author Fabricio
 * 
 */
public class Teste {

	private static String user = "";

	private static String passwd = "";

	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {

		// try {
		// example();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ServiceException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		initiFrame();
	}

	private static void example() throws IOException, ServiceException {
		String developerKey = "1";
		GoogleBaseService service = new GoogleBaseService(
				"Google-Tutorial-1.0", developerKey);

		GoogleBaseQuery query = new GoogleBaseQuery(FeedURLFactory.getDefault()
				.getSnippetsFeedURL());
		query.setGoogleBaseQuery("digital camera");

		GoogleBaseFeed feed = service.query(query);

		for (GoogleBaseEntry entry : feed.getEntries()) {
			System.out.println(entry.getGoogleBaseAttributes().getItemType()
					+ ": " + entry.getTitle().getPlainText() + "- "
					+ entry.getId());
		}
	}

	private static void queryGoogle() throws IOException, ServiceException {

		String urlSite = "www.uol.com.br";
		String stringURL = null;
		stringURL = getStringURL("q", urlSite, "site", urlSite, "output", "xml");
		// stringURL = getStringURL("q", urlSite, "as_lq", urlSite);
		System.out.println(stringURL);
		GoogleBaseQuery googleQuery = new GoogleBaseQuery(new URL(stringURL));

		// googleQuery.addCustomParameter(new CustomParameter("q", "fabricio"));
		// googleQuery.addCustomParameter(new CustomParameter("output", "xml"));

		// GoogleService service = new GoogleService("servicoTeste",
		// "aplicacaoTeste");

		Service service = new Service();

		// user = ususarioField.getText();
		// passwd = senhaPasswordField.getText();

		// service.setUserCredentials(user, passwd);

		GDataRequest data = service.createFeedRequest(googleQuery);

		data.execute();

		InputStream is = data.getResponseStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String rs = null;

		while ((rs = rd.readLine()) != null) {
			System.out.println(rs); // sai o resultado da pagina
		}
	}

	private static String getStringURL(String... args) {
		// a string da url
		// String urlString = "http://www.google.com.br/search";

		String urlString = "http://search.iol.ie/cgi-bin/search";

		// os parametros a serem enviados
		Properties parameters = new Properties();
		for (int i = 0; i < args.length; i += 2) {
			parameters.setProperty(args[i], args[i + 1]);
		}

		// parameters.setProperty("output", "xml");

		// o iterador, para criar a URL
		Iterator<Object> i = parameters.keySet().iterator();
		// o contador
		int counter = 0;

		// enquanto ainda existir parametros
		while (i.hasNext()) {

			String name = (String) i.next();
			String value = parameters.getProperty(name);

			urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;

		}
		return urlString;
	}

	private static void initiFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400/* Largura */, 300/* Altura */);
		frame.setContentPane(getJpainelPanel());
		frame.show();
	}

	private static void calendar() throws IOException, ServiceException {
		CalendarService myService = new CalendarService(
				"exampleCo-exampleApp-1.0");
		myService.setUserCredentials(user, passwd);

		URL feedUrl = new URL(
				"http://www.google.com/calendar/feeds/default/allcalendars/full");
		CalendarFeed resultFeed = myService
				.getFeed(feedUrl, CalendarFeed.class);

		System.out.println("Your calendars:");
		System.out.println();

		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			CalendarEntry entry = resultFeed.getEntries().get(i);
			System.out.println("\t" + entry.getTitle().getPlainText());
		}

	}

	private static JPanel painelPanel;

	private static JTextField ususarioField = null;// Caixa de Texto usuário

	private static JPasswordField senhaPasswordField = null;// Caixa de Texto

	// senha

	private static JButton BotaoOK = null;

	private static JPanel getJpainelPanel() {
		if (painelPanel == null) {

			JLabel loginLabel = new JLabel();
			loginLabel.setBounds(new java.awt.Rectangle(20, 75, 100, 20));
			loginLabel.setText("Usuário:");
			JLabel senhaLabel = new JLabel();
			senhaLabel.setBounds(new java.awt.Rectangle(30, 105, 100, 20));
			senhaLabel.setText("Senha:");
			painelPanel = new JPanel();
			painelPanel.setLayout(null);
			painelPanel.add(loginLabel, null);// Inserindo label login
			painelPanel.add(getJUsuarioTextField(), null);/*
			 * Inserindo a caixa
			 * de texto de
			 * usuário
			 */
			painelPanel.add(senhaLabel, null);// Inserindo label senha
			painelPanel.add(getSenhaPasswordField(), null);/*
			 * Inserindo a caixa
			 * de texto de senha
			 */
			painelPanel.add(getBotaoOk(), null);// Inserindo botão OK
		}
		return painelPanel;
	}

	/* Criando as Caixas de texto Usuário e Senha */
	// Usuário
	private static JTextField getJUsuarioTextField() {
		if (ususarioField == null) {
			ususarioField = new JTextField();
			ususarioField.setBounds(new java.awt.Rectangle(80, 75, 237, 20));
		}
		return ususarioField;
	}

	// Senha
	private static JPasswordField getSenhaPasswordField() {
		if (senhaPasswordField == null) {
			senhaPasswordField = new JPasswordField();
			senhaPasswordField.setBounds(new java.awt.Rectangle(80, 105, 237,
					20));
		}
		return senhaPasswordField;
	}

	/* Criando botões --- OK e SAIR */
	// OK
	private static JButton getBotaoOk() {
		if (BotaoOK == null) {
			BotaoOK = new JButton();
			BotaoOK.setText("OK");
			BotaoOK.setBounds(new java.awt.Rectangle(80, 160, 100, 30));
			BotaoOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						queryGoogle();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ServiceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} finally {
						System.exit(0);
					}

				}

			});
		}
		return BotaoOK;
	}

	private static void teste3() throws HttpException, IOException {

		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();

		GetMethod get = new GetMethod("http://www.google.com.br/search");

		HttpMethodParams params = new HttpMethodParams();
		params.setParameter("q", "fabricio");
		params.setParameter("output", "xml_no_dts");
		get.setParams(params);
		client.executeMethod(get);

		// Aqui ja leio o retorno da pagina
		InputStream is = get.getResponseBodyAsStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String rs = null;

		while ((rs = rd.readLine()) != null) {
			System.out.println(rs); // sai o resultado da pagina
		}

	}

	private static void teste2() throws MalformedURLException {

		String msgRequest;
		// msgRequest="http://search.mycompany.com/search?q=query+string" +
		// "&site=default_collection" + "&client=default_frontend" +
		// "&output=xml_no_dtd" + "&proxystylesheet=default_frontend";
		msgRequest = "http://www.google.com.br/search?q=fabricio&output=xml";

		URL url = new URL(msgRequest);
		// url.

	}

	private static void teste() throws IOException {
		String urlString = getStringURL("q", "fabricio");
		System.out.println(urlString);
		// cria o objeto url
		urlString = "http://www.google.com.br/search?q=fabricio&ie=utf-8&oe=utf-8&aq=t";
		URL url = new URL(urlString);

		// cria o objeto httpurlconnection
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// seta o metodo
		connection.setRequestProperty("Request-Method", "GET");

		// seta a variavel para ler o resultado
		connection.setDoInput(true);
		connection.setDoOutput(false);

		// conecta com a url destino
		connection.connect();

		// abre a conex�o pra input
		BufferedReader br = new BufferedReader(new InputStreamReader(connection
				.getInputStream()));

		// le ate o final
		StringBuffer newData = new StringBuffer(10000);
		String s = "";
		while (null != ((s = br.readLine()))) {
			newData.append(s);
		}
		br.close();

		PrintWriter out = new PrintWriter(System.out, true);
		// imprime o codigo resultante
		out.println(new String(newData));

		// imprime o numero do resultado
		out.println("Resultado: " + connection.getResponseCode() + "/"
				+ connection.getResponseMessage());
	}
}
