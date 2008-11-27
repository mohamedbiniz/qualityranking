/**
 * 
 */
package br.ufrj.cos.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Fabricio
 * 
 */
public class ServicesManager {

	public void execute() {
		int qtdServices = 2;
		Service[] services = new Service[qtdServices];
		ServiceCrawler serviceCrawler;
		// ServiceAutomaticEvaluation serviceAvaliacaoAutomatica;
		serviceCrawler = new ServiceCrawler();
		// serviceAvaliacaoAutomatica = new ServiceAutomaticEvaluation();
		services[0] = serviceCrawler;
		// services[1] = serviceAvaliacaoAutomatica;
		ServiceSearch serviceSearch;
		serviceSearch = new ServiceSearch();
		services[1] = serviceSearch;

		try {

			ExecutorService tpes = Executors.newFixedThreadPool(6);

			for (int i = 0; i < qtdServices; i++) {

				tpes.execute(services[i]);
			}
			while (true)
				;
			// tpes.shutdown();
			// serviceCrawler.run();
			// serviceAvaliacaoAutomatica.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
