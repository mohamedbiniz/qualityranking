//import br.ufrj.cos.bri.SearchEngine.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import br.ufrj.cos.GraphInstance;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.SearchEngine;
import br.ufrj.cos.foxset.search.SearchException;
import br.ufrj.cos.foxset.search.WebDocument;
import br.ufrj.cos.foxset.search.YahooSearch;
import br.ufrj.cos.foxset.search.SearchEngine.Result;
import edu.uci.ics.jung.algorithms.importance.HITS;
import edu.uci.ics.jung.algorithms.importance.NodeRanking;
import edu.uci.ics.jung.graph.Graph;

public class HubAuthorityGrafao {

	public static int qtdPag = 200;
	public static int qtdLinks = 10;
	public static int qtdLevels = 3;

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
			WebDocument wf = new WebDocument(url);
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
				if (++i <= qtdLinks) {
					getLinks(filho, nivel + 1, max);
				}
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
			int j = 0;
			for (Result r : results) {
				// if (++j > 2)
				// break;
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
			System.out.println("Exce��o na recurs�o");
			System.out.println("URL: " + url);
			System.out.println("Nivel: " + nivel);
			e.printStackTrace();
		}
	}

	public static void pajek() throws Exception {
		PrintWriter writer = new PrintWriter(new FileWriter("pajek.txt"));
		writer.println("*Vertices " + docs.size() + 1);
		for (String url : docs.keySet()) {
			Integer id = docs.get(url);
			writer.println(id + " \"" + url + "\"");
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
		if (urls == null || urls.size() == 0)
			carregarUrls();
		GraphInstance graphInstance = new GraphInstance();
		Graph graph = graphInstance.load("pajek.txt");
		HITS hitsAuthority = new HITS(graph, true);
		hitsAuthority.evaluate();
		HITS hitsHub = new HITS(graph, false);
		hitsHub.evaluate();
		for (String url : urls) {
			double authority = getScore(hitsAuthority, url);
			System.out.println(String.format("Authority (%s):\n%.5f", url,
					authority));
			double hub = getScore(hitsHub, url);
			System.out.println(String.format("Hub (%s):\n%.5f", url, hub));
			pwResultado.println(authority + " " + hub + " " + url);
			pwResultado.flush();
			if (connFoxset == null) {
				connFoxset = DriverManager
						.getConnection("jdbc:mysql://localhost/foxset?user=foxset&password=xamusko");
				psSelect = connFoxset
						.prepareStatement("SELECT id FROM document "
								+ "WHERE dataset_id = 578 AND url = ?");
				psUpdate = connFoxset
						.prepareStatement("UPDATE document_quality_dimension "
								+ "SET score = ? WHERE document_id = ? AND quality_dimension_id = ?");
			}
			psSelect.setString(1, url);
			ResultSet rs = psSelect.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				System.out.println("ID no FoxseT: " + id);
				System.out
						.println(String.format("Authority : %.5f", authority));
				psUpdate.setBigDecimal(1, new BigDecimal(round(authority)));
				psUpdate.setInt(2, id);
				psUpdate.setInt(3, 79); // Reputation

				int rows = psUpdate.executeUpdate();
				System.out.println(String.format("Hub : %.5f", hub));
				psUpdate.setBigDecimal(1, new BigDecimal(round(hub)));
				psUpdate.setInt(2, id);
				psUpdate.setInt(3, 81); // completeness

				rows += psUpdate.executeUpdate();
				System.out.println("Rows afetadas pelos 2 updates: " + rows);
			} else {
				System.out.println("Nao ha URL no Foxset: " + url);
			}
		}
	}

	private static double round(double authority) {
		return new Double(Math.round(authority * 100000)) / 100000.0;
	}

	private static void carregarUrls() throws IOException {
		urls = new HashSet<String>();
		docs = new HashMap<String, Integer>();

		FileReader fReader = new FileReader(new File("pajek.txt"));
		BufferedReader bReader = new BufferedReader(fReader);
		String line = bReader.readLine();
		while (line != null) {
			if (!line.startsWith("*Vertices")) {
				if (line.equals("*Arcs"))
					break;
				String[] words = line.split(" ");
				int id = Integer.parseInt(words[0]);
				String url = words[1].substring(1, words[1].length() - 1);
				docs.put(url, id);
				urls.add(url);
			}
			line = bReader.readLine();
		}
	}

	public static void main(String[] args) throws Exception {
		pwResultado = new PrintWriter(new FileWriter("resultado.txt"));
		File objetos = null;
		// se jah tiver o arquivo pajek.txt atribuir
		// valor true, atribuir false caso contr�rio
		boolean onlyJung = false;
		if (!onlyJung) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connIreval = DriverManager
					.getConnection("jdbc:mysql://localhost/ireval?user=foxset&password=xamusko");
			Statement connStat = connIreval.createStatement();

			connStat.setMaxRows(qtdPag);
			ResultSet rs = connStat
					.executeQuery("SELECT d.id, d.url FROM document AS d "
							+ "WHERE d.experiment_id = 1 AND "
							+ "((SELECT COUNT(DISTINCT evaluator_id) FROM document_evaluation AS de "
							+ "WHERE de.document_id = d.id AND de.linguistic_term_id IS NOT NULL) = 3)");
			int i = 0, iAnt = -1;
			objetos = new File("objetos.bin");
			if (objetos.exists()) {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(objetos));
				iAnt = ois.readInt();
				idMax = ois.readInt();
				urls = (Set<String>) ois.readObject();
				docs = (Map<String, Integer>) ois.readObject();
				idMax = new TreeSet<Integer>(docs.values()).last();
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
				getLinks(urlAtual, 1, qtdLevels);
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
		}
		jung();
		pwResultado.close();
		if (objetos != null)
			objetos.delete();
	}
}
