import java.awt.Component;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;

import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.matlab.exception.MatLabException;
import br.ufrj.cos.services.Service;

/**
 * 
 */

/**
 * @author Fabricio
 * 
 */
public class HelperScoreFinalDinamic {

	/**
	 * @param idDataSet
	 * @param weightREP
	 * @param weightCOM
	 * @param weightTIM
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static String generateScores(Long idDataSet, int weightREP,
			int weightCOM, int weightTIM, Component component)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		HashMap<String, Integer> mapWeights = putWeights(weightREP, weightCOM,
				weightTIM);

		DataSet dataSet = (DataSet) HibernateDAO.getInstance().loadById(
				DataSet.class, idDataSet);

		if (dataSet != null) {
			return reFuzzyDataSet(dataSet, mapWeights, component);
		}
		return "";
	}

	public static String reFuzzyDataSet(DataSet dataSet,
			HashMap<String, Integer> mapWeights, Component component)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		String result = "";
		Collection<ContextQualityDimensionWeight> listCQDWeights = HelperAcessDB
				.loadContextQualityDimensionWeights(dataSet);

		int qtdQualityDimensions = HelperAcessDB.loadQualityDimensions(dataSet)
				.size();

		double contextWeights[] = getWeights(listCQDWeights, mapWeights);

		int o = JOptionPane.OK_OPTION;
		Collection<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		Set<String> urlsIreval = HelperAcessDB.loadUrlsValidasFromIreval();
		for (Document document : documents) {
			if (!urlsIreval.contains(document.getUrl()))
				continue;
			double[] qds = HelperAcessDB
					.loadDocumentQualityDimensionScores(document);

			Double documentScore = null;
			while (documentScore == null) {
				try {
					documentScore = Service.fuzzy(qtdQualityDimensions, qds,
							contextWeights);
				} catch (MatLabException mle) {
					System.err.println(mle.getMessage());
					o = JOptionPane
							.showConfirmDialog(
									component,
									"Possivelmente o servi�o do MatLab n�o est� iniciado ou houve erro no mesmo!\n"
											+ "� necess�rio (re)iniciar o servi�o do MatLab antes de continuar!\n"
											+ "Deseja continuar?",
									"Erro no MatLab",
									JOptionPane.WARNING_MESSAGE);
					if (o != JOptionPane.OK_OPTION) {
						break;
					}
				}
			}

			if (o != JOptionPane.OK_OPTION) {
				break;
			}

			result = result
					+ String.format("%f\n", documentScore.doubleValue());
		}
		System.out.println(result);
		return result;
	}

	/**
	 * @return
	 */
	private static HashMap<String, Integer> putWeights(int weightREP,
			int weightCOM, int weightTIM) {
		HashMap<String, Integer> mapWeights = new HashMap<String, Integer>();
		mapWeights.put(QualityDimension.REP, new Integer(weightREP));
		mapWeights.put(QualityDimension.COM, new Integer(weightCOM));
		mapWeights.put(QualityDimension.TIM, new Integer(weightTIM));
		return mapWeights;
	}

	private static double[] getWeights(
			Collection<ContextQualityDimensionWeight> listCQDWeights,
			HashMap<String, Integer> mapWeights) {
		double[] weights = new double[listCQDWeights.size()];
		int i = 0;
		for (ContextQualityDimensionWeight contextQualityDimensionWeight : listCQDWeights) {
			weights[i++] = mapWeights.get(
					contextQualityDimensionWeight.getQualityDimension()
							.getCodeStr()).doubleValue();
		}
		return weights;
	}
}
