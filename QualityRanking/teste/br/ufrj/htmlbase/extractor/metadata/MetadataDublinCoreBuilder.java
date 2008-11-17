package br.ufrj.htmlbase.extractor.metadata;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import br.ufrj.htmlbase.PageCrawler;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 10, 2006 Time: 3:54:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataDublinCoreBuilder extends MetadataBuilder {

	private static Logger logger = Logger
			.getLogger(MetadataDublinCoreBuilder.class);

	public void makeMetadata(PageCrawler page) {

		URLConnection connection = null;

		try {
			URL url = new URL(page.getUrl());

			connection = url.openConnection();
			connection.setUseCaches(false);

		}

		catch (IOException ex) {
			logger.fatal(new java.util.Date()
					+ " URL nao pode ser acessada :: " + page);
		}

		long dataCriacao = connection.getDate();
		long lastModified = connection.getLastModified();
		String contentType = connection.getContentType();
		String encoded = connection.getContentEncoding();

		String title = processTitle(page);

		MetadataList list = page.getMetadata();

		if (dataCriacao != 0) {
			Locale locale = Locale.ENGLISH;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
			String s = formatter.format(new Date(dataCriacao));
			list.add(new MetadataCrawler(MetadataCrawler.DATA, s));

			logger.debug(new java.util.Date() + " Date :: " + s);

		}

		if (lastModified != 0) {
			Locale locale = Locale.ENGLISH;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
			String s = formatter.format(new Date(lastModified));
			list.add(new MetadataCrawler(MetadataCrawler.LAST_MODIFIED, s));

			logger.debug(new java.util.Date() + " Last Modified :: " + s);
		}

		if (contentType != null) {
			list.add(new MetadataCrawler(MetadataCrawler.CONTENT_TYPE,
					contentType));

			logger.debug(new java.util.Date() + " Type :: " + contentType);
		}

		if (encoded != null) {
			list.add(new MetadataCrawler(MetadataCrawler.ENCODED, contentType));
			logger.debug(new java.util.Date() + " Encoded :: " + encoded);
		}

		if (title != null) {
			list.add(new MetadataCrawler(MetadataCrawler.TITLE, title));
			logger.debug(new java.util.Date() + " Title :: " + title); // output
			// the
			// modified
			// HTML
		}

		page.setMetadata(list);
		if (sucessor != null) {
			sucessor.makeMetadata(page);
		}

	}

	private String processTitle(PageCrawler p) {
		Parser parser = null;
		String title = "";
		try {

			// TODO melhorar esse hardcode, esta causando erro, precisa garantir
			// o uso do mesmo path criado na page
			String path = p.getPath();

			parser = new Parser(path);
			NodeList nl = parser.parse(new TagNameFilter("TITLE")); // here is
			// your two
			// node list

			if (nl.size() != 0) {
				TitleTag t = (TitleTag) nl.elementAt(0);
				title = t.getTitle();
			}

		} catch (ParserException e) {
			logger.fatal(e); // To change body of catch statement use File |
			// Settings | File Templates.
		}
		return title;
	}

}
