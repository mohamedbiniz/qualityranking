package br.ufrj.htmlbase.db;

import java.sql.SQLException;
import java.util.Collection;

import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.htmlbase.PageCrawler;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 15, 2006 Time: 9:58:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PageBD {
	void shutdown() throws SQLException;

	Collection getLinksTop(int top) throws SQLException;

	void save(PageCrawler p, DataSet dataSet) throws SQLException,
			java.sql.BatchUpdateException;

	void updateLinks(Collection c) throws SQLException,
			java.sql.BatchUpdateException;

}
