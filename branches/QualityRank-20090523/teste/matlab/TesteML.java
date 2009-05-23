package matlab;

import java.io.IOException;

import br.ufrj.cos.matlab.JobSend;
import br.ufrj.cos.matlab.client.MatClient;

public class TesteML {
	// No Servidor Matlab rodar:
	// path('%workspace%/QualityRanking/bin',path);
	// javaaddpath %workspace%/QualityRanking/bin;
	// ms = br.ufrj.cos.bri.matlab.server.MatServer;
	// ms.body();

	public static void main(String[] args0) throws Exception {
		teste01();
	}

	private static void teste01() {
		int tl = 5;
		int qd = 3;
		double cdq1[] = { 2, 3, 4 };

		double qds[] = { 0.1, 0.1, 0.5 };

		JobSend jobSend = new JobSend("fuzzyDocument", qd, cdq1, qds);
		MatClient c = null;
		try {
			c = MatClient.getInstance();
			c.createJob(jobSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
