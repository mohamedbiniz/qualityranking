/**
 * 
 */
package br.ufrj.cos.matlab.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.ufrj.cos.matlab.JobSend;

/**
 * @author <a href="fabriciorsf@cos.ufrj.br">Fabricio Raphael</a>
 * 
 */
public class MatClient {

	private static final String HOST_DEFAULT = "localhost";

	private static final int PORT_DEFAULT = 4444;

	private Socket sock;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Object sendRequest, fromServer;

	private static MatClient matClient;

	public static MatClient getInstance() throws IOException {
		if (matClient == null)
			matClient = new MatClient();
		return matClient;
	}

	public static MatClient getInstance(String host, int port)
			throws IOException {
		if (matClient == null)
			matClient = new MatClient(host, port);
		return matClient;
	}

	private MatClient() throws IOException {
		this(HOST_DEFAULT, PORT_DEFAULT);
	}

	private MatClient(String host, int port) throws IOException {
		sock = null;
		out = null;
		in = null;
		sendRequest = "";
		fromServer = "";
		try {
			sock = new Socket(host, port);
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
		} catch (UnknownHostException hue) {
			System.err.println("Host not found: " + host + ":" + port);
			// System.exit(1);
			throw hue;
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.err
					.println("The service of MatLab possibly is not running!");
			// System.exit(1);
			throw ioe;
		}
	}

	public void finishJob() throws IOException {
		out.close();
		in.close();
		sock.close();
	}

	public double createJob(JobSend job) throws IOException,
			NumberFormatException, ClassNotFoundException {
		sendRequest = job;
		// uncomment for debugging purposes
		// System.out.println("Send Matlab request" );
		double rez = 0.0;
		try {
			out.writeObject(sendRequest);

			while ((fromServer = in.readObject()) != null) {
				// comment this out when not debugging
				if (fromServer instanceof String) {
					String fromServerStr = (String) fromServer;

					System.out.println("server: " + fromServerStr);
					if (fromServerStr.startsWith("bye")) {
						finishJob();
						break;
					} else {
						// process answer from server
						if (sendRequest instanceof JobSend) {
							JobSend jobSend = (JobSend) sendRequest;
							if (jobSend.getFunction().equals("fuzzyDocument"))
								rez = Double.parseDouble(fromServerStr);
							break;
						}

					}
				}
			}
		} catch (SocketException se) {
			matClient = null;
			se.printStackTrace();
		}
		return rez;
	}
}
