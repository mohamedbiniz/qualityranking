package trab01.ploter;

import java.util.TreeMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

/**
 * @author Fabricio
 * 
 */
public class Ploter {

	public static void print(TreeMap<Integer, Double>[][] resultados) {

		String[] tecnicasDeBusca = new String[] { "Boolean", "Full-text",
				"Full-Text Extended" };

		String[] campos = new String[] { "Titles", "Abstract", "Body" };

		ValueAxis axis1 = new NumberAxis("Recall (%)");
		ValueAxis axis2 = new NumberAxis("Precision (%)");

		StandardXYItemRenderer renderer = new StandardXYItemRenderer();

		DefaultTableXYDataset[] tableXYDataset = new DefaultTableXYDataset[3];

		JFrame[] f = new JFrame[3];

		XYPlot[] plot = new XYPlot[3];
		JFreeChart[] chart = new JFreeChart[3];
		ChartPanel[] panel = new ChartPanel[3];

		for (int i = 0; i < campos.length; i++) {// varia os campos
			f[i] = new JFrame("Resultados");
			f[i].setSize(640, 480);
			f[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			tableXYDataset[i] = new DefaultTableXYDataset();
			for (int j = 0; j < tecnicasDeBusca.length; j++) {// varia as
				// tecnicas

				XYSeries series = new XYSeries(tecnicasDeBusca[j], true, false);
				for (Integer recall : resultados[i][j].keySet()) {// varia os
					// pontos
					double revocacao = (double) recall;
					double precisao = resultados[i][j].get(recall);
					series.add(revocacao, precisao);
					if (i == 0 && j == 0) {
						System.out.println(String.format("%f\t%f", revocacao,
								precisao));
						;
					}

				}
				tableXYDataset[i].addSeries(series);

			}

			plot[i] = new XYPlot(tableXYDataset[i], axis1, axis2, renderer);
			chart[i] = new JFreeChart(plot[i]);
			chart[i].setTitle("Search in " + campos[i]);
			panel[i] = new ChartPanel(chart[i]);
			f[i].getContentPane().add(panel[i]);
			f[i].setVisible(true);
		}

	}

	public static void print(TreeMap<Integer, Double> resultados, String campo,
			String tecnicaDeBusca) {

		ValueAxis axis1 = new NumberAxis("Recall (%)");
		ValueAxis axis2 = new NumberAxis("Precision (%)");

		StandardXYItemRenderer renderer = new StandardXYItemRenderer();

		DefaultTableXYDataset tableXYDataset = null;

		JFrame f = new JFrame();

		XYPlot plot = null;
		JFreeChart chart = null;
		ChartPanel panel = null;

		f = new JFrame("Resultados");
		f.setSize(640, 480);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tableXYDataset = new DefaultTableXYDataset();

		XYSeries series = new XYSeries(tecnicaDeBusca, true, false);
		for (Integer recall : resultados.keySet()) {// varia os
			// pontos
			double revocacao = (double) recall;
			double precisao = resultados.get(recall);
			series.add(revocacao, precisao);

			System.out.println(String.format("%f\t%f", revocacao, precisao));
			;

		}
		tableXYDataset.addSeries(series);

		plot = new XYPlot(tableXYDataset, axis1, axis2, renderer);
		chart = new JFreeChart(plot);
		chart.setTitle("Search in " + campo);
		panel = new ChartPanel(chart);
		f.getContentPane().add(panel);
		f.setVisible(true);

	}
}
