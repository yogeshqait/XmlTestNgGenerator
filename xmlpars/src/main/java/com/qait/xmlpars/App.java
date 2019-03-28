package com.qait.xmlpars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class App {
	public static final String xmlFilePath = "Resources/myTemp.xml";
	public static final String InputFilePath = "Resources/file.txt";
	
	public List<String> readFile(String filepath) {
		List<String> mytests = new ArrayList<String>();
		try {
			BufferedReader bufReader = new BufferedReader(new FileReader(InputFilePath));
			String line = bufReader.readLine();
			while (line != null) {
				mytests.add(line);
				line = bufReader.readLine();
			}
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return mytests;
	}

	public void generate(List<String> myTests) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			Element root = document.createElement("suite");
			document.appendChild(root);
			root.setAttribute("verbose", "0");
			root.setAttribute("name", "Mexico Metrics Test");
			root.setAttribute("thread-count", "20");
			Element param = document.createElement("parameter");
			param.setAttribute("name", "browser");
			param.setAttribute("value", "firefox");

			root.appendChild(param);

			for (String item : myTests) {
				Element test = document.createElement("test");
				test.setAttribute("name", item);
				test.setAttribute("preserve-order", "true");
				test.setAttribute("enabled", "true");
				Element param1 = document.createElement("parameter");
				param1.setAttribute("name", "metric");
				param1.setAttribute("value", item);
				test.appendChild(param1);
				Element classes = document.createElement("classes");
				Element class1 = document.createElement("class");
				class1.setAttribute("name", "MX_New_Metric.Mexico_Metrics_Test");
				classes.appendChild(class1);
				test.appendChild(classes);
				root.appendChild(test);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://testng.org/testng-1.0.dtd");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));

			transformer.transform(domSource, streamResult);

			System.out.println("Done creating XML File");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerConfigurationException te) {
			te.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		App obj = new App();
		List<String> mytests = new ArrayList<String>();
		mytests = obj.readFile(InputFilePath);		
		obj.generate(mytests);
	}
}
