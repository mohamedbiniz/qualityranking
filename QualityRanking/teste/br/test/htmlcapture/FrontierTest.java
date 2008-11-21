package br.test.htmlcapture;

import junit.framework.TestCase;
import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.htmlbase.OutputLinkCrawler;
import br.ufrj.htmlbase.exception.HtmlBaseException;
import br.ufrj.htmlbase.frontier.Frontier;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 11, 2006 Time: 8:08:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FrontierTest extends TestCase {

	private DataSet dataSet;

	public FrontierTest(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	public void testLinks() {

		try {

			for (;;) {
				OutputLinkCrawler link = Frontier.getInstance().getNextURL();
				Frontier.getInstance().testeFim(dataSet);
			}

		} catch (HtmlBaseException e) {
			notifyAll();
			e.printStackTrace(); // To change body of catch statement use
			// File | Settings | File Templates.
		}

	}

}
