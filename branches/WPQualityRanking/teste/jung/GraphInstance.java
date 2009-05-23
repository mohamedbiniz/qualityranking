package jung;

import java.util.Iterator;
import java.util.Set;
import java.awt.Color;
import java.awt.Paint;
import java.io.*;
import javax.swing.JFrame;
import java.lang.Object;
//import edu.uci.ics.jung.io.PajekNetReader.ListTagPred;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.StringLabeller.UniqueLabelException;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabellerStringer;
import edu.uci.ics.jung.graph.decorators.NumberEdgeValue;
import edu.uci.ics.jung.graph.decorators.NumberVertexValue;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberVertexValue;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.SimpleSparseVertex;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.utils.TypedVertexGenerator;
import edu.uci.ics.jung.utils.GraphUtils;
import edu.uci.ics.jung.io.PajekNetFile;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer;

public class GraphInstance {
private String prevTerm=null;
	
	/**
	 *Esta classe é responsavel por colocar label nos vertices do grafo. 
	 */
	private StringLabeller vlabeller;
	
	/**
	 * Esta classe é resposavel por colocar label nas arestas 
	 */
	public EdgeWeightLabeller elabeller;
		
	/**
	 * Grafo que representa a relação entre os termos de uma coleção.
	 * 
	 */
	private Graph graph;
	
	/**
	 * É uma referencia para o vertice que mais aparece no grafo. Equivale ao 
	 * termo mais comum, ou seja, o de maior probabilidade e de menor energia.
	 */	
	SparseVertex mostCommon;
	
	/**
	* É a chave para o UserData que contem o nome do termo contido dentro 
	* do vertice. 
	 */
	private String vertexName = "vertexName";
	/**
	 * É a chave para o valor do vertice. Na pratica é o numero de vezes que
	 * o termo aparece na coleçao
	 */
	private String vertexValue = "vertexValue";
	
	public Graph getGraph()
	{
		return this.graph;
	}	
	
	public GraphInstance () {
		super();
		this.graph = new SparseGraph();
		this.vlabeller = StringLabeller.getLabeller(this.graph);
		this.elabeller = EdgeWeightLabeller.getLabeller(this.graph);
	}
		
	/**
	 * Este metodo cria um vertex e o guarda na hash para futuras consultas.
	 * @param term é o conteudo do vertex e tambem a chave da hashtable
	 * @return retorna o vertex criado
	 */
	private SparseVertex createVertex(String term) {
		SparseVertex vertex = new SparseVertex();
		vertex.addUserDatum(vertexName,term,UserData.SHARED); 
		vertex.addUserDatum(vertexValue,1,UserData.SHARED); //autobox
		graph.addVertex(vertex);
		try {
			vlabeller.setLabel(vertex,term);
		} catch (UniqueLabelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vertex;
	}

	/**
	 * Este metodo cria um grafo vertice a vertice, ligando um vertice no vertice anterior 
	 * @see br.ufrj.index.dao.CodificationFunction#evalCoeficient(java.lang.Object[])
	 */
	public int createGraph(Object[] param) {
		String term = (String) param[0];
		if(vlabeller.getVertex(term)!=null)
			updateVertex(term);
		else
			createVertex(term);			
		//faz a ligação
		if (this.prevTerm!=null){
			SparseVertex vertexFrom = (SparseVertex) vlabeller.getVertex(this.prevTerm);
			SparseVertex vertexTo = (SparseVertex) vlabeller.getVertex(term);
			DirectedSparseEdge edge = findEdge(vertexFrom,vertexTo);
			if(edge!=null)
				updateEdge(edge);
			else
				createEdge(vertexFrom,vertexTo);
		}		
		this.prevTerm = term;
		
		return -1;
	}	

	/**
	 * Cria uma aresta a partir de seus vertices
	 * @param vertexFrom
	 * @param vertexTo
	 */
	private void createEdge(SparseVertex vertexFrom, SparseVertex vertexTo) {
		//criando a aresta
		DirectedSparseEdge edge = new DirectedSparseEdge(vertexFrom,vertexTo);
		this.graph.addEdge(edge);
		elabeller.setWeight(edge,1); //dando peso
				
	}

	/**
	 * Atualiza os dados de uma aresta. Ná pratica apenasa incrementa o contador
	 * que mostra o numero de vezes que aquela aresta ocorreu. 
	 * @param termFrom
	 * @param termTo
	 */
	private void updateEdge(DirectedSparseEdge edge) {
		int c = elabeller.getWeight(edge);
		c++;
		elabeller.setWeight(edge,c);
			
	}
	/**
	 * Procura pela aresta indicada
	 * @param vertexFrom
	 * @param vertexTo
	 * @return
	 */
	private DirectedSparseEdge findEdge(SparseVertex vertexFrom, SparseVertex vertexTo) {
		DirectedSparseEdge edge = null;
		Set edges = vertexFrom.getOutEdges();
		
		Iterator it = edges.iterator();
		while(it.hasNext())
		{
			edge = (DirectedSparseEdge) it.next();
			if(edge.getDest()==vertexTo)
				break;
			else
				edge = null;
		}
		return edge;
	}


	/**
	 * Cada aresta do greafo deve ter uma representanção que lhe 
	 * serve como chave. Este metodo calcula esta chave;
	 * Trivialemente a representação é definida como
	 * termFrom->termTo
	 * @param termFrom termo de origem da arestaa
	 * @param termTo termo de destido da aresta
	 * @return a chave que representa esta aresta como definido acima
	 */
	private String evalEdgeKey(String termFrom, String termTo) {
		String edgeKey = termFrom+"->"+termTo;
		return edgeKey;
	}

	/**
	 * Este metodo atualiza os dados de um vertice do grafo. Note que 
	 * trivialmente a atualização corresponde ao numero de vezes que o
	 * termo aparece na coleção, portanto basta que atualizamos
	 * o contador interno do vertice;
	 * Além disso este método atualiza o vertice mais comum da coleção.
	 * Note que se nenhum vertice ocorrer mais vezes, entao todos os vertices
	 * terão ocorrido 1 unica vez e portanto sendo indiferente escolher o mais comum.
	 * 
	 * @param term
	 */
	private SparseVertex updateVertex(String term) {
		SparseVertex vertex = (SparseVertex) vlabeller.getVertex(term);
		
		int count = (Integer) vertex.getUserDatum(vertexValue);
		count++; //incrementa contador
		vertex.setUserDatum(term,count,UserData.SHARED); //atualiza o dado do vertice (autobox)
		//testa se é o mais comum
		if (this.mostCommon!=null){
			int mostCommonCount=(Integer)mostCommon.getUserDatum(vertexValue); //autobox java 5
			if(count>mostCommonCount) //se novo no maior que i, entao troque
				this.mostCommon = vertex;
		}else{
			this.mostCommon=vertex;
		}
		return vertex;
		
	}

	/**
	 * Este metodo carrega um grafo a partir de um arquivo no formato pajek
	 * @param filename
	 * @return
	 */
	public Graph load(String filename){
		Graph currentGraph = new SparseGraph();
		try{
			PajekNetReader netReader = new PajekNetReader(true);
			EdgeWeightLabeller edgeWeightLabellerGraph = EdgeWeightLabeller.getLabeller(currentGraph);
			currentGraph = netReader.load(filename, currentGraph, edgeWeightLabellerGraph);
			//bizu achado num site japones
		    StringLabeller v_labeller = StringLabeller.getLabeller(currentGraph, PajekNetReader.LABEL);
		    currentGraph.setUserDatum(StringLabeller.DEFAULT_STRING_LABELER_KEY, v_labeller, UserData.SHARED);
		    currentGraph.removeUserDatum(PajekNetReader.LABEL);			
			
		} catch (IOException e){
	            System.out.println("Error in loading graph");
	            e.printStackTrace();
	    }	
		return currentGraph;
	}
	
	/**
	 * Este metodo salva um grafo no formato pajek
	 * @param graphToBeSaved
	 * @param filename
	 */
	public void save(Graph graphToBeSaved, String filename){
		try{
			PajekNetWriter netWriter = new PajekNetWriter();
			StringLabeller StringLabellerGraph = StringLabeller.getLabeller(graphToBeSaved);
			EdgeWeightLabeller edgeWeightLabellerGraph = EdgeWeightLabeller.getLabeller(graphToBeSaved);
			netWriter.save(graphToBeSaved, filename, (VertexStringer)StringLabellerGraph, (NumberEdgeValue)edgeWeightLabellerGraph);			
		}catch (IOException e){
            System.out.println("Error in saving graph");
            e.printStackTrace();
		}	
	}
	
	/**
	 * Este metodo mostra um grafo na tela
	 * @param currentGraph
	 */
	public void displayGraph(Graph currentGraph){
		JFrame jf = new JFrame();
		PluggableRenderer pr = new PluggableRenderer();
		pr.setVertexStringer(StringLabeller.getLabeller(currentGraph));
		pr.setEdgeStringer(new EdgeWeightLabellerStringer(EdgeWeightLabeller.getLabeller(currentGraph)));
		pr.setVertexPaintFunction(new MostCommonVertexPaintFunction());
		VisualizationViewer vv = new VisualizationViewer(new FRLayout(currentGraph), pr);
		vv.setBackground(Color.WHITE);
		vv.revalidate();
		vv.repaint();		
        jf.getContentPane().add(vv);        
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}	
	
	/**
	 * Este metodo colore os vertices de um grafo
	 * @author wallace
	 *
	 */
	class MostCommonVertexPaintFunction implements VertexPaintFunction {

		public Paint getFillPaint(Vertex v) {
			/*
			if(v == graphInstance.mostCommon)
				return Color.blue;
			else
				return Color.RED;
			*/
			return Color.blue;
		}

		public Paint getDrawPaint(Vertex v) {
			// TODO Auto-generated method stub
			return null;
		}
	}	
	
}
