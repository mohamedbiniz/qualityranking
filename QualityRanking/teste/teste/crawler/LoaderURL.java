package teste.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.google.api.gbase.client.GoogleBaseQuery;
import com.google.gdata.client.Service;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.util.ServiceException;

/**
 * @author Fabricio
 * 
 */
public class LoaderURL {

	protected static final String SEARCH_MAIN_URL;

	static {
		SEARCH_MAIN_URL = "http://www.google.com.br/search";
	}

	private String urlString;

	private Set<String> backLinks;

	public LoaderURL(String urlString) {
		setUrlString(urlString);
		setBackLinks(new TreeSet<String>());
	}

	private void executeQueries() throws IOException, ServiceException {

		String stringURLSearch = null;
		stringURLSearch = getStringURLSearch("as_lq", getUrlString());

		GoogleBaseQuery googleQuery = new GoogleBaseQuery(new URL(
				stringURLSearch));

		Service service = new Service();

		GDataRequest data = service.createFeedRequest(googleQuery);

		data.execute();

		String resultInHtmlString = streamToString(data.getResponseStream());
		// TODO - tem q ajeitar pra pegar todos os resultados das consultas, e
		// não apenas os 10 primeiros
		setBackLinks(getBakLinksOfHTML(resultInHtmlString));

	}

	private String streamToString(InputStream is) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String rs = null;
		String resultHTML = "";
		while ((rs = rd.readLine()) != null) {
			resultHTML += rs;
		}
		return resultHTML;
	}

	private Set<String> getBakLinksOfHTML(String resultInHtmlString)
			throws IOException {
		Set<String> backLinks = new TreeSet<String>();
		// TODO - pegar a lista de itens, e para cada item d resultado extrair o
		// link (dá pra fazer com o htmlparser
		return backLinks;
	}

	private String getStringURLSearch(String... args) {

		String stringURLSearch = SEARCH_MAIN_URL;
		Properties parameters = new Properties();
		for (int i = 0; i < args.length; i += 2) {
			parameters.setProperty(args[i], args[i + 1]);
		}

		Iterator<Object> i = parameters.keySet().iterator();
		int counter = 0;

		while (i.hasNext()) {
			String name = (String) i.next();
			String value = parameters.getProperty(name);
			stringURLSearch += (++counter == 1 ? "?" : "&") + name + "="
					+ value;
		}
		return stringURLSearch;
	}

	public String getUrlString() {
		return urlString;
	}

	protected void setUrlString(String urlString) {
		this.urlString = urlString;
		try {
			executeQueries();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Set<String> getBackLinks() {
		return backLinks;
	}

	public void setBackLinks(Set<String> backLinks) {
		this.backLinks = backLinks;
	}
}
