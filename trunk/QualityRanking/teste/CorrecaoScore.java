import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.Metadata;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.enume.MetadataType;
import br.ufrj.cos.services.Service;
import br.ufrj.cos.services.process.MetadataExtract;

public class CorrecaoScore {

	private static Date now = new Date();

	private static HibernateDAO dao = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		setDao(HibernateDAO.getInstance());
		getDao().openSession();
		DataSet dataSet = (DataSet) HibernateDAO.getInstance().loadById(
				DataSet.class, new Long(578));
		try {
			// Service.fuzzyDataSet(dataSet);
			// corrigirMetadataDate(dataSet);
			System.gc();
			int[] a = count(dataSet);
			System.out.println(a[0]);
			System.out.println(a[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateDAO.getInstance().closeSession();
		// String l = "I";
		// for (int i = 11; i < 556; i++) {
		// System.out
		// .println(String
		// .format(
		// "=SE(%s%d<C2;B2;SE(%s%d<C3;B3;SE(%s%d<C4;B4;SE(%s%d<C5;B5;B6))))",
		// l, i, l, i, l, i, l, i));
		// }

	}

	private static int[] count(DataSet dataSet) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			ParseException {

		int a[] = new int[2];
		a[0] = 0;
		a[1] = 0;
		Set<String> urlsIreval = HelperAcessDB.loadUrlsValidasFromIreval();

		List<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		for (Document document : documents) {

			Metadata metadata = HelperAcessDB.loadMetadata(document,
					MetadataType.DATE);

			DateFormat dateFormat = new SimpleDateFormat(
					MetadataExtract.DATE_FORMAT);
			if (metadata != null) {
				Date dataAtualizacao = dateFormat.parse(new String(metadata
						.getValue()));
				if (dataAtualizacao.after(getMinDate())) {
					a[0]++;
					if (urlsIreval.contains(document.getUrl()))
						a[1]++;
				}
			}

		}
		return a;
	}

	private static Date getMinDate() {
		Calendar c = new GregorianCalendar();
		c.setTime(new Date(0));
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	private static void corrigirMetadataDate(DataSet dataSet) throws Exception {
		DocumentQualityDimension documentQualityDimension = null;

		List<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		Collection<QualityDimension> qualityDimensions = HelperAcessDB
				.loadQualityDimensions(dataSet);

		for (Document document : documents) {
			System.gc();
			MetadataExtract metadataExtract = new MetadataExtract(document
					.getUrl());
			Metadata metadata = HelperAcessDB.loadMetadata(document,
					MetadataType.DATE);

			metadata.setValue(metadataExtract.extract().get(MetadataType.DATE));
			getDao().update(metadata);

			for (QualityDimension qualityDimension : qualityDimensions) {
				documentQualityDimension = HelperAcessDB
						.loadDocumentQualityDimension(document,
								qualityDimension);

				String code = qualityDimension.getCodeStr();
				if (code.equals(QualityDimension.TIM)) {
					double score = 0;
					score = getTimeliness(document, metadata);
					documentQualityDimension.setScore(new BigDecimal(score));
					getDao().update(documentQualityDimension);
				}

			}

		}
	}

	private static double getTimeliness(Document document, Metadata metadata) {
		double score = 0;
		byte[] metadataValue = metadata.getValue();
		if (metadataValue != null) {
			DateFormat dateFormat = new SimpleDateFormat(
					MetadataExtract.DATE_FORMAT);
			Date lastModified = null;
			try {
				lastModified = dateFormat.parse(new String(metadataValue));
			} catch (ParseException e) {
				System.err.println(String.format(
						"Este documento (%s) não possui o metadado, "
								+ "ou o mesmo não está no formato correto, "
								+ "para calcular o timeliness", document
								.toString()));
			}
			if (lastModified != null) {
				double diffDates = calcDiffDays(getNow(), lastModified);
				double quo = (diffDates + 1);
				score = 1 / (quo > 1 ? quo : 1);
			}
		}
		return score;
	}

	private static double calcDiffDays(Date dateEnd, Date dateInit) {
		// Creates two calendars instances
		Calendar calInit = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();

		// Set the date for both of the calendar instance
		calInit.setTime(dateInit);
		calEnd.setTime(dateEnd);

		// Get the represented date in milliseconds
		long milisInit = calInit.getTimeInMillis();
		long milisEnd = calEnd.getTimeInMillis();

		// Calculate difference in milliseconds
		double diff = milisEnd - milisInit;

		//
		// // Calculate difference in seconds
		// double diffSeconds = diff / 1000;
		//
		// // Calculate difference in minutes
		// double diffMinutes = diff / (60 * 1000);
		//
		// // Calculate difference in hours
		// double diffHours = diff / (60 * 60 * 1000);

		// Calculate difference in days
		double diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}

	public static Date getNow() {
		return now;
	}

	public static void setNow(Date now) {
		CorrecaoScore.now = now;
	}

	public static HibernateDAO getDao() {
		return dao;
	}

	public static void setDao(HibernateDAO dao) {
		CorrecaoScore.dao = dao;
	}

}
