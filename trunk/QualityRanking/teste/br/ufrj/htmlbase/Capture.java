/*
 * Capture.java
 *
 * Created on March 15, 2006, 5:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.ufrj.htmlbase;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.cos.bri.bean.Document;
import br.ufrj.cos.bri.bean.SeedDocument;
import br.ufrj.cos.bri.db.HibernateDAO;
import br.ufrj.cos.bri.services.ServiceCrawler;
import br.ufrj.htmlbase.db.hibernate.PageHibernateImpl;

public class Capture {

	public static void main(String[] args) {
		int numWorkers = 1;
		int threadPoolSize = 1;
		try {
			HibernateDAO dao = HibernateDAO.getInstance();

			ExecutorService tpes = Executors.newFixedThreadPool(threadPoolSize);

			DataSet dataSet = (DataSet) dao
					.loadById(DataSet.class, new Long(1));

			HtmlBase[] workers = new HtmlBase[numWorkers];

			for (int i = 0; i < numWorkers; i++) {

				workers[i] = new HtmlBase(i, dataSet);

				tpes.execute(workers[i]);
			}
			tpes.shutdown();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deletarPastaTemp(PageCrawler.TEMP_PATH);

	}

	public void execute(DataSet dataSet, ServiceCrawler serviceCrawler, int j)
			throws Exception {
		int numWorkers = j;
		int threadPoolSize = j;
		try {

			deletarPastaTemp(PageCrawler.TEMP_PATH);

			HibernateDAO.getInstance();

			insertSeedsLinks(dataSet.getSeedDocuments());

			ExecutorService tpes = Executors.newFixedThreadPool(threadPoolSize);

			HtmlBase[] workers = new HtmlBase[numWorkers];

			for (int i = 0; i < numWorkers; i++) {

				workers[i] = new HtmlBase(i, dataSet, serviceCrawler);

				tpes.execute(workers[i]);
			}
			tpes.shutdown();
		} catch (Exception e) {
			throw e;
		} finally {

			deletarPastaTemp(PageCrawler.TEMP_PATH);
		}

	}

	private void insertSeedsLinks(Collection<SeedDocument> seedDocuments) {
		PageHibernateImpl pageDao = new PageHibernateImpl();
		pageDao.insertSeedsLinks(seedDocuments);
	}

	private static void deletarPastaTemp(String tempPath) {
		File dir = new File(tempPath);
		deleteDir(dir);
	}

	private static boolean deleteDir(File dir) {
		// Deletes all files and subdirectories under dir.
		// Returns true if all deletions were successful.
		// If a deletion fails, the method stops attempting to delete
		// and returns false.

		if (dir.exists() && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();

	}

	public Collection<Document> exportPages(DataSet dataSet, HibernateDAO dao)
			throws Exception {
		PageHibernateImpl pageDao = new PageHibernateImpl();
		return pageDao.exportPages(dataSet, dao);

	}
}
