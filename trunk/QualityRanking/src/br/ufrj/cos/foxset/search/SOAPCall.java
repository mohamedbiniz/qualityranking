/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author Heraldo
 */
public class SOAPCall {

	private String endpoint;
	private SOAPConnection connection;
	private SOAPMessage message;
	private SOAPElement bodyElement;

	static {
		System
				.setProperty("javax.xml.parsers.DocumentBuilderFactory",
						"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");

	}

	public SOAPCall(String endpoint, String localName) throws SOAPException {
		this(endpoint, localName, null, null);
	}

	public SOAPCall(String endpoint, String localName, String prefix, String uri)
			throws SOAPException {

		this.endpoint = endpoint;

		SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory
				.newInstance();
		connection = soapConnFactory.createConnection();

		MessageFactory messageFactory = MessageFactory.newInstance();
		message = messageFactory.createMessage();

		SOAPPart soapPart = message.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("xsd",
				"http://www.w3.org/2001/XMLSchema");
		envelope.addNamespaceDeclaration("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("enc",
				"http://schemas.xmlsoap.org/soap/encoding/");
		envelope.addNamespaceDeclaration("env",
				"http://schemas.xmlsoap.org/soap/envelop/");
		envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

		SOAPBody body = envelope.getBody();
		if (prefix == null && uri == null) {
			bodyElement = body.addChildElement(envelope.createName(localName));
		} else {
			bodyElement = body.addChildElement(envelope.createName(localName,
					prefix, uri));
		}
	}

	public Document call() throws SOAPException,
			TransformerConfigurationException, TransformerException,
			ParserConfigurationException, SAXException, IOException {

		message.saveChanges();
		SOAPMessage response = connection.call(message, endpoint);

		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		Source sourceContent = response.getSOAPPart().getContent();
		StreamResult streamResult = new StreamResult(writer);
		transformer.transform(sourceContent, streamResult);
		String content = writer.getBuffer().toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		System.out.println(factory.getClass().getName());
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(content)));

		connection.close();
		return doc;
	}

	public SOAPElement getBodyElement() {
		return bodyElement;
	}
}
