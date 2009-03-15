import java.util.List;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.HibernateDAO;

/**
 * 
 */

/**
 * @author Fabricio
 * 
 */
public class CorrigirURLs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
}
