import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.SearchEngine;
import br.ufrj.cos.foxset.search.SearchException;
import br.ufrj.cos.foxset.search.WebDocument;
import br.ufrj.cos.foxset.search.YahooSearch;
import br.ufrj.cos.foxset.search.SearchEngine.Result;

/**
 * 
 */

/**
 * @author Fabricio
 * 
 */
public class TestLinks {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// testLinks();
		updateDocuments(577);

	}

	private static void updateDocuments(int i) {
		HibernateDAO.getInstance().openSession();
		DataSet dataSet = (DataSet) HibernateDAO.getInstance().loadById(
				DataSet.class, new Long(577));
		try {
			List<Document> documents = HelperAcessDB.loadDocuments(dataSet);

			for (Document document : documents) {
				document
						.setUrl(HubAuthorityGrafao.tratarURL(document.getUrl()));
				HibernateDAO.getInstance().update(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateDAO.getInstance().closeSession();

	}

	private static void testLinks() {
		Set<Result> links = null;
		try {
			links = getLinks("postgre", 5, false);
		} catch (SearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (links != null) {
			for (Result result : links) {
				System.out.println(result);
			}
			System.out.println(links.size());
		}

	}

	public static Set<Result> getLinks(String searchStr, int maxBackLinks,
			boolean discardSameDomain) throws SearchException, IOException {
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
		se.setMaxResults(maxBackLinks);
		List<Result> results = se.search(searchStr);
		Set<Result> setURLS = new HashSet<Result>();
		for (Result result : results) {
			if (!WebDocument.discardUrl(result.getURL(), discardSameDomain,
					searchStr))
				setURLS.add(result);
		}
		return setURLS;
	}

}
