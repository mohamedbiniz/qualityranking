package br.ufrj.cos.bri.matlab.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.ufrj.cos.bri.matlab.JobSend;

/**
 * @author <a href="fabriciorsf@cos.ufrj.br">Fabricio Raphael</a>
 * 
 */
public class MatServer {

	MatlabControl mc = new MatlabControl();

	public void body() throws IOException {
		class Caller extends Thread {
			public void run() {
				try {
					body2();
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println(e);
				}
			}

			public void body2() throws IOException {
				ServerSocket serverSocket = null;
				boolean listening = true;
				try {
					serverSocket = new ServerSocket(4444);
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println(e);
					System.exit(1);
				}

				while (listening) {
					new workerThread(serverSocket.accept()).run();
					System.gc();
				}
				serverSocket.close();
			} // end_run
		} // end_caller
		Caller c = new Caller();
		c.start();
	}

	public class workerThread {
		private Socket socket = null;

		public workerThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				ObjectOutputStream out = new ObjectOutputStream(socket
						.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket
						.getInputStream());
				Object inputObj, outputObj;
				while ((inputObj = in.readObject()) != null) {
					outputObj = processInput(inputObj);
					out.writeObject(outputObj);
					System.gc();
					if (outputObj.equals("bye"))
						break;
				}

				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				// e.printStackTrace();
				System.err.println(e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private String processInput(Object req) throws InterruptedException,
				IOException {
			String rez = "Invalid Request";
			if (req.toString().equals("bye")) {
				rez = "bye";
			} else if (req instanceof JobSend) {
				JobSend job = (JobSend) req;
				rez = Double.toString(executeFuzzy5(job.getQd(), job.getCdq(),
						job.getQds()));
			}
			return rez;
		}

		private static final String NAME_FILE_RESULTS = "resultados.txt";

		private Double executeFuzzy5(int qd, double[] cdq, double[] qds)
				throws InterruptedException, IOException {
			int tl = 5;

			return executeFuzzy(tl, qd, cdq, qds);
		}

		private Double executeFuzzy(int tl, int qd, double[] cdq, double[] qds)
				throws InterruptedException, IOException {
			String function = "fuzzyDocument";
			File file = new File(NAME_FILE_RESULTS);
			file.delete();
			Object[] parameters = constuctParameters(tl, qd, cdq, qds, file
					.getAbsolutePath());
			mc.blockingFeval(function, parameters);

			while (!file.exists()) {
				Thread.sleep(100);
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String result = reader.readLine();
			reader.close();
			Double d = Double.parseDouble(result);
			return d;
		}

		private Object[] constuctParameters(int tl, int qd, double[] cdq,
				double[] qds, String nameFileResults) {

			Object[] parameters = { tl, qd, cdq, qds, nameFileResults };
			return parameters;
		}
	}
}
