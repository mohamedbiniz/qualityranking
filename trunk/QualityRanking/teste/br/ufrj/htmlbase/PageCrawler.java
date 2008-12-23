package br.ufrj.htmlbase;

import static br.ufrj.htmlbase.extractor.ContentExtractor.getContentExtracted;
import static br.ufrj.htmlbase.extractor.LinkExtractor.getLinks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import br.ufrj.htmlbase.extractor.metadata.MetadataCrawler;
import br.ufrj.htmlbase.extractor.metadata.MetadataList;
import br.ufrj.htmlbase.extractor.metadata.process.MetadataProcess;
import br.ufrj.htmlbase.io.MD5Hash;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Jan 22, 2006 Time: 3:49:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class PageCrawler {

	public static final String TEMP_PATH = (System.getProperty("user.home"))
			.replaceAll("\\\\", "/").concat("/Temp_Crawler/");

	private static Logger logger = Logger.getLogger(PageCrawler.class);

	private final long SCORE = 100;
	private final long VERSION = 1;

	// lista de urls existentes na pagina
	protected Collection urls;

	// lista de páginas filhas
	private Set<OutputLinkCrawler> agregatedLinks;

	// Lista de metadados que foram obtidos, a partir do Dublin core
	protected MetadataList metadata = new MetadataList();

	// conteudo da pagina sem links e figuras
	protected String content;

	// identificador unico gerado com o MD5 do conteudo
	protected long id;

	private long idDataSet;

	private long idPage;

	private long ordemDownload;

	// Url da pagina
	protected String url;

	private URL urlPath;

	// data do primeiro download
	protected int firstFetch;

	// data do proximo fecth
	protected int nextFetch;

	// pontuacao da pagina a partir de um algotimo de rank
	protected double score;

	// caminho para o arquivo fisicamente armazenado
	protected String path;

	// versao do fetch
	protected long version;

	// data do primeiro download
	protected Date dateCreate;

	// data do primeiro download
	protected Date lastModified;

	public boolean isAllOk() {
		return allOk;
	}

	public void setAllOk(boolean allOk) {
		this.allOk = allOk;
	}

	protected boolean allOk;

	public PageCrawler(OutputLinkCrawler link) throws IOException {
		this(link, true);
	}

	public PageCrawler(OutputLinkCrawler link, boolean download)
			throws IOException {
		this();
		String url = link.getUrl();

		setUrl(url);
		setDateCreate(new Date());
		setLastModified(new Date());
		setScore(SCORE);
		setVersion(VERSION);
		MD5Hash md5 = MD5Hash.digest(url);
		setId(md5.halfDigest());
		String path = makePath(url);

		setPath(path);

		setIdPage(link.getIdPage());
		if (download) {
			downloadFile(url, path);
		}

	}

	public PageCrawler() {
		setAgregatedLinks(new TreeSet<OutputLinkCrawler>());
	}

	private String makePath(String url) {
		try {
			// @TODO melhorar esse hardcode

			urlPath = new URL(url);
		} catch (MalformedURLException ex) {
			logger.debug(ex.getMessage());
		}

		String x = urlPath.getPath();
		String q = urlPath.getQuery();
		String host = urlPath.getHost();
		String newX = null;

		if (x.length() == 0 | x.equalsIgnoreCase("/")) {
			long name = getId();
			newX = name + ".htm";

		} else if (x.endsWith("/")) {

			x = x.substring(0, x.length() - 1);
			newX = x.concat(".htm");

		} else if (!x.contains(".")) {
			long name = getId();
			if (q == null)
				q = "";
			newX = x.concat("$" + name + "$" + q + ".htm");

		}

		/*
		 * HtmlBaseConfProvider provider = HtmlBaseConfProvider.getInstance();
		 * String pathCompleto = provider.getHome()+"/"+host+"/"+newX;
		 */

		String pathCompleto = TEMP_PATH + host + "/" + newX;

		return pathCompleto;
	}

	private void downloadFile(String url, String fileName) {

		logger.debug("");
		logger.debug(new Date() + " Iniciando download para " + url + "\n");
		InputStream is = null;

		try {

			urlPath = new URL(url);

			URLConnection connection = urlPath.openConnection();

			String contentType = connection.getContentType();

			if (contentType != null) {
				if (!contentType.contains("text")) {
					setAllOk(false);
					logger.debug(new Date()
							+ " Formato de arquivo nao aceito :: "
							+ contentType);
				} else {
					logger.debug(new Date() + " Formato de arquivo valido :: "
							+ contentType);
					logger.debug(new Date() + " Abrindo Streams");

					is = urlPath.openStream();

					logger.debug(new Date() + " Streams aberta");
					File outputFile = new File(fileName);

					if (!outputFile.exists()) {

						setAllOk(true);

						outputFile.getParentFile().mkdirs();

						FileOutputStream fos = new FileOutputStream(fileName);

						int c;

						while ((c = is.read()) != -1) {
							fos.write(c);
						}

						is.close();
						fos.close();

					} else {
						logger.debug(new Date() + " Arquivo ja existente :: "
								+ outputFile.getPath());

					}

				}
			}

		} catch (MalformedURLException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}

	}

	public boolean process() {

		if (isAllOk()) {

			setContent(processContent());
			setUrls(processLinks());
			// processMetadata();
		}
		return isAllOk();
	}

	private void processMetadata() {

		MetadataProcess meta = new MetadataProcess();

		meta.execute(this);

	}

	private String processContent() {
		return getContentExtracted(path);
	}

	private Collection processLinks() {

		return getLinks(path, urlPath.getHost(), idDataSet);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public Collection getUrls() {
		return urls;
	}

	public void setUrls(Collection urls) {
		this.urls = urls;
	}

	public MetadataList getMetadata() {
		return metadata;
	}

	public void setMetadata(MetadataList metadata) {
		this.metadata = metadata;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFirstFetch() {
		return firstFetch;
	}

	public void setFirstFetch(int firstFetch) {
		this.firstFetch = firstFetch;
	}

	public int getNextFetch() {
		return nextFetch;
	}

	public void setNextFetch(int nextFetch) {
		this.nextFetch = nextFetch;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void make(File sourcePage) {

	}

	public void addMEtadata(MetadataCrawler m) {
		metadata.add(m);

	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getPathPage() {
		return path;
	}

	public void setPathPage(String p) {
		path = p;
	}

	/**
	 * @return the idDataSet
	 */
	public long getIdDataSet() {
		return idDataSet;
	}

	/**
	 * @param idDataSet
	 *            the idDataSet to set
	 */
	public void setIdDataSet(long idDataSet) {
		this.idDataSet = idDataSet;
	}

	/**
	 * @return the idPage
	 */
	public long getIdPage() {
		return idPage;
	}

	/**
	 * @param idPage
	 *            the idPage to set
	 */
	public void setIdPage(long idPage) {
		this.idPage = idPage;
	}

	@Override
	public String toString() {

		return String.format("%d\t%s\t%d", getId(), getUrl(), getIdPage());
	}

	/**
	 * @return the ordemDownload
	 */
	public long getOrdemDownload() {
		return ordemDownload;
	}

	/**
	 * @param ordemDownload
	 *            the ordemDownload to set
	 */
	public void setOrdemDownload(long ordemDownload) {
		this.ordemDownload = ordemDownload;
	}

	/**
	 * @return the agregatedLinks
	 */
	public Set<OutputLinkCrawler> getAgregatedLinks() {
		return agregatedLinks;
	}

	/**
	 * @param agregatedLinks
	 *            the agregatedLinks to set
	 */
	public void setAgregatedLinks(Set<OutputLinkCrawler> agregatedLinks) {
		this.agregatedLinks = agregatedLinks;
	}

	/**
	 * @param link
	 * @return pages
	 */
	public boolean addAgregatedLink(OutputLinkCrawler link) {
		return getAgregatedLinks().add(link);
	}

	/**
	 * @param page
	 * @return pages
	 */
	public boolean removeAgregatedLink(OutputLinkCrawler link) {
		return getAgregatedLinks().remove(link);
	}

}
