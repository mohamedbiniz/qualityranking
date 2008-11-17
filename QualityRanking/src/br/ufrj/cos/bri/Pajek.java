/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.bri;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import edu.uci.ics.jung.algorithms.importance.HITS;
import edu.uci.ics.jung.algorithms.importance.PageRank;
import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;

/**
 * 
 * @author Heraldo
 */
public class Pajek {

	public void pajek() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager
				.getConnection("jdbc:mysql://localhost/sc?user=bri&password=bri");
		ResultSet rs = conn.createStatement().executeQuery(
				"SELECT COUNT(idPagina) AS rows FROM pagina");
		rs.next();
		int rows = rs.getInt("rows");
		PrintWriter writer = new PrintWriter(new FileWriter("pajek.txt"));
		writer.println("*Vertices " + rows);
		rs = conn.createStatement().executeQuery(
				"SELECT id, idPagina, url FROM pagina");
		while (rs.next()) {
			writer.println(rs.getLong("idPagina") + " \"" + rs.getString("url")
					+ "\"");
		}
		writer.println("*Arcs");
		rs = conn
				.createStatement()
				.executeQuery(
						"SELECT pai.idPagina AS ordem_pai, filho.idPagina AS ordem_filho "
								+ "FROM pagina AS pai "
								+ "INNER JOIN outputlink AS link ON (link.idPagina = pai.id) "
								+ "INNER JOIN pagina AS filho ON (filho.id = link.id) "
								+ "ORDER BY pai.idPagina, filho.idPagina");
		while (rs.next()) {
			writer.println(rs.getLong("ordem_pai") + " "
					+ rs.getLong("ordem_filho") + " 1");
		}
		writer.close();
	}

	public void jung() {
		GraphInstance graphInstance = new GraphInstance();
		Graph graph = graphInstance.load("pajek.txt");
		graphInstance.displayGraph(graph);
		// graphInstance.save(graph,"C:/eclipse/workspace/jung/graphTestOut.txt");
		System.out.println(" Ranking usando Authorities ");
		HITS rankerAuthorities = new HITS(graph, true);
		rankerAuthorities.evaluate();
		rankerAuthorities.printRankings(true, true);
		System.out.println(" Ranking usando Hubs ");
		HITS rankerHubs = new HITS(graph, false);
		rankerHubs.evaluate();
		rankerHubs.printRankings(true, true);
		System.out.println(" Ranking usando Pagerank ");
		DirectedGraph directedGraph = new DirectedSparseGraph();
		directedGraph = DirectionTransformer.toDirected(graph);
		// directedGraph= (DirectedSparseGraph)graph;
		PageRank rankerPageRank = new PageRank(directedGraph, 0.15);
		rankerPageRank.evaluate();
		rankerPageRank.printRankings(true, true);
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) throws Exception {
	// pajek();
	// jung();
	// }
}
