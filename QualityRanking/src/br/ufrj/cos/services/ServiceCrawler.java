/**
 * 
 */
package br.ufrj.cos.services;

import java.util.Collection;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.htmlbase.Capture;

/**
 * @author Fabricio
 * 
 */
public class ServiceCrawler extends Service {

	private static final long PAUSA_CRAWLER = 30000;

	Capture crawler;

	private boolean pausado;

	private int qtdSubThread = 10;

	public ServiceCrawler() {
		// super(DataSet.STATUS_CRAWLING, DataSet.STATUS_AUTOMATIC_EVALUATION,
		// PAUSA_CRAWLER);
		super(DataSet.STATUS_SEARCH, DataSet.STATUS_MANUAL_EVALUATION,
				DataSet.CRAWLER_QUALITYFUZZY, PAUSA_CRAWLER);
		crawler = new Capture();
	}

	@Override
	protected void execute(DataSet dataSet) throws Exception {

		crawler.execute(dataSet, this, qtdSubThread);
		setPausado(true);
		verificaPausa();
		Collection<Document> documents = crawler.exportPages(dataSet, getDao());
		extractMetadatasOfDocuments(documents);
		derivacaoMetadados(dataSet);
		fuzzyDataSet(dataSet);
	}

	private void extractMetadatasOfDocuments(Collection<Document> documents)
			throws Exception {
		for (Document document : documents) {
			extractMetadatas(document);
		}
	}

	/**
	 * Nesse m�todo, verificamos a condi��o que desejamos. Se a vari�vel pausada
	 * valer true, isso nos indica que a thread deve dormir. Portanto, damos um
	 * wait() nela. Caso contr�rio, ela deve continuar.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void verificaPausa() throws InterruptedException {
		// Esse while � necess�rio pois threads est�o sujeitas a spurious
		// wakeups, ou seja, elas podem acordar mesmo que nenhum notify tenha
		// sido dado.

		// Whiles diferentes podem ser usados para descrever condi��es
		// diferentes. Voc� tamb�m pode ter mais de uma condi��o no while
		// associada com um e. Por exemplo, no caso de um produtor/consumidor,
		// poderia ser while (!pausado && !fila.cheia()).

		// Nesse caso s� temos uma condi��o, que � dormir quando pausado.
		while (pausado) {
			wait();
		}
	}

	/**
	 * Nesse m�todo, permitimos a quem quer que use a impressora que controle
	 * sua thread. Definindo pausado como true, essa thread ir� parar e esperar
	 * indefinidamente. Caso pausado seja definido como false, a impressora
	 * volta a imprimir.
	 * 
	 * @param pausado
	 *            True para pausar, false para continuar.
	 */
	public synchronized void setPausado(boolean pausado) {
		// if (pausado == false) {
		// qtdSubThread--;
		// if (qtdSubThread == 0)
		// this.pausado = pausado;
		// } else {
		// qtdSubThread = 4;
		// this.pausado = pausado;
		// }
		this.pausado = pausado;

		// Caso pausado seja definido como false, acordamos a thread e pedimos
		// para ela verificar sua condi��o. Nesse caso, sabemos que a thread
		// acordar�, mas no caso de uma condi��o com v�rias alternativas, nem
		// sempre isso seria verdadeiro.
		if (!this.pausado)
			notifyAll();
	}

}
