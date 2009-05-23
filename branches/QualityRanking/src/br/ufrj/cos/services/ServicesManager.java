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
		int qtdServices = 3;
		Service[] services = new Service[qtdServices];
		services[0] = new ServiceCrawler();

		services[1] = new ServiceSearchQualityFuzzy();
		services[2] = new ServiceSearchPofN();

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
