package engine.XML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import beans.Artigo;
import beans.Item;
import beans.Query;

//import com.thoughtworks.xstream.XStream;

public class Loader {

	public Loader(String path) {
		setPath(path);
	}

	private String path;

	public List<Query> loadQueries() throws JDOMException, IOException {
		File f = new File(getPath() + "cfquery.xml");

		// Classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Toda a estrutura do arquivo est? contida nesse objeto.
		Document document = sb.build(f);

		// Recupera o elemento root
		Element root = document.getRootElement();

		// Adicionada todos os records para cada documento
		List<Element> querys = root.getChildren();

		List<Query> queries = new ArrayList<Query>();
		for (Element q : querys) {
			Query query = new Query();

			query.setNumero(Integer.parseInt(findNumeroQuery(q)));
			query.setText(findText(q));

			queries.add(query);
			// System.out.println(query);
		}
		return queries;
	}

	private String findText(Element q) {
		return q.getChildText("QueryText");
	}

	private String findNumeroQuery(Element q) {
		return q.getChildText("QueryNumber").trim();
	}

	public List<Item> loadItens() throws JDOMException, IOException {

		File f = new File(getPath() + "cfquery.xml");

		// Classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Toda a estrutura do arquivo est? contida nesse objeto.
		Document document = sb.build(f);

		// Recupera o elemento root
		Element root = document.getRootElement();

		// Adicionada todos os records para cada documento
		List<Element> querys = root.getChildren();

		List<Item> itens = new ArrayList<Item>();
		for (Element q : querys) {
			int numQuery = Integer.parseInt(findNumeroQuery(q));

			List<Element> records = q.getChildren("Records");

			for (Element record : records) {

				List<Element> itemRecord = record.getChildren("Item");

				for (Element itemR : itemRecord) {

					Item item = new Item();

					item.setNumeroArtigo(Integer
							.parseInt(findNumeroArtigoInItem(itemR)));
					item.setNumeroQuery(numQuery);
					item.setNotas(findScoreInItem(itemR));
					itens.add(item);
					System.out.println(item);
				}
			}

		}
		return itens;
	}

	public List<Artigo> loadArtigos() throws JDOMException, IOException {
		// Recupera os elementos filhos
		List<Element> records = new ArrayList<Element>();

		for (int i = 4; i < 10; i++) {

			File f = new File(String.format(getPath() + "cf7%1d.xml", i));

			// Classe SAXBuilder que vai processar o XML
			SAXBuilder sb = new SAXBuilder();

			// Toda a estrutura do arquivo est? contida nesse objeto.
			Document document = sb.build(f);

			// Recupera o elemento root
			Element root = document.getRootElement();

			// Adicionada todos os records para cada documento
			records.addAll(root.getChildren());
		}

		List<Artigo> artigos = new ArrayList<Artigo>();
		for (Element record : records) {
			Artigo artigo = new Artigo();

			artigo.setNumero(Integer.parseInt(findNumeroRecord(record)));
			artigo.setAbstracT(findAbstract(record));
			artigo.setTitle(findTitulo(record));
			artigo.setCorpo(findCorpo(record));

			artigos.add(artigo);
			// System.out.println(artigo);
		}
		return artigos;

	}

	private String findScoreInItem(Element record) {

		String score = record.getAttributeValue("score");

		return score;
	}

	private String findNumeroArtigoInItem(Element record) {

		String artigoOfItem = record.getValue().trim();
		int numArtigo = Integer.parseInt(artigoOfItem);
		return artigoOfItem;
	}

	private String findNumeroRecord(Element record) {
		return record.getChildText("RECORDNUM").trim();
	}

	private static String findCorpo(Element record) {
		String corpo = findTitulo(record) + " " + findAbstract(record) + " "
				+ findAtributtes(record) + " " + findAutores(record) + " "
				+ findTopicos(record) + " " + findReferences(record);

		return corpo;
	}

	private static String findTopicos(Element record) {
		String topicosStr = "";
		List<Element> topicos = null;

		topicos = record.getChildren("MAJORSUBJ");
		for (Element topico : topicos) {
			topicosStr += " " + topico.getChildText("TOPICO");
		}

		topicos = record.getChildren("MINORSUBJ");
		// topicos.addAll(record.getChildren("MINORSUBJ"));
		for (Element topico : topicos) {
			topicosStr += " " + topico.getChildText("TOPICO");
		}

		return topicosStr;
	}

	private static String findReferences(Element record) {
		String citesStr = "";
		List<Element> references = record.getChildren("REFERENCES");
		for (Element reference : references) {
			Element cite = reference.getChild("CITE");
			if (cite != null) {
				citesStr += " " + cite.getAttributeValue("num") + " "
						+ cite.getAttributeValue("author") + " "
						+ cite.getAttributeValue("publication") + " "
						+ cite.getAttributeValue("d1") + " "
						+ cite.getAttributeValue("d2") + " "
						+ cite.getAttributeValue("d3");
			}
		}
		return citesStr;
	}

	private static String findAtributtes(Element record) {
		return record.getChildText("PAPERNUM") + " "
				+ record.getChildText("RECORDNUM") + " "
				+ record.getChildText("MEDLINENUM") + " "
				+ record.getChildText("SOURCE");

	}

	private static String findAutores(Element record) {
		String autoresStr = "";
		List<Element> autores = record.getChildren("AUTHORS");
		for (Element autor : autores) {
			autoresStr += " " + autor.getChildText("AUTHOR");
		}
		return autoresStr;
	}

	private static String findTitulo(Element record) {
		return record.getChildText("TITLE");
	}

	private static String findAbstract(Element record) {
		String abstracT = null;
		abstracT = record.getChildText("ABSTRACT");
		if (abstracT == null) {
			abstracT = "";// record.getChildText("EXTRACT");
		}
		return abstracT;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}