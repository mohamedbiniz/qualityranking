package jung.teste;

import jung.GraphInstance;
import junit.framework.TestCase;



import edu.uci.ics.jung.algorithms.importance.HITS;

import edu.uci.ics.jung.algorithms.importance.PageRank;

import edu.uci.ics.jung.graph.*;

import edu.uci.ics.jung.graph.impl.SparseGraph;

import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;

import junit.framework.TestCase;

import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer; 



public class GraphInstanceTest extends TestCase {

	

GraphInstance graphInstance = new GraphInstance();

	

	/*

	 * Test method for 'br.ufrj.HITS'

	 */

	public void testload() {

		

		/**

		 * Chama o metodo que cria um grafo vertice a vertice

		 */

		/*

		for(int i = 0; i < 10 ; i++)

		{

			graphInstance.createGraph(new Object[]{"Term"+i});

			

		}

		graphInstance.createGraph(new Object[]{"Term"+3});

		graphInstance.createGraph(new Object[]{"Term"+8});

		graphInstance.createGraph(new Object[]{"Term"+6});

		graphInstance.createGraph(new Object[]{"Term"+7});

		*/

		

		Graph graph = graphInstance.load("/Users/mayworm/Documents/pessoais/mestrado/tese/workspace/Jung/teste/jung/teste/graphTest2.txt");

		

		graphInstance.displayGraph(graph);

		

		//graphInstance.save(graph,"C:/eclipse/workspace/jung/graphTestOut.txt");

		

		/*

		 * Calcula Authorities

		 */

		System.out.println(" Ranking usando Authorities ");

		HITS rankerAuthorities = new HITS(graph, true);

		rankerAuthorities.evaluate();

		rankerAuthorities.printRankings(true, true);

		/*

		 * Calcula Hubs

		 */

		System.out.println(" Ranking usando Hubs ");

		HITS rankerHubs = new HITS(graph, false);

		rankerHubs.evaluate();

		rankerHubs.printRankings(true, true);

		/*

		 * Calcula PageRank

		 */

		System.out.println(" Ranking usando Pagerank ");

		DirectedGraph directedGraph = new DirectedSparseGraph();

		directedGraph = DirectionTransformer.toDirected(graph);

		//directedGraph= (DirectedSparseGraph)graph;

		PageRank rankerPageRank = new PageRank(directedGraph, 0.15);

		rankerPageRank.evaluate();

		rankerPageRank.printRankings(true, true);

		

		try {

			Thread.sleep(200000);

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}				

	}	




}
