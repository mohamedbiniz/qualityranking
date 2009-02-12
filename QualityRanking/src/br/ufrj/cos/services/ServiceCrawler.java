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
	 * Nesse método, verificamos a condição que desejamos. Se a variável pausada
	 * valer true, isso nos indica que a thread deve dormir. Portanto, damos um
	 * wait() nela. Caso contrário, ela deve continuar.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void verificaPausa() throws InterruptedException {
		// Esse while é necessário pois threads estão sujeitas a spurious
		// wakeups, ou seja, elas podem acordar mesmo que nenhum notify tenha
		// sido dado.

		// Whiles diferentes podem ser usados para descrever condições
		// diferentes. Você também pode ter mais de uma condição no while
		// associada com um e. Por exemplo, no caso de um produtor/consumidor,
		// poderia ser while (!pausado && !fila.cheia()).

		// Nesse caso só temos uma condição, que é dormir quando pausado.
		while (pausado) {
			wait();
		}
	}

	/**
	 * Nesse método, permitimos a quem quer que use a impressora que controle
	 * sua thread. Definindo pausado como true, essa thread irá parar e esperar
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
		// para ela verificar sua condição. Nesse caso, sabemos que a thread
		// acordará, mas no caso de uma condição com várias alternativas, nem
		// sempre isso seria verdadeiro.
		if (!this.pausado)
			notifyAll();
	}

}
