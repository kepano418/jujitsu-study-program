/*
 * SMILParser
 *
 * Version 1.0
 *
 * Programmer: Jesse Louderback
 * 
 */
package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import android.util.Log;

public class Parser {
	private static String fileLoc;
	private Map<String, ArrayList<Map<String, String>>> bigList = new Hashtable<String, ArrayList<Map<String, String>>>();
	private ArrayList<Map<String, String>> slot;
	private Map<String, String> data;

	public Parser(String fileLoc) {
		Parser.fileLoc = fileLoc;
	}

	public Map<String, ArrayList<Map<String, String>>> parse() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(new File(fileLoc));

			// normalize text representation
			doc.getDocumentElement().normalize();

			slot = new ArrayList<Map<String, String>>();
			getCategoryNodeData(doc.getElementsByTagName("CategoryData"));
			slot = new ArrayList<Map<String, String>>();
			getMovesNodeData(doc.getElementsByTagName("MoveData"));
			slot = new ArrayList<Map<String, String>>();
			getAdultRankNodeData(doc.getElementsByTagName("ARanks"));
			//slot = new ArrayList<Map<String, String>>();
			//getImageNodeData(doc.getElementsByTagName("Images"));
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return bigList;
	}

	protected void getMovesNodeData(NodeList Nodes) {
		Log.e("kepano", "getMovesNodeData:  Length = " + Nodes.getLength());
		for (int s = 0; s < Nodes.getLength(); s++) {
			Node singleNode = Nodes.item(s);
			if (singleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) singleNode;
				data = new Hashtable<String, String>();
				data.put("ID", getNodeData(element.getElementsByTagName("ID")));
				data.put("Moves",
						getNodeData(element.getElementsByTagName("Moves")));
				data.put("Category", getNodeData(element.getElementsByTagName("Category")));
				data.put("ARank", getNodeData(element.getElementsByTagName("ARank")));
				//data.put("KRank", getNodeData(element.getElementsByTagName("KRank")));
				slot.add(data);
			}
		}
		addToBigList("MoveData");

	}

	protected void getAdultRankNodeData(NodeList Nodes) {
		Log.e("kepano", "getAdultRankNodeData:  Length = " + Nodes.getLength());
		for (int s = 0; s < Nodes.getLength(); s++) {
			Node singleNode = Nodes.item(s);
			if (singleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) singleNode;
				data = new Hashtable<String, String>();
				data.put("ID", getNodeData(element.getElementsByTagName("ID")));
				data.put("Belt",
						getNodeData(element.getElementsByTagName("Belt")));
				slot.add(data);
			}
		}
		addToBigList("ARanks");

	}

	protected void getCategoryNodeData(NodeList Nodes) {
		Log.e("kepano", "getCategoryNodeData:  Length = " + Nodes.getLength());
		for (int s = 0; s < Nodes.getLength(); s++) {
			Node singleNode = Nodes.item(s);
			if (singleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) singleNode;
				data = new Hashtable<String, String>();
				data.put("ID", getNodeData(element.getElementsByTagName("ID")));
				data.put("AreaMoveIsIn",
						getNodeData(element.getElementsByTagName("AreaMoveIsIn")));
				slot.add(data);
			}
		}
		addToBigList("Category");

	}
	
	protected void getImageNodeData(NodeList Nodes) {
		Log.e("kepano", "getImageNodeData:  Length = " + Nodes.getLength());
		for (int s = 0; s < Nodes.getLength(); s++) {
			Node singleNode = Nodes.item(s);
			if (singleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) singleNode;
				data = new Hashtable<String, String>();
				data.put("PrimaryKey", getNodeData(element.getElementsByTagName("PrimaryKey")));
				data.put("PicID",
						getNodeData(element.getElementsByTagName("PicID")));
				data.put("PicNum",
						getNodeData(element.getElementsByTagName("PicNum")));
				data.put("Picture",
						getNodeData(element.getElementsByTagName("Picture")));
				slot.add(data);
			}
		}
		addToBigList("Images");

	}


	protected String getNodeData(NodeList n) {
		//Log.e("kepano", n.getLength() + "");
		return n.item(0).getFirstChild().getNodeValue();
	}

	protected void addToBigList(String id) {
		bigList.put(id, slot);
	}
}
