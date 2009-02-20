//import br.ufrj.cos.bri.SearchEngine.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrj.cos.GraphInstance;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.SearchEngine;
import br.ufrj.cos.foxset.search.SearchException;
import br.ufrj.cos.foxset.search.WebFile;
import br.ufrj.cos.foxset.search.YahooSearch;
import br.ufrj.cos.foxset.search.SearchEngine.Result;
import edu.uci.ics.jung.algorithms.importance.HITS;
import edu.uci.ics.jung.algorithms.importance.NodeRanking;
import edu.uci.ics.jung.graph.Graph;

public class HubAuthorityGrafao {

	private static Connection connIreval, connFoxset;
	private static PreparedStatement psSelect, psUpdate;
	private static PrintWriter pwResultado;
	private static Set<String> urls = new HashSet<String>();
	private static Map<String, Integer> docs = new HashMap<String, Integer>();
	private static Set<String> lines = new HashSet<String>();
	private static int idMax = 0;

	public static List<Result> getBackLinks(String url) throws SearchException {
		int engine = 1;
		SearchEngine se = null;
		if (engine == 0) {
			se = new GoogleSearch();
			se.setAppID("F4ZdLRNQFHKUvggiU+9+60sA8vc3fohb");
		} else if (engine == 1) {
			se = new YahooSearch();
			se
					.setAppID("j3ANBxbV34FKDH_U3kGw0Jwj5Zbc__TDAYAzRopuJMGa8WBt0mtZlj4n1odUtMR8hco-");
		}
		return se.search("link:" + url);
	}

	public static void getLinks(String url, int nivel, int max) {
		if (nivel > max) {
			return;
		}
		try {
			Integer id = docs.get(url);
			WebFile wf = new WebFile(url);
			Map<String, Integer> fl = wf.getForwardLinks();
			System.out.println("Rec. " + nivel + ", FL = " + fl.size() + ": "
					+ id + " - " + url);
			int i = 0;
			for (String filho : fl.keySet()) {
				Integer idFilho = docs.get(filho);
				if (idFilho == null) {
					idFilho = ++idMax;
					docs.put(filho, idFilho);
				}
				lines.add(id + " " + idFilho + " 1");
				// if (++i <= 10) {
				getLinks(filho, nivel + 1, max);
				// }
			}

			List<Result> results = null;
			try {
				results = getBackLinks(url);
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
				results = getBackLinks(url);
			}
			System.out.println("Rec. " + nivel + ", BL = " + results.size()
					+ ": " + id + " - " + url);
			for (Result r : results) {
				String pai = r.getURL();
				Integer idPai = docs.get(pai);
				if (idPai == null) {
					idPai = ++idMax;
					docs.put(pai, idPai);
				}
				lines.add(idPai + " " + id + " 1");
				getLinks(pai, nivel + 1, max);
			}
			wf = null;
			System.gc();
		} catch (Exception e) {
			System.out.println("Exceção na recursão");
			System.out.println("URL: " + url);
			System.out.println("Nivel: " + nivel);
			e.printStackTrace();
		}
	}

	public static void pajek() throws Exception {
		PrintWriter writer = new PrintWriter(new FileWriter("pajek.txt"));
		writer.println("*Vertices " + docs.size());
		for (String url : docs.keySet()) {
			Integer id = docs.get(url);
			writer.println(id + " \"" + id + "\"");
		}
		writer.println("*Arcs");
		for (String linha : lines) {
			writer.println(linha);
		}
		writer.close();
	}

	public static double getScore(HITS hits, String url) {
		List rankings = hits.getRankings();
		int id = docs.get(url);
		for (Object obj : rankings) {
			NodeRanking ranking = (NodeRanking) obj;
			if (ranking.originalPos == id) {
				return Double.isNaN(ranking.rankScore) ? 0 : ranking.rankScore;
			}
		}
		System.out.println("ZERO!");
		return 0;
	}

	public static void jung() throws Exception {
		GraphInstance graphInstance = new GraphInstance();
		Graph graph = graphInstance.load("pajek.txt");
		HITS hitsAuthority = new HITS(graph, true);
		hitsAuthority.evaluate();
		HITS hitsHub = new HITS(graph, false);
		hitsHub.evaluate();
		for (String url : urls) {
			double authority = getScore(hitsAuthority, url);
			System.out.println("Authority (" + url + ") = " + authority);
			double hub = getScore(hitsHub, url);
			System.out.println("Hub (" + url + ") = " + hub);
			pwResultado.println(authority + " " + hub + " " + url);
			pwResultado.flush();
			if (connFoxset == null) {
				connFoxset = DriverManager
						.getConnection("jdbc:mysql://localhost/foxset?user=root&password=123");
				psSelect = connFoxset
						.prepareStatement("SELECT id FROM document WHERE dataset_id = 578 AND url = ?");
				psUpdate = connFoxset
						.prepareStatement("UPDATE document_quality_dimension SET score = ? WHERE document_id = ? AND quality_dimension_id = ?");
			}
			psSelect.setString(1, url);
			ResultSet rs = psSelect.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				System.out.println("ID no FoxseT: " + id);
				psUpdate.setBigDecimal(1, new BigDecimal(authority));
				psUpdate.setInt(2, id);
				psUpdate.setInt(3, 79); // Reputation

				int rows = psUpdate.executeUpdate();
				psUpdate.setBigDecimal(1, new BigDecimal(hub));
				psUpdate.setInt(2, id);
				psUpdate.setInt(3, 81); // completeness

				rows += psUpdate.executeUpdate();
				System.out.println("Rows afetadas pelos 2 updates: " + rows);
			} else {
				System.out.println("Nao ha URL no Foxset: " + url);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		pwResultado = new PrintWriter(new FileWriter("resultado.txt"));
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connIreval = DriverManager
				.getConnection("jdbc:mysql://localhost/ireval?user=root&password=123");
		ResultSet rs = connIreval
				.createStatement()
				.executeQuery(
						"SELECT d.id, d.url FROM document AS d WHERE d.experiment_id = 1 AND ((SELECT COUNT(DISTINCT evaluator_id) FROM document_evaluation AS de WHERE de.document_id = d.id AND de.linguistic_term_id IS NOT NULL) = 3)");
		int i = 0, iAnt = -1;
		File objetos = new File("objetos.bin");
		if (objetos.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					objetos));
			iAnt = ois.readInt();
			idMax = ois.readInt();
			urls = (Set<String>) ois.readObject();
			docs = (Map<String, Integer>) ois.readObject();
			lines = (Set<String>) ois.readObject();
			ois.close();
		}
		while (rs.next()) {
			System.out.println("i = " + ++i);
			if (i <= iAnt) {
				continue;
			}
			Integer idAtual = rs.getInt("id");
			String urlAtual = rs.getString("url");
			System.out.println("Select: " + idAtual + " - " + urlAtual);
			docs.put(urlAtual, ++idMax);
			urls.add(urlAtual);
			getLinks(urlAtual, 1, 2);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(objetos));
			oos.writeInt(i);
			oos.writeInt(idMax);
			oos.writeObject(urls);
			oos.writeObject(docs);
			oos.writeObject(lines);
			oos.close();
		}
		pajek();
		jung();
		pwResultado.close();
	}
}
