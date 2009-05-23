package br.ufrj.htmlbase.db.hsqldb;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 1, 2006
 * Time: 10:40:46 PM
 * To change this template use File | Settings | File Templates.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.htmlbase.OutputLinkCrawler;
import br.ufrj.htmlbase.PageCrawler;
import br.ufrj.htmlbase.db.PageBD;
import br.ufrj.htmlbase.extractor.metadata.MetadataCrawler;

public class PageBDImpl implements PageBD {

	private static Logger logger = Logger.getLogger(PageBDImpl.class);

	Connection conn;

	public PageBDImpl(String db_file_name_prefix) throws Exception { // note
		// more
		// general
		// exception
		createConnection(db_file_name_prefix); // password

	}

	private synchronized void createConnection(final String db_file_name_prefix)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SQLException {

		Class.forName("org.hsqldb.jdbcDriver").newInstance();

		conn = DriverManager.getConnection(
				"jdbc:hsqldb:" + db_file_name_prefix, "sa", ""); // password
	}

	public void shutdown() throws SQLException {

		Statement st = conn.createStatement();
		st.execute("SHUTDOWN");
		conn.close(); // if there are no other open connection
	}

	// use for SQL command SELECT
	public synchronized Collection getLinksTop(int top) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(); // statement objects can be
		// reused with

		rs = st.executeQuery("SELECT TOP " + top
				+ " id, url, is_visited FROM LINKS where is_visited = false"); // run
		// the
		// query

		Collection lista = new ArrayList();
		while (rs.next()) {
			OutputLinkCrawler o = new OutputLinkCrawler();

			o.setIdTest(rs.getLong(1));
			o.setUrl(rs.getString(2));

			lista.add(o);

		}

		st.close(); // NOTE!! if you close a statement the associated
		// ResultSet is

		return lista;
	}

	public synchronized void save(PageCrawler p, DataSet dataSet)
			throws SQLException {

		PreparedStatement stmt = conn
				.prepareStatement("INSERT INTO PAGE(IdPagina, content, url, path_page, IdDataSet) values(?,?,?,?,?)");
		stmt.setLong(1, p.getId());
		stmt.setString(2, p.getContent());
		stmt.setString(3, p.getUrl());
		stmt.setString(4, p.getPath());
		stmt.setLong(5, dataSet.getId());

		int i = stmt.executeUpdate(); // run the query

		if (i > 0) {
			logger.debug(new java.util.Date() + " URL persistida :: "
					+ p.getUrl());
		} else {
			logger.debug(" Problemas na persistencia da URL :: " + p.getUrl());
		}

		stmt = conn
				.prepareStatement("INSERT INTO LINKS(id, IdPagina, url, domain, is_visited) values(?,?,?,?,?)");

		Collection urls = p.getUrls();

		for (Object url : urls) {

			OutputLinkCrawler u = (OutputLinkCrawler) url;

			stmt.setLong(1, u.getIdTest());
			stmt.setLong(2, p.getId());
			stmt.setString(3, u.getUrl());
			stmt.setString(4, u.getDomain());
			stmt.setBoolean(5, false);

			i = stmt.executeUpdate(); // run the query

		}

		if (i > 0) {
			logger.debug(new java.util.Date()
					+ " Links persistidos com sucesso : " + urls.size());
		} else {
			logger.debug(new java.util.Date()
					+ " Problemas na persistencia dos links : ");
		}

		Collection metadatas = p.getMetadata().getMetadatas();
		stmt = conn
				.prepareStatement("INSERT INTO METADATA(id, IdPagina, property, value) values(?,?,?,?)");

		for (Object metadata : metadatas) {
			MetadataCrawler m = (MetadataCrawler) metadata;
			stmt.setLong(1, m.getId());
			stmt.setLong(2, p.getId());
			stmt.setString(3, m.getName());
			stmt.setString(4, m.getValue());

			i = stmt.executeUpdate();

		}

		stmt.close();

	}

	// TODO melhorar implementacao
	public void updateLinks(Collection c, DataSet dataSet) throws SQLException {

		Statement stmt = conn.createStatement();

		String update;
		update = "UPDATE LINKS SET IS_VISITED = true where ID in (";

		for (Object o : c) {

			Long m = (Long) o;

			update = update.concat(m.toString() + ",");
		}

		update = update.substring(0, update.length() - 1);

		update = update.concat(")");

		stmt.executeUpdate(update);

		stmt.close();

	}

	public static void dump(ResultSet rs) throws SQLException {

		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;

		for (; rs.next();) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is
				// indexed

				// with 1 not 0
				logger.info(o.toString() + " ");
			}

			logger.info(" ");
		}
	} // void dump( ResultSet rs )

} // class Testdb
